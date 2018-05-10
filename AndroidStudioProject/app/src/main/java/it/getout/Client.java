package it.getout;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import it.getout.fragments.FragmentEmergenza;
import it.getout.gestioneconnessioni.Notifica;
import it.getout.gestioneposizione.GestoreEntita;
import it.getout.gestionevisualizzazionemappa.MappaFragment;



public class Client extends AppCompatActivity {

    private final int PERMESSO_LOCATION = 1;

    private CardView loading;
    private CardView loadingPhase2;
    private GestoreEntita gestore;
    private MappaFragment mappaFragment;

    private PendingIntent intentNotifica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, Notifica.class);
        intentNotifica = PendingIntent.getBroadcast(this, 0, intent, 0);



        loading = findViewById(R.id.cv_loading);
        loadingPhase2 = findViewById(R.id.cv_loading_phase2);

        gestore = new GestoreEntita(this);

        startLoading();

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMESSO_LOCATION);
        }
        else if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            gestore.coordinaPopolamentoDati();
        }
        else {
            gestore.coordinaPopolamentoDati();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.switch_button);
        item.setActionView(R.layout.switch_layout);
        return true;
    }

    public void inizializzaFragment() {
        new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Window window = getWindow();
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(ContextCompat.getColor(Client.this, R.color.colorPrimaryDarkEmergenza));

                        ActionBar bar = getSupportActionBar();
                        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryEmergenza)));
                        bar.setTitle("Modalità Emergenza");

                        mappaFragment = MappaFragment.newInstance();

                        FragmentEmergenza emergenza = FragmentEmergenza.newInstance();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, emergenza).commit();

                        stopLoading();
                    }
                });
            }
        }.start();

        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingPhase2.setVisibility(View.VISIBLE);
                    }
                });
                while(!gestore.isDownloadFinished()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingPhase2.setVisibility(View.GONE);
                    }
                });
            }
        }.start();
    }

    public MappaFragment getMappaFragment() { return mappaFragment; }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMESSO_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gestore.coordinaPopolamentoDati();
                }
                else {
                    Toast.makeText(Client.this, "Permessi negati. L'app ha bisogno del permesso, altrimenti morirai al prossimo incendio!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMESSO_LOCATION);
                }
                break;
            }
        }
    }

    public void startLoading() {
        new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    public void run() {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        loading.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();

    }

    public void stopLoading() {
        new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    public void run() {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        loading.setVisibility(View.GONE);
                    }
                });
            }
        }.start();
    }





}
