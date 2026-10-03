package com.noratunnel.domain.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ServerProfile(
    val id: Long = 0,
    val name: String,
    val endpoint: String,
    val serverPublicKey: String,
    val encryptedPrivateKeyAlias: String,
    val clientAddress: String,
    val clientIpv6: String? = null,
    val dns: String = "10.8.0.1",
    val allowedIps: String = "0.0.0.0/0, ::/0",
    val persistentKeepalive: Int = 25,
    val mtu: Int = 1280
) : Parcelable
