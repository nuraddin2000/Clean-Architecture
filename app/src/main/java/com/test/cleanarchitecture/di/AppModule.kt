package com.test.cleanarchitecture.di

import android.content.Context
import androidx.room.Room
import com.test.cleanarchitecture.common.Constants
import com.test.cleanarchitecture.data.remote.ContactsService
import com.test.cleanarchitecture.data.repository.ContactsDaoRepositoryImpl
import com.test.cleanarchitecture.data.repository.ContactsRepositoryImpl
import com.test.cleanarchitecture.db.AppDatabase
import com.test.cleanarchitecture.db.contacts.ContactsDao
import com.test.cleanarchitecture.domain.repository.ContactsDaoRepository
import com.test.cleanarchitecture.domain.repository.ContactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient  {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideContactsApi(okHttpClient: OkHttpClient): ContactsService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContactsService::class.java)
    }

    @Provides
    @Singleton
    fun provideContactsRepository(api: ContactsService): ContactsRepository {
        return ContactsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun injectRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,AppDatabase::class.java,"AppDB").build()

    @Singleton
    @Provides
    fun injectDao(
        database: AppDatabase
    ) = database.userDao()


    @Singleton
    @Provides
    fun injectContactsDaoRepo(dao : ContactsDao) : ContactsDaoRepository {
        return ContactsDaoRepositoryImpl(dao)
    }
}