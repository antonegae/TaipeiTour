package com.rick.ricktour.repository

import com.rick.ricktour.api.TaipeiTourService
import com.rick.ricktour.api.TourModel
import retrofit2.Response

class MainActivityRepository(private val taipeiTourService: TaipeiTourService) {

    suspend fun callTaipeiService(language: String, page: Int?): Response<TourModel> {
        return taipeiTourService.getAttractions(language, page)
    }

}