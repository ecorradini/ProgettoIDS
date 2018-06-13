package connessioni;

import entita.DAOUtente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe invio broadcast indirizzo IP
 */
public class DiscoveryIP implements Runnable{

    //Socket
    DatagramSocket socket;

    /**
     * Metodo run del Runnable
     */
    @Override
    public void run() {
        try {
            //Mantieni il socket aperto all'ascolto di tutto il traffico UDP destinato alla porta 9605
            socket = new DatagramSocket(9605, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (true) {
                //Ricevi un pacchetto
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                //Pacchetto ricevuto
                //Controllo se il pacchetto
                String message = new String(packet.getData()).trim();
                if (message.equals("GETOUT")) {
                    byte[] sendData = "GETOUT_R".getBytes();
                    //Invia un pacchetto in risposta
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    try {
                        Process sendOutput = Runtime.getRuntime().exec("iptables -A OUTPUT -p udp --dport "+ packet.getPort() + " -j ACCEPT");
                        sendOutput.waitFor();
                    } catch(InterruptedException e) {
                    }
                    System.out.println(packet.getPort());
                    socket.send(sendPacket);
                    System.out.println(sendPacket.getAddress()+" "+sendPacket.getPort());

                    //Inserisco l'IP del mittente nella tabella degli utenti
                    DAOUtente.insertUtente(sendPacket.getAddress().getHostAddress());

                    //Aggiungo l'eccezione ad iptables per il client appena registratosi
                    try {
                        Process sendOutput = Runtime.getRuntime().exec("iptables -D OUTPUT -p udp --dport "+ packet.getPort() + " -j ACCEPT");
                        sendOutput.waitFor();
                        Process ipTablesInput = Runtime.getRuntime().exec("iptables -A INPUT -s " + sendPacket.getAddress().getHostAddress() + " -j ACCEPT");
                        ipTablesInput.waitFor();
                        Process ipTablesOutput = Runtime.getRuntime().exec("iptables -A OUTPUT -s " + sendPacket.getAddress().getHostAddress() + " -j ACCEPT");
                        ipTablesOutput.waitFor();
                    } catch(InterruptedException e) {
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DiscoveryIP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Restituisco l'istanza
     * @return Istanza della classe
     */
    public static DiscoveryIP getInstance() {
        return DiscoveryIPHolder.INSTANCE;
    }

    /**
     * Instanzio la classe. Final, deve essere sempre in esecuzione.
     */
    private static class DiscoveryIPHolder {
        private static final DiscoveryIP INSTANCE = new DiscoveryIP();
    }
}
