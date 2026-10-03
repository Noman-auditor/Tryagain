package com.noratunnel.di
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.noratunnel.data.db.AppDatabase
import com.noratunnel.data.datastore.SettingsDataStore
import com.noratunnel.security.KeystoreHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
private val Context.ds by preferencesDataStore("settings")
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext c: Context) = Room.databaseBuilder(c, AppDatabase::class.java, "nora.db").build()
    @Provides fun provideDao(db: AppDatabase) = db.serverDao()
    @Provides @Singleton fun provideKeystore(@ApplicationContext c: Context) = KeystoreHelper(c)
    @Provides @Singleton fun provideSettings(@ApplicationContext c: Context) = SettingsDataStore(c.ds)
}

