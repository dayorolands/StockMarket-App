package com.plcoding.stockmarketapp.data.repository

import com.opencsv.bean.util.OpencsvUtils
import com.plcoding.stockmarketapp.BuildConfig.API_KEY
import com.plcoding.stockmarketapp.data.local.StockDatabase
import com.plcoding.stockmarketapp.data.mapper.toCompanyList
import com.plcoding.stockmarketapp.data.remote.StockApi
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import com.ramcosta.composedestinations.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    database: StockDatabase
) : StockRepository {
    private val dao = database.dao
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListing.map { mapper->
                    mapper.toCompanyList()
                }
            ))

            val isDbEmpty =localListing.isEmpty() && query.isBlank()
            val shouldLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListing = try {
                val response = api.getListings()
            }
            catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }
            catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }
}