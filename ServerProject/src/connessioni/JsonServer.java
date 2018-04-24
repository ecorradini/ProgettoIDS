package connessioni;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import entita.*;

import java.io.*;
import java.net.InetSocketAddress;


public class JsonServer {
    private HttpServer server;
    private Thread worker;

    public JsonServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(9600), 0);

        server.createContext("/edificioattuale", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                System.out.println("Richiesto DAOEdificio da DAOBeacon");
                String response = DAOEdificio.selectEdificioByBeacon(arg0.getRequestURI().getQuery());  //vuole una stringa e riprende una stringa
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        });


        server.createContext("/pianoattuale", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                System.out.println("Richiesto DAOPiano da DAOBeacon");
                String response = DAOPiano.selectPianoByBeacon(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        });


        server.createContext("/beacontronco", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                System.out.println("Richiesti Beacons da DAOTronco");
                String response = DAOBeacon.selectAllBeaconsByTronco(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        });

        server.createContext("/posizione", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                System.out.println("Richiesta Posizione da DAOBeacon");
                String response = DAOBeacon.selectPosizioneById(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }

        });

        server.createContext("/pianiedificio", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = DAOPiano.selectAllPianiByEdificio(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.createContext("/aulepiano", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                String response = DAOAula.selectAllAuleByPiano(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.createContext("/tronchipiano", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                System.out.println("Richiesti Tronchi da DAOPiano");
                String response = DAOTronco.selectAllTronchiByPiano(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        server.createContext("/mappapiano", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                System.out.println("Richieste Mappe da DAOPiano");
                String response = DAOMappa.selectMappaByPiano(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, response.length());
                OutputStream os = arg0.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        //Inserisci l'utente nella lista di utenti collegati al beacon
        server.createContext("/sommautente", new com.sun.net.httpserver.HttpHandler() {

            @Override
            public void handle(com.sun.net.httpserver.HttpExchange arg0) throws IOException {
                boolean res = DAOBeacon.sumUser(arg0.getRequestURI().getQuery());
                arg0.sendResponseHeaders(200, (res ? "{OK}":"{ERROR}").length());
                OutputStream os = arg0.getResponseBody();
                os.write((res ? "{OK}":"{ERROR}").getBytes());
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
