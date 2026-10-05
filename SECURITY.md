# Security Policy - Nora Tunnel

- **Standard Cryptography:** Relies on battle-tested core implementations (WireGuard kernel/userspace libraries) without custom cryptographic primitives.
- **Rigorous Validation:** All imported configuration links (VLESS, VMess, WireGuard, JSON) are strictly validated before profile creation.
- **Log Redaction:** Sensitive parameters such as passwords, private keys, tokens, and pre-shared keys are automatically redacted before logs are displayed or exported.
