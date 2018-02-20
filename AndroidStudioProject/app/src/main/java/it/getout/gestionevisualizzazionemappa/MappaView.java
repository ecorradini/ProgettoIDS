package it.getout.gestionevisualizzazionemappa;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;

import it.getout.gestioneposizione.Percorso;

/**
 * Created by Alessandro on 01/02/2018.
 */

public class MappaView extends android.support.v7.widget.AppCompatImageView{
    public MappaView(Context context) {
        super(context);
    }

    private Canvas canvas;

    public void MappaView(Context context, AttributeSet attributeSet){   // costruttore non sicuro del void
    }

    protected void onDraw(Canvas canvas) {

    }

    public void disegnaPercorso(Canvas canvas, Percorso percorso) {

    }

    public void disegnaPosizione(Canvas canvas, PointF point) {

    }

}
