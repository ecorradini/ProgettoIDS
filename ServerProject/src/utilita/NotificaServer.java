package utilita;

import connessioni.DiscoveryIP;
import entita.DAOBeacon;
import entita.DAOEdificio;
import entita.DAOParametri;
import entita.DAOUtente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static entita.DAOParametri.selectEdificioParametro;

public class NotificaServer extends Thread {

    private DatagramSocket socket;
    private int count;
    private boolean working = false;
    private ArrayList<String> giaInviata;
    private boolean verifica_emergenza = false;

    public NotificaServer(boolean emergenza) {
        super();
        verifica_emergenza = emergenza;
        giaInviata = new ArrayList<>();
    }

    @Override
    public void run() {
        try {

            working = true;
            byte[] sendData;
            String appoggio = "";
            String fineEmergenza = "Fine Emergenza";
            socket = new DatagramSocket();
            ArrayList<String> ipList = DAOUtente.getAllUtenti();
            count = 0;
            for(int i=0;i<ipList.size();i++) {
                if(!giaInviata.contains(ipList.get(i))) {
                    String ip = ipList.get(i);

                    if (verifica_emergenza){
                        appoggio = "Emergenza: "+DAOParametri.selectEdificioParametro().toUpperCase()+"!";
                        sendData = appoggio.getBytes();

                    }
                    else {
                        appoggio = fineEmergenza;
                        sendData = appoggio.getBytes();
                    }
                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), 9601);
                    socket.send(sendPacket);
                    giaInviata.add(ip);
                }
            }
            socket.close();
        }
        catch (IOException ex) {
            Logger.getLogger(NotificaServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        working = false;
        verifica_emergenza = false;

    }
}
