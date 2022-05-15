package dev.sourabh.stockmarketapp.domain.repository

import dev.sourabh.stockmarketapp.domain.model.CompanyListing
import dev.sourabh.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}