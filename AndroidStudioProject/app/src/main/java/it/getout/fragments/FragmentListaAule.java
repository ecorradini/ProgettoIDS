package it.getout.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.getout.R;
import it.getout.gestioneposizione.Posizione;
import it.getout.utilita.RVAdapterAule;

/**
 * Classe per la visualizzazione della lista delle aule
 */
public class FragmentListaAule extends Fragment {

    //View del fragment
    public View view;
    //Fragment genitore
    public FragmentOrdinaria parent;

    //RecyclerView per la visualizzazione della lista delle aule
    private RecyclerView rv;

    /**
     * Metodo che restituisce una nuova istanza della classe
     * @return
     */
    public static FragmentListaAule newInstance() {
        return new FragmentListaAule();
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

        //Inflater del layout
        view = inflater.inflate(R.layout.fragment_lista_aule, container, false);
        //Istanzio la RecyclerView
        rv = (RecyclerView) view.findViewById(R.id.rv_lista_aule);

        //Proprieta della RV
        rv.hasFixedSize();
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);

        //Istanzion l'adapter della RV
        RVAdapterAule rvaule = new RVAdapterAule(Posizione.getEdificioAttuale().getPiani(),view.getContext(),parent);
        rv.setAdapter(rvaule);

        return view;
    }

    /**
     * Metodo che setta il genitore del Fragment
     * @param ordinaria fragment modalita ordinaria
     */
    public void setParent(FragmentOrdinaria ordinaria) {
        parent = ordinaria;
    }
}
