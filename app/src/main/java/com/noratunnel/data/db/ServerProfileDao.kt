package com.noratunnel.data.db
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface ServerProfileDao {
    @Query("SELECT * FROM servers") fun observeAll(): Flow<List<ServerProfileEntity>>
    @Query("SELECT * FROM servers WHERE id=:id") suspend fun getById(id: Long): ServerProfileEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(e: ServerProfileEntity): Long
    @Delete suspend fun delete(e: ServerProfileEntity)
    @Query("SELECT * FROM servers LIMIT 1") suspend fun getFirst(): ServerProfileEntity?
}
