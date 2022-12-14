package com.plcoding.stockmarketapp.presentation.company_listings

sealed class CompanyListingsEvents{
    object Refresh: CompanyListingsEvents()
    data class OnSearchQueryChange(val searchQuery: String): CompanyListingsEvents()
}
