package it.getout;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.getout.gestioneconnessioni.Connessioni;
import it.getout.gestioneconnessioni.DBHelper;
import it.getout.gestioneconnessioni.ServerHelper;
import it.getout.gestioneposizione.PosizioneUtente;
import it.getout.gestionevisualizzazionemappa.MappaFragment;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private final int PERMESSO_LOCATION = 1;

    MappaFragment mappaFragment;
    private CardView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = findViewById(R.id.cv_loading);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMESSO_LOCATION);
        }
        else {
            Connessioni.init(this);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMESSO_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Connessioni.init(this);
                }
                else {
                    Toast.makeText(MainActivity.this, "Permessi negati. L'app ha bisogno del permesso, altrimenti morirai al prossimo incendio!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMESSO_LOCATION);
                }
                break;
            }
        }
    }

    public MappaFragment getMappaFragment() { return mappaFragment; }




    public void startLoading() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loading.setVisibility(View.VISIBLE);
    }

    public void stopLoading() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loading.setVisibility(View.GONE);
    }
}
