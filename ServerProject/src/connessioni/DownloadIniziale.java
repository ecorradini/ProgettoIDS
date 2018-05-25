package connessioni;

import entita.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadIniziale implements Runnable{
    private boolean working = false;
    private DatagramSocket socket;

/*
    public static String getQueryFianle(){
        String cazzo;
        String query1 = DAOEdificio.downloadEdicifi();
        String query2 = DAOPiano.downloadPiani();
        String query3 = DAOAula.downloadAule();
        String query4 = DAOBeacon.downloadBeacons();
        String query5 = DAOMappa.downloadMappe();
        String query6 = DAOTronco.downloadTronchi();

        //parametri utente non serve da scaricare offline

        String cazzo = query1+query2+query3+query4+query5+query6;
    }
*/

    public void run(){
        try {
            String query1 = DAOEdificio.downloadEdicifi();
            String query2 = DAOPiano.downloadPiani();
            String query3 = DAOAula.downloadAule();
            String query4 = DAOBeacon.downloadBeacons();
            String query5 = DAOMappa.downloadMappe();
            String query6 = DAOTronco.downloadTronchi();

            //parametri utente non serve da scaricare offline

            String cazzo = query1+query2+query3+query4+query5+query6;

            //String cazzo = getQueryFianle();
            working = true;
            byte[] sendData;
            socket = new DatagramSocket();
            byte[] recvBuf = new byte[15000];
            DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);

            socket.receive(packet);
            String message = new String(packet.getData()).trim();

            if (message.equals("GETOUTEDO")) {
                sendData = cazzo.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), 9678);
                socket.send(sendPacket);
            }

            socket.close();
        }
        catch (IOException ex) {
            Logger.getLogger(DownloadIniziale.class.getName()).log(Level.SEVERE, null, ex);
        }

        working = false;


    }


    public static DownloadIniziale getInstance() {
        return DownloadInizialeHolder.INSTANCE;
    }

    private static class DownloadInizialeHolder {
        private static final DownloadIniziale INSTANCE = new DownloadIniziale();
    }



}

