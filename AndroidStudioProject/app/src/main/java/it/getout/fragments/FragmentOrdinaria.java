package it.getout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.getout.Client;
import it.getout.R;
import it.getout.gestioneposizione.GestoreEntita;
import it.getout.gestioneposizione.Posizione;
import it.getout.gestioneposizione.Tronco;
import it.getout.gestionevisualizzazionemappa.MappaFragment;

/**
 * Classe per la visualizzazione dell'interfaccia della modalità ordinaria
 */
public class FragmentOrdinaria extends Fragment {

    //View del fragment
    public View view;
    //Bottone per visualizzare la lista della aule
    private FloatingActionButton button_ordinaria;
    //Nome dell'aula di destinazione
    private String aula;
    //Variabile se il percorso è già stato calcolato
    private boolean percorsoFlag = false;

    /**
     * Metodo che restituisce
     * @return
     */
    public static FragmentOrdinaria newInstance() {
        return new FragmentOrdinaria();
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
        if (view == null) {
            //Inflater del Fragment
            view = inflater.inflate(R.layout.fragment_ordinaria, container, false);
            //Istanzio il bottone
            button_ordinaria = (FloatingActionButton)view.findViewById(R.id.floating_botton);

            //Metto il fragment della mappa
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            MappaFragment mappa = ((Client) getActivity()).getMappaFragment();
            if(getActivity().getSupportFragmentManager().findFragmentByTag("MAPPA")!=null) {
                mappa = ((Client) getActivity()).recreateMappaFragment();
            }
            ft.add(R.id.mappa_container_ordinaria, mappa, "MAPPA");
            ft.commit();

            new Thread() {
                public void run() {
                    //Aspetto che GestoreEntita finisca il download
                    GestoreEntita gestoreEntita = ((Client) getActivity()).getGestore();
                    while (!gestoreEntita.isDownloadNecessariFinished()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //Rendo invisibile la barra di caricamento
                    ((Client)getActivity()).stopLoadingPhase2();

                    ((Client)getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Rendo il bottone visibile
                            button_ordinaria.setVisibility(View.VISIBLE);
                            button_ordinaria.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Se clicco nel bottone rendo visibile la lista delle aule))
                                    setButton(false);
                                    FragmentListaAule fragmentListaAule = FragmentListaAule.newInstance();
                                    fragmentListaAule.setParent(FragmentOrdinaria.this);
                                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit).replace(R.id.mappa_container_ordinaria, fragmentListaAule, "LISTAAULE").addToBackStack(null).commit();
                                }
                            });
                        }
                    });

                    while (true) {
                        //Ogni volta che cambio beacon ricalcolo il percorso
                        String beaconA = Posizione.getIDBeaconAttuale();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (percorsoFlag){
                            final ArrayList<Tronco> percorso = gestoreEntita.scaricaPercorso(aula);
                            ((Client) getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((Client) getActivity()).getMappaFragment().disegnaPercorso(percorso);
                                }
                            });
                            percorsoFlag = false;
                        }

                        if (!beaconA.equals(Posizione.getIDBeaconAttuale())) {
                            final ArrayList<Tronco> percorso = gestoreEntita.scaricaPercorso(aula);
                            ((Client) getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((Client) getActivity()).getMappaFragment().disegnaPercorso(percorso);
                                }
                            });
                        }
                    }
                }
            }.start();

        }
        return view;
    }

    /**
     * Metodo che setta l'aula di destinazione
     * @param aula aula di destinazione
     */
    public void setAula(String aula) {
        this.aula = aula;
        percorsoFlag = true;
    }

    /**
     * Metodo che setta la visibilità del bottone
     * @param visibile true:visibile, false:invisibile
     */
    public void setButton(boolean visibile) {
        if(visibile) {button_ordinaria.setVisibility(View.VISIBLE);}
        else {button_ordinaria.setVisibility(View.GONE);}
    }
}
