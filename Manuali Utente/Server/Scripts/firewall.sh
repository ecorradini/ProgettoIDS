# !/bin/bash

# È NECESSARIO ESEGUIRE QUESTO SCRIPT COME ROOT ( $ sudo <nome_script>) 


echo "[*] Avvio configurazione Firewall..."

#	iptables -F
#	iptables -X	


iptables --policy INPUT DROP
iptables --policy OUTPUT DROP
iptables --policy FORWARD DROP

#	Enable traffic through port 9605 used for DISCOVERY IP

echo "[*] Apertura porta 9605 per funzionalità di DiscoveryIP..."

iptables -A INPUT -p tcp -s 127.0.0.1 -j ACCEPT
iptables -A OUTPUT -p tcp -s 127.0.0.1 -j ACCEPT
iptables -A FORWARD -p tcp -s 127.0.0.1 -j ACCEPT
iptables -A INPUT -p udp --dport 9605 -j ACCEPT
iptables -A OUTPUT -p udp --dport 9605 -j ACCEPT
iptables -A FORWARD -p udp --dport 9605 -j ACCEPT
iptables -A INPUT -p tcp --dport 9605 -j ACCEPT
iptables -A OUTPUT -p tcp --dport 9605 -j ACCEPT
iptables -A FORWARD -p tcp --dport 9605 -j ACCEPT
