#!/bin/bash
set -e
DATE=$(date +%F_%H%M%S)
BACKUP_DIR="/root"
BACKUP="$BACKUP_DIR/nora-backup-$DATE.tar.gz"
if [ "$EUID" -ne 0 ]; then echo "Run as root"; exit 1; fi
tar -czf "$BACKUP" /etc/wireguard/ --exclude=\'*.log\' 2>/dev/null
chmod 600 "$BACKUP"
echo "[*] Backup created: $BACKUP"
ls -lh "$BACKUP"
echo "[*] Restore: tar -xzf $BACKUP -C /"
