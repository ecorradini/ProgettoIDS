package connessioni;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class JsonServer {
    private HttpServer server;
    private Thread worker;

    public JsonServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(9600),0);
        server.createContext("/edificio",new )
    }

}
