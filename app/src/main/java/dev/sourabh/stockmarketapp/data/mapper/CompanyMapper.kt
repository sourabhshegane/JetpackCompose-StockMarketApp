package dev.sourabh.stockmarketapp.data.mapper

import dev.sourabh.stockmarketapp.data.local.CompanyListingEntity
import dev.sourabh.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = symbol
    )
}

