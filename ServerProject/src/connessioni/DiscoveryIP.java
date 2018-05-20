package connessioni;

import entita.DAOUtente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscoveryIP implements Runnable{

    DatagramSocket socket;

    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            socket = new DatagramSocket(9600, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (true) {

                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                //Packet received
                //See if the packet holds the right command (message)
                String message = new String(packet.getData()).trim();
                if (message.equals("GETOUT")) {
                    byte[] sendData = "GETOUT_R".getBytes();
                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);

                    DAOUtente.insertUtente(sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DiscoveryIP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DiscoveryIP getInstance() {
        return DiscoveryIPHolder.INSTANCE;
    }

    private static class DiscoveryIPHolder {
        private static final DiscoveryIP INSTANCE = new DiscoveryIP();
    }
}
