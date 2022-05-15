package dev.sourabh.stockmarketapp.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sourabh.stockmarketapp.domain.repository.StockRepository
import dev.sourabh.stockmarketapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel  @Inject constructor(
    private val stockRepository: StockRepository
): ViewModel(){

    var state by mutableStateOf(CompanyListingState())
    private val searchJob: Job? = null

    fun onEvent(event: CompanyListingEvent){
        when(event){
            is CompanyListingEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                viewModelScope.launch {
                    delay(500)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote:Boolean = false
    ){
        viewModelScope.launch {
            stockRepository.getCompanyListings(fetchFromRemote, query).collect{result ->
                when(result){
                    is Resource.Success -> {
                        result.data?.let { listings ->
                            state = state.copy(
                                companies = listings
                            )
                        }
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {
                        state = state.copy(isLoading = false)
                    }
                }
            }
        }
    }
}