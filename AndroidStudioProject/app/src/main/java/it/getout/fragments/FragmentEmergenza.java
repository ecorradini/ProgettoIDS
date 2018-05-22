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
import it.getout.gestioneposizione.Posizione;
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

            getFragmentManager().beginTransaction().replace(R.id.mappa_container, ((Client)getActivity()).getMappaFragment()).commit();

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
                    final ArrayList<Tronco> percorso = gestoreEntita.scaricaPercorso("");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((Client) getActivity()).getMappaFragment().disegnaPercorso(percorso);
                        }
                    });
                    ((Client)getActivity()).stopLoadingPhase2();

                    while (true) {
                        String beaconA = Posizione.getIDBeaconAttuale();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!beaconA.equals(Posizione.getIDBeaconAttuale())) {
                            final ArrayList<Tronco> percorsoN = gestoreEntita.scaricaPercorso("");
                            getActivity().runOnUiThread(new Runnable() {
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
