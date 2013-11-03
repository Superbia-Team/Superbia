package com.dm.estore;

import com.dm.estore.server.WebServer;

/**
 * User: denis
 * Date: 11/1/13
 * Time: 5:09 PM
 */
public class MainIDE extends Main {

    public static void main(String... anArgs) throws Exception {
        new MainIDE().start();
    }

    protected WebServer createWebServer() {
        WebServer server = super.createWebServer();
        server.setRunningInShadedJar(Boolean.FALSE);
        return server;
    }
}
