package application.communication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import application.communication.http.DeleteRequest;
import application.communication.http.GetRequest;
import application.communication.http.PostRequest;
import application.communication.http.PutRequest;
import application.communication.message.MessageBuilder;
import application.communication.message.MessageParser;


/**
 * Questa classe serve per gestire la comunicazione con il server
 */

public class ServerComunication{

    private static String hostMaster = "172.23.147.80"; //= hostname;
    private static JSONObject jsonObject;
    private static final ArrayList<String> userProfileKeys = new ArrayList<String>(){{
        add("email");
        add("password");
        add("nome");
        add("cognome");
        add("data_nascita");
        add("luogo_nascita");
        add("provincia");
        add("stato");
        add("telefono");
        add("sesso");
        add("cod_fis");
    }};



    public static String getIP() {
        return hostMaster;
    }

    public static void setHostMaster (String h) {
        hostMaster = h;
    }

    //TODO /attuatori, consumo_tot(una stringa), consumo_att(una stringa), modpriorità

    public static HashMap<String,String>[] listaAttuatori() throws ExecutionException, InterruptedException, JSONException {
        ArrayList<String> keys = new ArrayList<>();

        keys.add("at_id");
        keys.add("nome");
        keys.add("descrizione");
        keys.add("cat_id");
        keys.add("priorita");
        keys.add("categoria");

        return MessageParser.analyzeMessageArray(new GetRequest().execute(hostMaster,"attuatori").get(),keys,"attuatori");
    }

    public static HashMap<String,String> consumo_tot() throws ExecutionException, InterruptedException, JSONException {
        ArrayList<String> key = new ArrayList<>();
        key.add("consumo_tot");
        return MessageParser.analyzeMessage(new GetRequest().execute(hostMaster,"consumototale").get(),key);
    }

    public static HashMap<String,String> consumo_attuatore(int id_att) throws ExecutionException, InterruptedException, JSONException {
        ArrayList<String> key = new ArrayList<>();
        key.add("consumo_att");
        return MessageParser.analyzeMessage(new GetRequest().execute(hostMaster,"consumoattuatore?"+id_att).get(),key);
    }

    public static HashMap<String,String> togglePlug(int id_att) throws ExecutionException, InterruptedException, JSONException {
        ArrayList<String> key = new ArrayList<>();
        return MessageParser.analyzeMessage(new GetRequest().execute(hostMaster,"togglepresa?"+id_att).get(),key);
    }

    public static HashMap<String,String> turnPlug(int id_att, boolean state) throws ExecutionException, InterruptedException, JSONException {
        ArrayList<String> key = new ArrayList<>();
        return MessageParser.analyzeMessage(new GetRequest().execute(hostMaster,"impostapresa?"+id_att+"&"+(state ? "on":"off")).get(),key);
    }
}
