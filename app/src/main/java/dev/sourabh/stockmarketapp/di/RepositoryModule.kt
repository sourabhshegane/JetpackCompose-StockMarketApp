package dev.sourabh.stockmarketapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sourabh.stockmarketapp.data.csv.CSVParser
import dev.sourabh.stockmarketapp.data.csv.CompanyListingParser
import dev.sourabh.stockmarketapp.data.repository.StockRepositoryImpl
import dev.sourabh.stockmarketapp.domain.model.CompanyListing
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepositoryImpl
}