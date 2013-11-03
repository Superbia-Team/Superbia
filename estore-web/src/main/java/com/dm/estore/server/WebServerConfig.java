package com.dm.estore.server;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 6:11 PM
 */
public interface WebServerConfig
{
    public String getServerName();

    public int getPort();

    public String getHostInterface();

    public int getMinThreads();

    public int getMaxThreads();

    public String getAccessLogDirectory();

    int getSSLPort();

    public class Factory {

        public static final String SERVER_NAME = "eStore";
        public static final String DEFAULT_LOGS_FOLDER = "./target/logs/";

        public static WebServerConfig newDevelopmentConfig(int serverPort, String serverInterface) {
            return new Development(serverPort, serverInterface);
        }

        public static WebServerConfig newProductionConfig(int serverPort, String serverInterface, int minThreads, int maxThreads) {
            return new Production(serverPort, serverInterface, minThreads, maxThreads);
        }

        static abstract class AbstractWebServerConfig implements WebServerConfig
        {
            private int port;
            private int sslPort;
            private String serverInterface;
            private int minThreads;
            private int maxThreads;

            private AbstractWebServerConfig(int serverPort, String serverInterface, int minThreads, int maxThreads, int sslPort)
            {
                port = serverPort;
                this.serverInterface = serverInterface;
                this.minThreads = minThreads;
                this.maxThreads = maxThreads;
                this.sslPort = sslPort;
            }

            @Override
            public String getServerName()
            {
                return SERVER_NAME;
            }

            @Override
            public int getPort()
            {
                return port;
            }

            @Override
            public String getHostInterface()
            {
                return serverInterface;
            }

            @Override
            public int getMinThreads()
            {
                return minThreads;
            }

            @Override
            public int getMaxThreads()
            {
                return maxThreads;
            }

            @Override
            public String getAccessLogDirectory()
            {
                return DEFAULT_LOGS_FOLDER;
            }

            @Override
            public int getSSLPort()
            {
                return sslPort;
            }
        }

        public static final class Development extends AbstractWebServerConfig
        {
            public Development(int aPort, String anInterface)
            {
                super(aPort, anInterface, 5, 15, 8443);
            }
        }

        public static final class Production extends AbstractWebServerConfig
        {
            public Production(int aPort, String anInterface, int aMinThreads, int aMaxThreads)
            {
                super(aPort, anInterface, aMinThreads, aMaxThreads, 8443);
            }
        }
    }
}
