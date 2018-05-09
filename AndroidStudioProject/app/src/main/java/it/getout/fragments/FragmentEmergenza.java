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

import it.getout.Client;
import it.getout.R;

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

        }
        return view;
    }
}
