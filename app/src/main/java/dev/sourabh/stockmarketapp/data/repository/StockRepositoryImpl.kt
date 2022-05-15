package dev.sourabh.stockmarketapp.data.repository

import dev.sourabh.stockmarketapp.data.csv.CSVParser
import dev.sourabh.stockmarketapp.data.local.StockDatabase
import dev.sourabh.stockmarketapp.data.mapper.toCompanyListing
import dev.sourabh.stockmarketapp.data.mapper.toCompanyListingEntity
import dev.sourabh.stockmarketapp.data.remote.StockAPI
import dev.sourabh.stockmarketapp.domain.model.CompanyListing
import dev.sourabh.stockmarketapp.domain.repository.StockRepository
import dev.sourabh.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockAPI,
    private val db: StockDatabase,
    private val companyListingCSVParser: CSVParser<CompanyListing>
): StockRepository{

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query = query)
            emit(Resource.Success(data = localListings.map {
                it.toCompanyListing()
            }))

            val isDBEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDBEmpty && !fetchFromRemote

            if(shouldJustLoadFromCache){
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteListings = try {
                val responseFromAPI = api.getAllListings()
                responseFromAPI.byteStream()
                companyListingCSVParser.parse(responseFromAPI.byteStream())
            }catch (ioException: IOException){
                ioException.printStackTrace()
                emit(Resource.Error(message = "Could not load data. Please try again"))
                null
            }catch (httpException: HttpException){
                httpException.printStackTrace()
                emit(Resource.Error(message = "Could not load data. Please try again"))
                null
            }

            remoteListings?.let {listings ->
                dao.clearCompanyListing()
                dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
                emit(Resource.Success(listings))
                emit(Resource.Loading(false))
            }
        }
    }
}