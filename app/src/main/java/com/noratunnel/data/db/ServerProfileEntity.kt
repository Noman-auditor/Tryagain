package com.noratunnel.data.db
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "servers")
data class ServerProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val endpoint: String,
    val serverPublicKey: String,
    val encryptedPrivateKeyAlias: String,
    val clientAddress: String,
    val clientIpv6: String?,
    val dns: String,
    val allowedIps: String,
    val keepalive: Int,
    val mtu: Int
)
