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

    public NotificaServer() {
        super();
    }

    @Override
    public void run() {
        try {

            working = true;



            socket = new DatagramSocket();
            ArrayList<String> ipList = DAOUtente.getAllUtenti();

            String edificio;

            count = 0;
            for(int i=0;i<ipList.size();i++) {
                String ip = ipList.get(i);
                System.out.println("INVIA NOTIFICA"+ip);
                //edificio
                edificio = "GETOUT EMERGENZA A: "+DAOParametri.selectEdificioParametro();

                byte[] sendData = edificio.getBytes();
                //Send a response

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), 9601);
                socket.send(sendPacket);
                if(sendPacket.getPort() == 9601){
                    System.out.println("CONNESSO");
                }
            }
            System.out.println("FINE INVIO NOTIFICA EMERGENZA");
            socket.close();
        }
        catch (IOException ex) {
            Logger.getLogger(NotificaServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        working = false;
    }

    public boolean isWorking() {
        return working;
    }
}
