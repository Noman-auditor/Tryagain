#!/bin/bash
set -e
PUBKEY=$1
if [ -z "$PUBKEY" ]; then echo "Usage: $0 <ClientPublicKey>"; echo "Find key: wg show | grep -A2 peer"; exit 1; fi
if [ "$EUID" -ne 0 ]; then echo "Run as root"; exit 1; fi

echo "[*] Revoking $PUBKEY"
wg set wg0 peer "$PUBKEY" remove || echo "[!] Peer not active"

# Remove from wg0.conf - keeps file consistent
cp /etc/wireguard/wg0.conf /etc/wireguard/wg0.conf.bak
awk -v key="$PUBKEY" \'
  $0 ~ key {skip=1; next}
  skip && /^\[Peer\]/ {skip=0}
  skip && /^PublicKey/ {next}
  !skip {print}
\' /etc/wireguard/wg0.conf.bak > /tmp/wg0.tmp && mv /tmp/wg0.tmp /etc/wireguard/wg0.conf
chmod 600 /etc/wireguard/wg0.conf
echo "[*] Removed from wg0.conf (backup: wg0.conf.bak)"
echo "[*] Restart: systemctl restart wg-quick@wg0"
