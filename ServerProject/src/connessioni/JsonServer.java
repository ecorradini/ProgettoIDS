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

    private HashMap<String,HashMap<String,GrafoTronchi>> grafiPiani;
    private HttpServer server;
    private Thread worker;

    public JsonServer() throws IOException {

        popolaGrafi();
        System.out.println("Il server ora accetta richieste. Grazie per l'attesa.");

        server = HttpServer.create(new InetSocketAddress(9600), 0);
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
                            System.out.println(path+link);

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

        server.createContext("/parametri", new HttpHandler() {
            public void handle(HttpExchange arg0) throws IOException {
                new Thread(){
                    public void run()  {
                        try {
                            String response = DAOParametri.selectParametri(Integer.getInteger(arg0.getRequestURI().getQuery()));
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
                            Percorso percorso = new Percorso(arg0.getRequestURI().getQuery());
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

    public void popolaGrafi() {
        Thread popolamento = new Thread() {
            public void run() {
                System.out.println("Avvio il caricamento delle mappe. Attendere per favore.");
                int nEdifici = DAOEdificio.selectCountEdifici();
                int nPiani = DAOPiano.selectCountPiani();
                int numeroTotaleDati = nEdifici*nPiani;
                int percentualeCompletamento=0;
                grafiPiani = new HashMap<>();
                ArrayList<String> edifici = DAOEdificio.selectEdifici();
                for (int i = 0; i < edifici.size(); i++) {
                    ArrayList<String> piani = DAOPiano.selectListaPianiByEdificio(edifici.get(i));
                    HashMap<String, GrafoTronchi> value = new HashMap<>();
                    for (int j = 0; j < piani.size(); j++) {
                        value.put(piani.get(j), new GrafoTronchi(piani.get(j)));
                        percentualeCompletamento += (100/(numeroTotaleDati));
                        System.out.print("\rPercentuale di completamento: "+percentualeCompletamento+"%\r");
                    }
                    grafiPiani.put(edifici.get(i), value);
                    if(percentualeCompletamento<100) {
                        System.out.print("\rPercentuale di completamento: 100%\r");
                    }
                }
            }
        };
        popolamento.start();
        try {
            popolamento.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
