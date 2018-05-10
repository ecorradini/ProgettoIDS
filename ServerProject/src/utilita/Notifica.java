package utilita;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import entita.DAOUtente;

public class Notifica {
    private static Thread t;
    private static boolean running = true;
    private final static String USER_AGENT = "Mozilla/5.0";
    private static Runnable r;
    private static boolean stopRequest=false,working=false;
    private static int count;


    public static Runnable notificationThread(boolean stop, String msg){
        r = new Runnable() {

            @Override
            public void run() {
                ArrayList<String> ipList = DAOUtente.getAllUtenti();

                while(running){
                    String json =  "{ "
                            +"\"msg\" :" + "\""+ msg+"\" }";


                    System.out.println(json);


                    count = 0;
                    for(int i=0;i<ipList.size();i++){
                        String ip = ipList.get(i);
                        ip = "http://"+ip+":8888";
                        try {
                            if(running){
                                System.out.println("Comunicazione con "+ip);
                                count++;
                                working = true;
                                sendPost(ip, json);

                            }
                        }catch (ConnectException e1) {
                            System.out.println("Utente non raggiungibile ip: "+ip);
                        }catch(SocketTimeoutException e3){
                            System.out.println("Connessione scaduta con "+ip);
                        }catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }


                        //running=false;

                    }
                    //we have to reblock sending of messages
                    running=false;
                    //working = false;
                    if(ipList.size()==0){
                        System.out.println("Nessun utente collegato");
                    }else{
                        if(count==ipList.size()){
                            System.out.println("Ho terminato la lista degli utenti");
                        }
                    }
                    //System.out.println("Terminata la procedura di invio");



                }
            }
        };
        return r;
    }


    // HTTP POST request
    private static void sendPost(String url,String json) throws Exception {

        //String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept-Charset", "utf-8");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setConnectTimeout(5 * 1000);

        //String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        if(con!=null){
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            //wr.writeBytes(urlParameters);
            Charset.forName("UTF-8").encode(json);
            wr.writeChars(json);
            wr.flush();
            wr.close();
        }
        int responseCode = con.getResponseCode();
        System.out.println("Sending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + json);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
			/*
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
*/
        //print result
        System.out.println(response.toString());


    }

    public static void startThread(boolean s,String msg){
        if(t != null){
            if(!t.isAlive()){
                notificationThread(s,msg);
                t = new Thread(r);
                running = true;
                t.start();
            }
        }else{
            Runnable r = notificationThread(s,msg);
            t = new Thread(r);
            t.start();
        }


    }

    public static void stopThread(){
        if(t!=null){
            t.interrupt();
        }
        running = false;
        stopRequest = false;
        working = false;
        //System.out.println("running"+running);
    }

    public static boolean getStopRequest(){
        return stopRequest;
    }

    public static void setStopRequest(boolean s){
        stopRequest = s;
    }
    public static boolean isWorking() {
        return working;
    }
    public static void setWorking(boolean working) {
        Notifica.working = working;
    }

}
