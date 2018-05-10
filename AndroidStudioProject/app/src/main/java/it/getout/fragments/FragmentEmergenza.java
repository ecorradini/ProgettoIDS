package it.getout.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.ArrayList;

import it.getout.Client;
import it.getout.R;
import it.getout.gestioneposizione.GestoreEntita;
import it.getout.gestioneposizione.Tronco;

public class FragmentEmergenza extends Fragment {

    public View view;

    public static FragmentEmergenza newInstance() {
        return new FragmentEmergenza();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_emergenza, container, false);

            //TextView textEmergenza = (TextView)view.findViewById(R.id.textEmergenza);
            //textEmergenza.startAnimation(getBlinkAnimation());

            getFragmentManager().beginTransaction().replace(R.id.mappa_container, ((Client)getActivity()).getMappaFragment()).commit();

            //((Client)getActivity()).getMappaFragment().disegnaPosizione();

            new Thread() {
                public void run() {
                    GestoreEntita gestoreEntita = ((Client)getActivity()).getGestore();
                    while(!gestoreEntita.isDownloadFinished()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ((Client)getActivity()).getMappaFragment().disegnaPercorso(gestoreEntita.scaricaPercorso());
                }
            }.start();


        }
        return view;
    }
}
