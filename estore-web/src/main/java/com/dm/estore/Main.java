package com.dm.estore;

import com.dm.estore.server.WebServer;
import com.dm.estore.server.WebServerConfig;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 5:08 PM
 */
public class Main {

    protected WebServer server;

    public static void main(String... anArgs) throws Exception {
        new Main().start();
    }

    protected WebServer createWebServer() {
        // TODO: load configuration from file
        server = new WebServer(WebServerConfig.Factory.newDevelopmentConfig(8080, "localhost"));
        server.setRunningInShadedJar(Boolean.TRUE);
        return server;
    }

    public Main() {
        createWebServer();
    }

    public void start() throws Exception {
        server.start();
        server.join();
    }
}
