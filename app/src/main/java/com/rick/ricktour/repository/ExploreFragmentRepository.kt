package com.rick.ricktour.repository

import com.rick.ricktour.api.TaipeiTourService
import com.rick.ricktour.api.TourModel
import retrofit2.Response


class ExploreFragmentRepository(private val taipeiTourService: TaipeiTourService) {

    suspend fun callTaipeiService(language: String, page: Int?, nlat: Double?, elong: Double?): Response<TourModel> {
        return taipeiTourService.getAttractionsNearBy(language, page, nlat, elong)
    }

}