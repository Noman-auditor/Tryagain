package com.nora.tunnel.data.database

import androidx.room.*
import com.nora.tunnel.core.model.Core
import com.nora.tunnel.core.model.Protocol
import com.nora.tunnel.core.model.TunnelProfile
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Database(entities = [TunnelProfile::class, ConnectionSession::class], version = 1, exportSchema = false)
abstract class NoraDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun sessionDao(): SessionDao
}

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<TunnelProfile>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(profile: TunnelProfile)
    @Delete suspend fun delete(profile: TunnelProfile)
    @Query("SELECT * FROM profiles WHERE id = :id") suspend fun getById(id: String): TunnelProfile?
}

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(session: ConnectionSession)
}

@Entity(tableName = "connection_history")
data class ConnectionSession(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val profileId: String,
    val profileName: String,
    val protocol: String,
    val core: String,
    val startTime: Long,
    val endTime: Long? = null,
    val durationSec: Long = 0,
    val rxBytes: Long = 0,
    val txBytes: Long = 0,
    val result: String,
    val disconnectReason: String? = null
)
