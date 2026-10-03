# Security Policy

## Secure Storage
- PrivateKeys encrypted via Android Keystore + EncryptedSharedPreferences (AES256-GCM)
- Never stored in Git, logs, analytics, crash reports, plain SharedPreferences
- No hardcoded keys/passwords/tokens

## Firewall
- Default deny incoming, allow outgoing
- Allow 22/tcp SSH only, 51820/udp WireGuard only
- Documented in `server/firewall/setup-firewall.sh` and `server/scripts/install-wireguard.sh`
- Use `ufw status verbose` to verify

## Reporting
Do not log PrivateKey. Report vulnerabilities to security@noratunnel.local
