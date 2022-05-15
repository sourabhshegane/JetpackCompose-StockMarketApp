package dev.sourabh.stockmarketapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sourabh.stockmarketapp.data.local.StockDatabase
import dev.sourabh.stockmarketapp.data.remote.StockAPI
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesStockAPI(): StockAPI {
        return Retrofit.Builder().baseUrl(StockAPI.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create()).build().create()
    }

    @Singleton
    @Provides
    fun providesStockDB(app: Application): StockDatabase {
        return Room.databaseBuilder(app, StockDatabase::class.java, "stocks.db").build()
    }
}