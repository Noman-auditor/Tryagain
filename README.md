# NORA TUNNEL - Secure. Private. Connected.

Production-quality self-hosted WireGuard VPN. Real `android.net.VpnService` client for your own authorized VPS.

**Architecture:** Android Phone -> WireGuard Tunnel -> Your VPS -> NAT/Firewall -> Internet

## Build (Codespaces)
## Server Setup (Ubuntu/Debian VPS)
## WireGuard Library
`com.wireguard.android:tunnel:1.0.20231018` - WireGuard Go userspace, GPLv2. No custom crypto.

## Security Testing Checklist
1. Connect VPN 2. Verify IP = VPS IP (ip.me) 3. Verify DNS (dnsleaktest.com) 4. Verify IPv4 0.0.0.0/0 via VPN 5. Check IPv6 warning if not configured 6. Disable VPN 7. Test Kill Switch (Always-on + Block without VPN) 8. Switch WiFi<->Mobile -> reconnect 9. `wg show` handshake <2min 10. `ufw status` only 22,51820 11. Logcat has no PrivateKey 12. Only test on your own servers

## Important Limitations
Self-hosted hides ISP IP behind VPS IP but does NOT guarantee anonymity. Datacenter IPs are detectable, accounts/cookies still identify you. No anti-VPN bypass.
