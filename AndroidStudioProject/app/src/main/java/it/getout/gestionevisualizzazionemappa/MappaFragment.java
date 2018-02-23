package it.getout.gestionevisualizzazionemappa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import it.getout.R;

/**
 * Created by ecorradini on 23/02/18.
 */

public class MappaFragment extends Fragment {

    public View view;
    private ImageView immMappa;

    public static MappaFragment newInstance() {
        return new MappaFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.mappa_fragment, container, false);

            immMappa = (ImageView)view.findViewById(R.id.image_mappa);

            immMappa.setImageBitmap(Mappa.getMappa());
        }
        return view;
    }
}
