package dev.sourabh.stockmarketapp.presentation.company_listings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sourabh.stockmarketapp.domain.repository.StockRepository
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel  @Inject constructor(
    private val stockRepository: StockRepository
): ViewModel(){

}