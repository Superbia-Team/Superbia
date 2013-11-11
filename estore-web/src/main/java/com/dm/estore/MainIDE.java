package com.dm.estore;

import com.dm.estore.server.WebServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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
    
    @Override
    public void start() throws Exception {
        server.start();
        
        new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        in.readLine();
                        server.stop();
                        in.close();
                    } catch (Exception ex) {
                        System.out.println("Failed to stop Jetty");
                    }
                }
            }.start();
        
        server.join();
    }
}
