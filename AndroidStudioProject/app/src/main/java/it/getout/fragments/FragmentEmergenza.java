package it.getout.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import it.getout.gestioneposizione.Posizione;
import it.getout.gestioneposizione.Tronco;
import it.getout.gestionevisualizzazionemappa.MappaFragment;

/**
 * Classe interfaccia grafica emergenza
 */
public class FragmentEmergenza extends Fragment {

    //View del fragment
    public View view;

    /**
     * Instanzia un nuovo fragment
     * @return nuovo fragment
     */
    public static FragmentEmergenza newInstance() {
        return new FragmentEmergenza();
    }

    /**
     * Metodo onCreate del Fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Metodo onCreateView del Fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null) {

            //Inflate del layout
            view = inflater.inflate(R.layout.fragment_emergenza, container, false);

            //Metto la mappa nel container adatto
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            MappaFragment mappa = ((Client) getActivity()).getMappaFragment();
            if(getActivity().getSupportFragmentManager().findFragmentByTag("MAPPA")!=null) {
                mappa = ((Client) getActivity()).recreateMappaFragment();
            }
            ft.add(R.id.mappa_container, mappa, "MAPPA");
            ft.commit();

            //Avvio lo scaricamento dei dati necessari
            new Thread() {
                public void run() {
                    GestoreEntita gestoreEntita = ((Client) getActivity()).getGestore();
                    //Lascio la barra di caricamento nel caso GestoreEntità non abbia finito lo scaricamento di tutti i dati
                    while (!gestoreEntita.isDownloadNecessariFinished()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //Quando GestoreEntità ha terminato lo scaricamento richiedo il percorso
                    final ArrayList<Tronco> percorso = gestoreEntita.scaricaPercorso("");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Disegno il percorso calcolato
                            ((Client) getActivity()).getMappaFragment().disegnaPercorso(percorso);
                        }
                    });
                    //Rendo invisibile la barra di caricamento
                    ((Client) getActivity()).stopLoadingPhase2();

                    //Ricalcolo il percorso ogni volta che si cambia beacon
                    while (true) {
                        String beaconA = Posizione.getIDBeaconAttuale();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!beaconA.equals(Posizione.getIDBeaconAttuale())) {
                            final ArrayList<Tronco> percorsoN = gestoreEntita.scaricaPercorso("");
                            ((Client) getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((Client) getActivity()).getMappaFragment().disegnaPercorso(percorsoN);
                                }
                            });
                        }
                    }
                }
            }.start();
        }
        return view;
    }
}
