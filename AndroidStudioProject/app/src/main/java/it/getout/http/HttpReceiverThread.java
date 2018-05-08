package it.getout.http;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import it.getout.Client;


public class HttpReceiverThread extends Thread{

    private Socket socket;

    HttpReceiverThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader is;
        PrintWriter os;
        String line;




        try {
            //Quando arriva la richesta da parte del serve di connessione http per inviare le notifiche,
            //l'applicazione apre un canale in ingresso e inizia a leggere la stringa che rappresenta
            //le notifiche scritte sotto forma di documento JSON
            InputStreamReader lettore = new InputStreamReader(socket.getInputStream());
            is = new BufferedReader(lettore);
            line = is.readLine();
            StringBuilder raw = new StringBuilder();
            raw.append("" + line);
            boolean isPost = line.startsWith("POST");
            int contentLength = 0;
            String request;


            while (!(line = is.readLine()).equals("")) {
                raw.append('\n' + line);
                if (isPost) {
                    final String contentHeader = "Content-Length: ";
                    if (line.startsWith(contentHeader)) {
                        contentLength = Integer.parseInt(line.substring(contentHeader.length()));
                    }
                }
            }
            StringBuilder body = new StringBuilder();
            if (isPost) {
                int c = 0;
                for (int i = 0; i < contentLength; i++) {
                    c = is.read();
                    if(c>20)
                        body.append( (char) c);
                }
            }
            Log.i("POST: ", body.toString());

            Client.launchNotification();

            os = new PrintWriter(socket.getOutputStream(), true);

            String response =
                    "<html><head></head>" +
                            "<body>" +
                            "<h1>" + "INVIO RIUSCITO" + "</h1>" +
                            "</body></html>";

            os.print("HTTP/1.0 200" + "\r\n");
            os.print("Content type: text/html" + "\r\n");
            os.print("Content length: " + response.length() + "\r\n");
            os.print("\r\n");
            os.print(response + "\r\n");
            os.flush();
            socket.close();


        } catch (IOException e) {

            e.printStackTrace();
        }

        return;
    }

}


