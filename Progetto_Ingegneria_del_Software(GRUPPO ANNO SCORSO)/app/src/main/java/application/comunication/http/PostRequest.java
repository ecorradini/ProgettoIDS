package application.comunication.http;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import application.MainApplication;

/**
 * Classe che implementa una HTTP POST, impacchetta un messaggio passandolo come parametro
 * L'url della risorsa sarà http://[ipserver]:8080/[uri della risorsa]
 */

public class PostRequest extends AsyncTask<String,Void,String> {

    private String result;

    private static final String PORT = "8080";

    private static final String SERVER_ID = "RestfulServerTID";

    /**
     * La richiesta viene effettuata attraverso un oggetto HttpURLConnection che permette di costruire
     * una connessione utilizzando il protocollo HTTP.
     * @param urls
     * @return un stringa di true/false che rappresenta la risposta del server
     */

    @Override
    protected String doInBackground(String... urls) {


        URL url = null;
        try {
            url = new URL("http://" + urls[0] + ":" + PORT + "/" + SERVER_ID + "/" + urls[1]);
            Log.i("URL","url: " + url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        }catch (SocketTimeoutException e1){
            Toast.makeText(MainApplication.getCurrentActivity().getApplicationContext(), "Connessione al server scaduta, riavviare l'applicazione", Toast.LENGTH_SHORT).show();
            MainApplication.setOnlineMode(false);
            Log.e("errore","sessione scadura");
        } catch (IOException e) {
            e.printStackTrace();
        }

        connection.setConnectTimeout(5000);

        try {

            connection.setDoOutput(true);   //abilita la scrittura
            connection.setRequestMethod("POST");
                //scritto header http del messaggio (per inviare json)
            connection.setRequestProperty("Content-Type", "application/json");


            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            Log.i("json",urls[2]);
            wr.write(urls[2]);
            wr.flush();
            wr.close();



        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (connection.getResponseCode() == 200) {
                InputStreamReader is = new InputStreamReader(connection.getInputStream());
                BufferedReader read = new BufferedReader(is);
                String s = null;
                StringBuffer sb = new StringBuffer();
                try {
                    while ((s = read.readLine()) != null) {
                        sb.append(s);
                    }
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    is.close();
                }
                result = sb.toString();

            }
            else if (connection.getResponseCode()==201) {
                result = "true";
            }
            else if (connection.getResponseCode()==500) {
                result = "false";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                Log.i("Response"," " + connection.getResponseCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection!=null) connection.disconnect();


        }
        return result;
    }


}
