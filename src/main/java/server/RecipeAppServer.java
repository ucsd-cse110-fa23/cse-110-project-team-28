package server;

import com.sun.net.httpserver.*;

import utilites.InitializeHelper;
import utilites.MongoDBHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class RecipeAppServer {
  // initialize server port and hostname
  private static final int SERVER_PORT = 8000;
  private static final String SERVER_HOSTNAME = "localhost";

  public static void main(String[] args) throws IOException {
    // initialize
    InitializeHelper.init();

    // create a thread pool to handle requests
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    // create a server
    HttpServer server = HttpServer.create(
        new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
        0);

    // create contexts
    server.createContext("/api/", new APIHandler());
    server.createContext("/auth/", new AuthHandler()); // todo: eventually will be merged into api

    // set executor
    server.setExecutor(threadPoolExecutor);
    server.start();

    System.out.println("Server started on port: " + SERVER_PORT);
  }
}