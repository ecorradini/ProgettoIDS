package connessioni;

import entita.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe per il download di tutti i dati iniziali
 */
public class DownloadIniziale {
    /**
     * Risultato delle query
     */
        private String risultato;

    /**
     * Costruttore
     */
    public DownloadIniziale(){
            String query1 = DAOEdificio.downloadEdicifi();
            String query2 = DAOPiano.downloadPiani();
            String query3 = DAOAula.downloadAule();
            String query4 = DAOBeacon.downloadBeacons();
            String query5 = DAOTronco.downloadTronchi();
            String query6 = DAOMappa.getBase64Mappe();
            risultato = "{" + query1 + "," + query2 + "," + query3 + "," + query4 + "," + query5 + "," + query6 + "}";
        }

    /**
     * Metodo per la restituzione del risultato
     * @return
     */
    public String getRisultato(){
            return risultato;
        }




}

