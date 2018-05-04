package it.getout.gestionevisualizzazionemappa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import it.getout.R;
import it.getout.gestioneposizione.Posizione;
import it.getout.gestioneposizione.Tronco;

/**
 * Created by ecorradini on 23/02/18.
 */

public class MappaFragment extends Fragment {

    public View view;
    private TouchImageView immMappa;

    public static MappaFragment newInstance() {
        return new MappaFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mappa_fragment, container, false);

        immMappa = (TouchImageView)view.findViewById(R.id.image_mappa);
        immMappa.setMaxZoom(4f);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        disegnaPosizione();
    }


    /*public void disegnaPercorso() {
        ArrayList<Tronco> percorso = Posizione.getPercorso().getTronchi();
        //Instanzio un Bitmap temporaneo delle dimensioni dell'ImageView che lo conterrà
        Bitmap tempMappa = Bitmap.createBitmap(immMappa.getMeasuredWidth(),immMappa.getMeasuredHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempMappa);
        //Disegno questo Bitmap in un canvas
        tempCanvas.drawBitmap(tempMappa,0,0,null);
        //Definisco il Paint di ciascun tronco selezionato
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //Disegno il rettangolo per ogni tronco
        for(int i=0; i<percorso.size(); i++) {
            /*

                *------T------*
                |             |
                |             |
                |             |
                |             |
                L             R
                |             |
                |             |
                |             |
                |             |
                *------B------*

             */

         /*   //La coordinata left è la distanza del lato L dalla parte sinistra del canvas:
            //quindi è dato dalla cordinata X di inizio del tronco - metà della larghezza
            float left = percorso.get(i).getInizio().x - (percorso.get(i).getLarghezza()/2);
            //La coordinata right è la distanza del lato R dalla parte sinistra del canvas:
            //quindi è dato da left + larghezza
            float right = left + percorso.get(i).getLarghezza();
            //La coordinata top è la distanza del lato T dalla parte superiore del canvas:
            //quindi è la coordinata y di inizio del tronco
            float top = percorso.get(i).getInizio().y;
            //La coorinata bottom è la distanza del lato B dalla parte superiore del canvas:
            //quindi è la coordinata y di fine del tronco
            float bottom = percorso.get(i).getFine().y;

            //Posso ora disegnare il rettangolo
            tempCanvas.drawRect(new RectF(left,top,right,bottom),paint);

            immMappa.setImageDrawable(new BitmapDrawable(getResources(),tempMappa));
        }

    }*/

    public void disegnaPosizione() {
        /*
        //Instanzio un Bitmap temporaneo delle dimensioni dell'ImageView che lo conterrà
        Bitmap tempMappa = Bitmap.createBitmap((int)Mappa.getWidth(),(int)Mappa.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas();
        //Disegno questo Bitmap in un canvas
        tempCanvas.drawBitmap(tempMappa,0,0,null);
        //Definisco il Paint di ciascun tronco selezionato
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        //Definisco le coordinate del punto
        float x = Posizione.getPosizione().x;
        float y = Posizione.getPosizione().y;

        //Disegno il punto
        tempCanvas.drawCircle(x, y, 10, paint);

        immMappa.setImageDrawable(new BitmapDrawable(getResources(),tempMappa));*/

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#00c853"));


        Bitmap workingBitmap = Bitmap.createBitmap(Mappa.getMappa());
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Definisco le coordinate del punto
        float x = Posizione.getPosizione().x;
        float y = Posizione.getPosizione().y;

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(x, y, 25, paint);

        //immMappa.setAdjustViewBounds(true);
        immMappa.setImageBitmap(mutableBitmap);

    }
}
