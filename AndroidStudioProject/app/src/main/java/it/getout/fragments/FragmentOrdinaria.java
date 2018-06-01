package it.getout.fragments;

import android.os.Bundle;
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

public class FragmentOrdinaria extends Fragment {

    public View view;
    private FloatingActionButton button_ordinaria;

    public static FragmentOrdinaria newInstance() {
        return new FragmentOrdinaria();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_ordinaria, container, false);
            button_ordinaria = (FloatingActionButton) view.findViewById(R.id.floating_botton);

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            MappaFragment mappa = ((Client) getActivity()).getMappaFragment();
            if(getActivity().getSupportFragmentManager().findFragmentByTag("MAPPA")!=null) {
                mappa = ((Client) getActivity()).recreateMappaFragment();
            }
            ft.add(R.id.mappa_container_ordinaria, mappa,"MAPPA");
            ft.commit();

            new Thread() {
                public void run() {
                    GestoreEntita gestoreEntita = ((Client) getActivity()).getGestore();
                    while (!gestoreEntita.isDownloadNecessariFinished()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ((Client) getActivity()).stopLoadingPhase2();

                    ((Client) getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button_ordinaria.setVisibility(View.VISIBLE);
                            button_ordinaria.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    button_ordinaria.setVisibility(View.INVISIBLE);
                                    getFragmentManager().beginTransaction().replace(R.id.mappa_container, FragmentListaAule.newInstance()).addToBackStack(null).commit();
                                }
                            });
                        }
                    });

                }
            }.start();
        }
        return view;
    }
}
