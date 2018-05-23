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

public class FragmentListaAule extends Fragment {
    public View view;

    private RecyclerView rv;

    public static FragmentListaAule newInstance() {
        return new FragmentListaAule();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_lista_aule, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv_lista_aule);


        rv.hasFixedSize();
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);

        RVAdapterAule rvaule = new RVAdapterAule(Posizione.getEdificioAttuale().getPiani(),view.getContext());
        rv.setAdapter(rvaule);

        return view;
    }
}
