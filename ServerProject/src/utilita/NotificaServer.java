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

public class NotificaServer implements Runnable{

    private static DatagramSocket socket;
    private static int count;
    private static boolean working = false;

    @Override
    public void run() {
        try {

            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            socket = new DatagramSocket(9600, InetAddress.getByName("0.0.0.0"));
            ArrayList<String> ipList = DAOUtente.getAllUtenti();

            String edificio;

            count = 0;
            for(int i=0;i<ipList.size();i++) {
                String ip = ipList.get(i);

                //edificio
                edificio = "GETOUT EMERGENZA A: "+DAOParametri.selectEdificioParametro();

                byte[] sendData = edificio.getBytes();
                //Send a response
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), socket.getPort());
                socket.send(sendPacket);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(NotificaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        working = false;
    }

    public static NotificaServer getInstance() {
        working = true;
        return NotificaServer.NotificaServerHolder.INSTANCE;
    }

    private static class NotificaServerHolder {
        private static final NotificaServer INSTANCE = new NotificaServer();
    }

    public static boolean isWorking() {
        return working;
    }
}
