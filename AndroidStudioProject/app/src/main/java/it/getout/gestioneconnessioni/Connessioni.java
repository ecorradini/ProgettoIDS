package it.getout.gestioneconnessioni;

import android.content.Context;

/**
 * Created by Edoardo on 15/03/2018.
 */

public class Connessioni {
    private static DBHelper dbReference;
    private static ServerHelper serverReference;


    public static void init(Context c) {
        dbReference = new DBHelper(c);
        serverReference = new ServerHelper(c);
    }


    public static DBHelper getDbReference() { return dbReference; }             ///

    public static ServerHelper getServerReference() { return serverReference; }   ///

}
