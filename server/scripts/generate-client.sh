#!/bin/bash
set -e
CLIENT_NAME=${1:-client1}
SERVER_ENDPOINT=${2:-$(curl -s ifconfig.me 2>/dev/null):51820}
WG_DIR="/etc/wireguard"

if [ "$EUID" -ne 0 ]; then echo "Run as root"; exit 1; fi
if [ ! -f "$WG_DIR/wg0.conf" ]; then echo "wg0.conf not found. Run install-wireguard.sh first"; exit 1; fi

LAST_IP=$(grep -o "10.8.0.[0-9]*" $WG_DIR/wg0.conf | cut -d. -f4 | sort -n | tail -1)
NEXT_IP=$((LAST_IP + 1))
if [ -z "$LAST_IP" ]; then NEXT_IP=2; fi
if [ $NEXT_IP -gt 254 ]; then echo "IP pool full (10.8.0.2-254)"; exit 1; fi

CLIENT_PRIV=$(wg genkey)
CLIENT_PUB=$(echo $CLIENT_PRIV | wg pubkey)
SERVER_PUB=$(cat $WG_DIR/server_public.key)
PRESHARED=$(wg genpsk)

echo "[*] Adding peer $CLIENT_NAME -> 10.8.0.$NEXT_IP / fd86:ea04:1111::$NEXT_IP"

cat >> $WG_DIR/wg0.conf <<EOF

[Peer]
# $CLIENT_NAME
PublicKey = $CLIENT_PUB
PresharedKey = $PRESHARED
AllowedIPs = 10.8.0.$NEXT_IP/32, fd86:ea04:1111::$NEXT_IP/128
EOF

wg syncconf wg0 <(wg-quick strip wg0) || systemctl restart wg-quick@wg0

cat > $WG_DIR/${CLIENT_NAME}.conf <<EOF
[Interface]
PrivateKey = $CLIENT_PRIV
Address = 10.8.0.$NEXT_IP/32, fd86:ea04:1111::$NEXT_IP/128
DNS = 10.8.0.1, 1.1.1.1
MTU = 1280

[Peer]
PublicKey = $SERVER_PUB
PresharedKey = $PRESHARED
Endpoint = $SERVER_ENDPOINT
AllowedIPs = 0.0.0.0/0, ::/0
PersistentKeepalive = 25
EOF
chmod 600 $WG_DIR/${CLIENT_NAME}.conf
echo "[*] Created: $WG_DIR/${CLIENT_NAME}.conf"
cat $WG_DIR/${CLIENT_NAME}.conf
echo ""
if command -v qrencode &> /dev/null; then
  echo "[*] QR Code (scan with NORA TUNNEL):"
  qrencode -t ansiutf8 < $WG_DIR/${CLIENT_NAME}.conf
fi
