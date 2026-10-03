#!/bin/bash
set -e
# NORA TUNNEL - Production WireGuard Installer
# Tested on Ubuntu 22.04 / Debian 12

if [ "$EUID" -ne 0 ]; then echo "Run as root: sudo bash install-wireguard.sh"; exit 1; fi

WAN_INTERFACE=$(ip route get 1.1.1.1 2>/dev/null | awk \'{print $5; exit}\')
if [ -z "$WAN_INTERFACE" ]; then WAN_INTERFACE="eth0"; echo "[!] Could not detect WAN, fallback to eth0"; fi
echo "[*] Detected WAN_INTERFACE: $WAN_INTERFACE"

echo "[*] Installing dependencies..."
apt update && apt install -y wireguard iptables iproute2 ufw qrencode curl

echo "[*] Enabling IP Forwarding..."
sysctl -w net.ipv4.ip_forward=1
sysctl -w net.ipv6.conf.all.forwarding=1
grep -q "net.ipv4.ip_forward=1" /etc/sysctl.conf || echo "net.ipv4.ip_forward=1" >> /etc/sysctl.conf
grep -q "net.ipv6.conf.all.forwarding=1" /etc/sysctl.conf || echo "net.ipv6.conf.all.forwarding=1" >> /etc/sysctl.conf

WG_DIR="/etc/wireguard"
mkdir -p $WG_DIR && chmod 700 $WG_DIR

if [ ! -f "$WG_DIR/server_private.key" ]; then
    echo "[*] Generating server keys..."
    wg genkey | tee $WG_DIR/server_private.key | wg pubkey > $WG_DIR/server_public.key
    chmod 600 $WG_DIR/server_private.key
fi

SERVER_PRIV=$(cat $WG_DIR/server_private.key)
SERVER_PUB=$(cat $WG_DIR/server_public.key)

echo "[*] Creating wg0.conf with NAT for $WAN_INTERFACE..."
cat > $WG_DIR/wg0.conf <<EOF
[Interface]
PrivateKey = $SERVER_PRIV
Address = 10.8.0.1/24, fd86:ea04:1111::1/64
ListenPort = 51820
SaveConfig = false
PostUp = iptables -A FORWARD -i %i -j ACCEPT; iptables -t nat -A POSTROUTING -o $WAN_INTERFACE -j MASQUERADE; ip6tables -A FORWARD -i %i -j ACCEPT; ip6tables -t nat -A POSTROUTING -o $WAN_INTERFACE -j MASQUERADE
PostDown = iptables -D FORWARD -i %i -j ACCEPT; iptables -t nat -D POSTROUTING -o $WAN_INTERFACE -j MASQUERADE; ip6tables -D FORWARD -i %i -j ACCEPT; ip6tables -t nat -D POSTROUTING -o $WAN_INTERFACE -j MASQUERADE
EOF
chmod 600 $WG_DIR/wg0.conf

echo "[*] Configuring UFW..."
ufw --force reset
ufw default deny incoming
ufw default allow outgoing
ufw allow 22/tcp comment \'SSH\'
ufw allow 51820/udp comment \'WireGuard\'
ufw --force enable

systemctl enable wg-quick@wg0
systemctl restart wg-quick@wg0 || wg-quick up wg0

echo "[*] DONE. Server PublicKey: $SERVER_PUB"
echo "[*] Next: bash generate-client.sh singapore \$(curl -s ifconfig.me):51820"
wg show
