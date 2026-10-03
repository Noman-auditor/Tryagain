#!/bin/bash
set -e
# NORA TUNNEL - Restrictive Firewall
# Documents every rule. Only 22/tcp and 51820/udp are allowed inbound.

if [ "$EUID" -ne 0 ]; then echo "Run as root"; exit 1; fi

echo "[*] Applying UFW rules..."
echo "  - Default incoming: DENY"
echo "  - Default outgoing: ALLOW (required for NAT)"
echo "  - Allow 22/tcp SSH (management)"
echo "  - Allow 51820/udp WireGuard"
echo "  - DENY all other ports"

ufw --force reset
ufw default deny incoming
ufw default allow outgoing
ufw allow 22/tcp comment \'SSH - Management only\'
ufw allow 51820/udp comment \'WireGuard VPN\'
# Do NOT expose 80,443 or backend ports unless you explicitly need them
ufw --force enable
ufw status numbered
ufw status verbose

echo ""
echo "[*] nftables equivalent (if using nft instead of UFW):"
echo "  nft add table inet filter"
echo "  nft add chain inet filter input { type filter hook input priority 0 \\; policy drop \\; }"
echo "  nft add rule inet filter input ct state established,related accept"
echo "  nft add rule inet filter input tcp dport 22 accept"
echo "  nft add rule inet filter input udp dport 51820 accept"
echo "  nft add rule inet filter input iif lo accept"
