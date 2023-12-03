package server;

import com.sun.net.httpserver.*;
import config.Config;
import utilites.InitializeHelper;
import utilites.MongoDBHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class RecipeAppServer {
  public static void main(String[] args) throws IOException {
    InitializeHelper.init();
    // create a thread pool to handle requests
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    // create a server
    HttpServer server = HttpServer.create(
        new InetSocketAddress(Config.getServerHostname(), Config.getServerPort()),
        0);

    server.createContext("/api/", new APIHandler());
    server.createContext("/auth/", new AuthHandler());

    server.setExecutor(threadPoolExecutor);
    server.start();

    System.out.println("Server started on port: " + server.getAddress().getPort());
  }
}