package connessioni;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import entita.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonServer {
    private HttpServer server;
    private Thread worker;

    public JsonServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(9600), 0);

        server.createContext("/edificio", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = Edificio.selectEdificioByBeacon(arg0.getRequestURI().getQuery());  //vuole una stringa e riprende una stringa
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        });


        server.createContext("/piano", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = Piano.selectPianoByBeacon(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        });


        server.createContext("/beacon", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = Beacon.selectAllBeaconsByTronco(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        });

        server.createContext("/beacon", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = Beacon.selectPosizioneById(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        });

        server.createContext("/piano", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = Piano.selectAllPianiByEdificio(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.createContext("/aula", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = Aula.selectAllAuleByPiano(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.createContext("/tronco", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = Tronco.selectAllTronchiByPiano(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.createContext("/mappa", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = Mappa.selectMappaByPiano(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        // connectionhendler server json server


        server.setExecutor(null); // creates a default executor
        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                server.start();
            }
        });
        worker.start();
    }
}
