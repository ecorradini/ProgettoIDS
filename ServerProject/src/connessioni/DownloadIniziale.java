package connessioni;

import entita.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadIniziale {
        private String risultato;

        public DownloadIniziale(){
            String query1 = DAOEdificio.downloadEdicifi();
            String query2 = DAOPiano.downloadPiani();
            String query3 = DAOAula.downloadAule();
            String query4 = DAOBeacon.downloadBeacons();
            String query5 = DAOMappa.downloadMappe();
            String query6 = DAOTronco.downloadTronchi();
            risultato = "{" + query1 + "," + query2 + "," + query3 + "," + query4 + "," + query5 + "," + query6 + "}";
        }

        public String getRisultato(){
            return risultato;
        }




}

