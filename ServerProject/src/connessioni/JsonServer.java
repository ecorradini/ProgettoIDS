package connessioni;

import com.sun.net.httpserver.HttpServer;
import entita.Edificio;
import entita.Piano;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;



public class JsonServer {
    private HttpServer server;
    private Thread worker;

    public JsonServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(9600),0);

        server.createContext("/edificio",new com.sun.net.httpserver.HttpHandler() {
            public void handle(com.sun.net.httpserver.HttpExchange arg0) throws IOException {

                String response = Edificio.selectEdificioByBeacon(arg0.getRequestURI().getQuery());  //vuole una stringa e riprende una stringa
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        } );

        server.createContext("/piano", new com.sun.net.httpserver.HttpHandler() {
            public void handle(com.sun.net.httpserver.HttpExchange arg0) throws IOException {
                String response = Piano.selectAllPianiByEdificio(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
    }

}
