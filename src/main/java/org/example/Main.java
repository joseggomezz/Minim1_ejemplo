package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static final String BASE_URI = "http://localhost:8080/dsaApp/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("org.example.services");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        logger.info("Servidor arrancado en {}. Pulsa Enter para pararlo.", BASE_URI);
        System.in.read();
        server.stop();
    }
}