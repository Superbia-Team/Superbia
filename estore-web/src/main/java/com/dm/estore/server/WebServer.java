package com.dm.estore.server;

import org.eclipse.jetty.annotations.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 5:13 PM
 */
public class WebServer {

    private static final String WEB_XML = "META-INF/MANIFEST.MF";

    private static final String PROJECT_RELATIVE_PATH_TO_WEBAPP = "src/main/webapp";

    private Server server;
    private WebServerConfig config;
    private boolean runningInShadedJar;

    public static interface WebContext
    {
        public File getWarPath();
        public String getContextPath();
    }

    public WebServer(WebServerConfig aConfig)
    {
        config = aConfig;
    }

    public void start() throws Exception
    {
        server = new Server(createThreadPool());

        List<ServerConnector> connectors = createConnectors();
        for (ServerConnector connector : connectors) {
            server.addConnector(connector);
        }

        server.setHandler(createHandlers());
        server.setStopAtShutdown(true);

        server.start();
    }

    public void join() throws InterruptedException
    {
        server.join();
    }

    public void stop() throws Exception
    {
        server.stop();
    }

    private ThreadPool createThreadPool()
    {
        QueuedThreadPool _threadPool = new QueuedThreadPool();
        _threadPool.setName(config.getServerName());
        _threadPool.setMinThreads(config.getMinThreads());
        _threadPool.setMaxThreads(config.getMaxThreads());
        return _threadPool;
    }

    private List<ServerConnector> createConnectors() {
        List<ServerConnector> connectors = new ArrayList<ServerConnector>();

        // ======================================================================
        // HTTP Configuration
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(config.getSSLPort());
        http_config.setOutputBufferSize(32768);
        http_config.setRequestHeaderSize(8192);
        http_config.setResponseHeaderSize(8192);
        http_config.setSendServerVersion(true);
        http_config.setSendDateHeader(false);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(config.getPort());
        http.setSoLingerTime(-1);
        connectors.add(http);

        // ======================================================================
        // SSL Context Factory
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(getResource("keystore").toString());
        sslContextFactory.setKeyStorePassword("a12345678");
        sslContextFactory.setKeyManagerPassword("a12345678");
        sslContextFactory.setTrustStorePath(getResource("keystore").toString());
        sslContextFactory.setTrustStorePassword("a12345678");
        sslContextFactory.setExcludeCipherSuites(
                "SSL_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_DSS_WITH_DES_CBC_SHA",
                "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
                "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");

        // SSL HTTP Configuration
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        // SSL Connector
        ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https_config));
        sslConnector.setPort(config.getSSLPort());
        connectors.add(sslConnector);

        return connectors;
    }

    private URL getResource(String aResource) {
        return Thread.currentThread().getContextClassLoader().getResource(aResource);
    }

    private HandlerCollection createHandlers()
    {
        WebAppContext _ctx = new WebAppContext();
        _ctx.setContextPath("/");
        if (runningInShadedJar) {
            _ctx.setBaseResource(new ResourceCollection(new String[]{ getShadedWarUrl(), "." }));
        } else {
            _ctx.setBaseResource(new ResourceCollection(new String[]{ PROJECT_RELATIVE_PATH_TO_WEBAPP }));
        }

        _ctx.setConfigurations (new Configuration[]
                {
                        // This is necessary because Jetty out-of-the-box does not scan
                        // the classpath of your project in Eclipse, so it doesn't find
                        // your WebAppInitializer.
                        new AnnotationConfiguration()
                        {
                            @Override
                            public void configure(WebAppContext context) throws Exception {
                                boolean metadataComplete = context.getMetaData().isMetaDataComplete();
                                context.addDecorator(new AnnotationDecorator(context));


                                //Even if metadata is complete, we still need to scan for ServletContainerInitializers - if there are any
                                AnnotationParser parser = null;
                                if (!metadataComplete)
                                {
                                    //If metadata isn't complete, if this is a servlet 3 webapp or isConfigDiscovered is true, we need to search for annotations
                                    if (context.getServletContext().getEffectiveMajorVersion() >= 3 || context.isConfigurationDiscovered())
                                    {
                                        _discoverableAnnotationHandlers.add(new WebServletAnnotationHandler(context));
                                        _discoverableAnnotationHandlers.add(new WebFilterAnnotationHandler(context));
                                        _discoverableAnnotationHandlers.add(new WebListenerAnnotationHandler(context));
                                    }
                                }

                                //Regardless of metadata, if there are any ServletContainerInitializers with @HandlesTypes, then we need to scan all the
                                //classes so we can call their onStartup() methods correctly
                                createServletContainerInitializerAnnotationHandlers(context, getNonExcludedInitializers(context));

                                if (!_discoverableAnnotationHandlers.isEmpty() || _classInheritanceHandler != null || !_containerInitializerAnnotationHandlers.isEmpty())
                                {
                                    parser = createAnnotationParser();
                                    parse(context, parser);

                                    for (AnnotationParser.DiscoverableAnnotationHandler h: _discoverableAnnotationHandlers)
                                        context.getMetaData().addDiscoveredAnnotations(((AbstractDiscoverableAnnotationHandler)h).getAnnotationList());
                                }

                            }

                            private void parse(final WebAppContext context, AnnotationParser parser) throws Exception
                            {
                                List<Resource> _resources = getResources(getClass().getClassLoader());

                                for (Resource _resource : _resources)
                                {
                                    if (_resource == null)
                                        return;

                                    parser.clearHandlers();
                                    for (AnnotationParser.DiscoverableAnnotationHandler h: _discoverableAnnotationHandlers)
                                    {
                                        if (h instanceof AbstractDiscoverableAnnotationHandler)
                                            ((AbstractDiscoverableAnnotationHandler)h).setResource(null); //
                                    }
                                    parser.registerHandlers(_discoverableAnnotationHandlers);
                                    parser.registerHandler(_classInheritanceHandler);
                                    parser.registerHandlers(_containerInitializerAnnotationHandlers);

                                    parser.parse(_resource.getURI(),
                                            new ClassNameResolver()
                                            {
                                                public boolean isExcluded (String name)
                                                {
                                                    if (context.isSystemClass(name)) return true;
                                                    if (context.isServerClass(name)) return false;
                                                    return false;
                                                }

                                                public boolean shouldOverride (String name)
                                                {
                                                    //looking at webapp classpath, found already-parsed class of same name - did it come from system or duplicate in webapp?
                                                    if (context.isParentLoaderPriority())
                                                        return false;
                                                    return true;
                                                }
                                            });
                                }
                            }

                            private List<Resource> getResources(ClassLoader aLoader) throws IOException
                            {
                                if (aLoader instanceof URLClassLoader)
                                {
                                    List<Resource> _result = new ArrayList<Resource>();
                                    URL[] _urls = ((URLClassLoader)aLoader).getURLs();
                                    for (URL _url : _urls)
                                        _result.add(Resource.newResource(_url));

                                    return _result;
                                }
                                return Collections.emptyList();
                            }
                        }
                });

        List<Handler> _handlers = new ArrayList<Handler>();

        _handlers.add(_ctx);

        HandlerList _contexts = new HandlerList();
        _contexts.setHandlers(_handlers.toArray(new Handler[0]));

        RequestLogHandler _log = new RequestLogHandler();
        _log.setRequestLog(createRequestLog());

        HandlerCollection _result = new HandlerCollection();
        _result.setHandlers(new Handler[] {_contexts, _log});

        return _result;
    }

    private RequestLog createRequestLog()
    {
        NCSARequestLog _log = new NCSARequestLog();

        File _logPath = new File(config.getAccessLogDirectory() + "yyyy_mm_dd.request.log");
        _logPath.getParentFile().mkdirs();

        _log.setFilename(_logPath.getPath());
        _log.setRetainDays(30);
        _log.setExtended(false);
        _log.setAppend(true);
        _log.setLogTimeZone("UTC");
        _log.setLogLatency(true);
        return _log;
    }

    private String getShadedWarUrl() {
        String _urlStr = getResource(WEB_XML).toString();
        return _urlStr.substring(0, _urlStr.length() - WEB_XML.length());
    }

    public void setRunningInShadedJar(boolean runningInShadedJar) {
        this.runningInShadedJar = runningInShadedJar;
    }
}
