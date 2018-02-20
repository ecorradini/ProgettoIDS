package it.getout.gestioneconnessioni;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class ServerHelper {
    //AsyncTask che richiede il percorso al Server
    private class richiediPercorso extends AsyncTask<PointF[],Void,Boolean> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(PointF[]... points) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                //ho il percorso del server
            }
            else {
                //errore download
            }
        }
    }
}
