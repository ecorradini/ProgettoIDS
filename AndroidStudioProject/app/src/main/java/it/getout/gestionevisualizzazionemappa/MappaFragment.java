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
        if(view==null) {
            view = inflater.inflate(R.layout.mappa_fragment, container, false);

            immMappa = view.findViewById(R.id.image_mappa);
            immMappa.setMaxZoom(4f);
        }

        disegnaPosizione();

        return view;
    }


    public void disegnaPercorso(ArrayList<Tronco> percorso) {

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#FF5252"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);


        Bitmap workingBitmap = Bitmap.createBitmap(Mappa.getMappa());
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Il primo lo disegno a partire dal beacon
        Canvas canvas = new Canvas(mutableBitmap);
        float startX0 = Posizione.getBeaconAttuale().getPosizione().x;
        float starty0 = Posizione.getBeaconAttuale().getPosizione().y;
        float endx0 = percorso.get(1).getInizio().x;
        float endy0 = percorso.get(1).getInizio().y;

        canvas.drawLine(startX0,starty0,endx0,endy0,paint);

        for(int i=1; i<percorso.size();i++) {

            //La coordinata left è la distanza del lato L dalla parte sinistra del canvas:
            //quindi è dato dalla cordinata X di inizio del tronco - metà della larghezza
            float startX = percorso.get(i).getInizio().x;
            //La coordinata right è la distanza del lato R dalla parte sinistra del canvas:
            //quindi è dato da left + larghezza
            float startY = percorso.get(i).getInizio().y;
            //La coordinata top è la distanza del lato T dalla parte superiore del canvas:
            //quindi è la coordinata y di inizio del tronco
            float stopX = percorso.get(i).getFine().x;
            //La coorinata bottom è la distanza del lato B dalla parte superiore del canvas:
            //quindi è la coordinata y di fine del tronco
            float stopY = percorso.get(i).getFine().y;


            canvas.drawLine(startX,startY,stopX,stopY,paint);

        }

        Paint paintP = new Paint();
        paintP.setAntiAlias(true);
        paintP.setColor(Color.parseColor("#67c700"));

        //Definisco le coordinate del punto
        float x = Posizione.getPosizione().x;
        float y = Posizione.getPosizione().y;
        canvas.drawCircle(x, y, 15, paintP);

        immMappa.setImageBitmap(mutableBitmap);
    }

    public void disegnaPosizione() {

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#67c700"));


        Bitmap workingBitmap = Bitmap.createBitmap(Mappa.getMappa());
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        //Definisco le coordinate del punto
        float x = Posizione.getPosizione().x;
        float y = Posizione.getPosizione().y;

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(x, y, 15, paint);

        //immMappa.setAdjustViewBounds(true);
        immMappa.setImageBitmap(mutableBitmap);
        float focusX = x/mutableBitmap.getWidth();
        float focusY = y/mutableBitmap.getHeight();
        immMappa.setZoom(3.6f,focusX,focusY);
    }
}
