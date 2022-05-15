package dev.sourabh.stockmarketapp.presentation.company_listings

import dev.sourabh.stockmarketapp.domain.model.CompanyListing

data class CompanyListingState (
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)