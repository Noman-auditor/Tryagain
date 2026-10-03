#!/bin/bash
echo "===== NORA TUNNEL SERVER STATUS ====="
echo "Date: $(date -u)"
echo ""
echo "[WireGuard]"
wg show 2>&1 || echo "WireGuard not running"
echo ""
echo "[Latest Handshakes]"
wg show all latest-handshakes 2>&1
echo ""
echo "[Transfer RX/TX]"
wg show all transfer 2>&1
echo ""
echo "[System]"
uptime
echo ""
free -h
echo ""
df -h /
echo ""
echo "[Listening Port]"
ss -ulnp | grep 51820 || echo "51820 not listening"
echo ""
echo "[Firewall]"
ufw status verbose 2>&1 || nft list ruleset 2>&1 | head -n 50
