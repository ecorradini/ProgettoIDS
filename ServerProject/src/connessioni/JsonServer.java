package connessioni;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import entita.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;


public class JsonServer {

    private HttpServer server;
    private Thread worker;

    public JsonServer() throws IOException {

        System.out.println("Il server ora accetta richieste. Grazie per l'attesa.");

        server = HttpServer.create(new InetSocketAddress(9600), 0);

        //scaricamento di tutti gli edifici per ottenimento dati offline EDO
        server.createContext("/downloadEdifici", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String queryEdifici = DAOEdificio.downloadEdicifi();
                            File file = new File(queryEdifici);
                            arg0.sendResponseHeaders(200, file.length());
                            OutputStream outputStream=arg0.getResponseBody();
                            Files.copy(file.toPath(), outputStream);
                            outputStream.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });


        //scaricamento di tutti i piani per ottenimento dati offline EDO
        server.createContext("/downloadPiani", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String queryEdifici = DAOPiano.downloadPiani();
                            File file = new File(queryEdifici);
                            arg0.sendResponseHeaders(200, file.length());
                            OutputStream outputStream=arg0.getResponseBody();
                            Files.copy(file.toPath(), outputStream);
                            outputStream.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });




        server.createContext("/edificioattuale", new HttpHandler() {
            public void handle(HttpExchange arg0) {
                new Thread(){
                    public void run()  {
                        try {
                            String response = DAOEdificio.selectEdificioByBeacon(arg0.getRequestURI().getQuery());  //vuole una stringa e riprende una stringa
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });


        server.createContext("/pianoattuale", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = DAOPiano.selectPianoByBeacon(arg0.getRequestURI().getQuery());
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }

        });


        server.createContext("/beacontronco", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = DAOBeacon.selectAllBeaconsByTronco(arg0.getRequestURI().getQuery());
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }

        });

        server.createContext("/posizione", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = DAOBeacon.selectPosizioneById(arg0.getRequestURI().getQuery());
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }

        });

        server.createContext("/pianiedificio", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = DAOPiano.selectAllPianiByEdificio(arg0.getRequestURI().getQuery());
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });

        server.createContext("/aulepiano", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = DAOAula.selectAllAuleByPiano(arg0.getRequestURI().getQuery());
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });

        server.createContext("/tronchipiano", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = DAOTronco.selectAllTronchiByPiano(arg0.getRequestURI().getQuery());
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });

        server.createContext("/mappapiano", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String path = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("/ServerProject.jar","");
                            String link = DAOMappa.selectMappaByPiano(arg0.getRequestURI().getQuery());

                            File file = new File(path+link);
                            arg0.sendResponseHeaders(200, file.length());
                            OutputStream outputStream=arg0.getResponseBody();
                            Files.copy(file.toPath(), outputStream);
                            outputStream.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });

        server.createContext("/controllaEmergenza", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = Boolean.toString(DAOParametri.controllaEmergenza()) ;
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });

        //Inserisci l'utente nella lista di utenti collegati al beacon
        server.createContext("/sommautente", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(com.sun.net.httpserver.HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            boolean res = DAOBeacon.sumUser(arg0.getRequestURI().getQuery());
                            arg0.sendResponseHeaders(200, (res ? "{OK}":"{ERROR}").length());
                            OutputStream os = arg0.getResponseBody();
                            os.write((res ? "{OK}":"{ERROR}").getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();

            }
        });
        //Inserisci l'utente nella lista di utenti collegati al beacon
        server.createContext("/percorso", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(com.sun.net.httpserver.HttpExchange arg0) throws IOException {
                new Thread() {
                    public void run() {
                        try {
                            String[] arrivoDestinazione = arg0.getRequestURI().getQuery().split(",");
                            Percorso percorso = null;
                            if(arrivoDestinazione.length < 2) {
                                percorso = new Percorso(arrivoDestinazione[0]);
                            }
                            else {
                                percorso = new Percorso(arrivoDestinazione[0],arrivoDestinazione[1]);
                            }
                            String response = percorso.getResult();
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });

        server.createContext("/uscite", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(com.sun.net.httpserver.HttpExchange arg0) throws IOException {
                new Thread() {
                    public void run() {
                        try {
                            String response = DAOUscita.getBeaconUscitaEdificio(arg0.getRequestURI().getQuery());
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        });


        server.createContext("/downloaddatabase", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = new DownloadIniziale().getRisultato();
                            arg0.sendResponseHeaders(200, response.length());
                            OutputStream os = arg0.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
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
