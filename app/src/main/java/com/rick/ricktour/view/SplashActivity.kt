package com.rick.ricktour.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rick.ricktour.R
import com.rick.ricktour.api.TourModel
import com.rick.ricktour.databinding.ActivitySplashBinding
import com.rick.ricktour.utils.AppDatabase
import com.rick.ricktour.view.main.MainActivity
import com.rick.ricktour.view.main.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        binding = ActivitySplashBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
            splashViewModel = viewModel
        }
        initTourData()
        initObserverParam()
    }

    private fun initTourData() {
        viewModel.fetchAllPages("zh-tw")
    }

    //觀察apiResults的變化
    private fun initObserverParam() {

        viewModel.apiResults.observe(this) { responses ->

            val appDatabase = AppDatabase.getDatabase(this)

            lifecycleScope.launch(Dispatchers.IO) {
                responses.forEach { result ->
                    val tourModel = result.getOrNull()?.body()
                    val updatedTourModel = updateTourItemIds(tourModel)
                    updatedTourModel?.data?.let { tourItems ->
                        appDatabase.tourItemDao().insertAll(tourItems)
                    }
                }
            }

            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()

        }

        //database
        viewModel.allTourItems.observe(this) { model ->
            Log.d("TAG", "model size is --- ${model.size}")
        }

    }
    //將 tourItem 的 id 分配給其所有相關子項目的 tourItemId
    fun updateTourItemIds(tourModel: TourModel?): TourModel? {
        tourModel?.data?.forEach { tourItem ->

            val tourItemId = tourItem.id ?: return@forEach

            tourItem.category?.forEach { it?.tourItemId = tourItemId }
            tourItem.friendly?.forEach { it?.tourItemId = tourItemId }
            tourItem.images.forEach { it?.tourItemId = tourItemId }
            tourItem.links?.forEach { it?.tourItemId = tourItemId }
            tourItem.service?.forEach { it?.tourItemId = tourItemId }
            tourItem.target?.forEach { it?.tourItemId = tourItemId }
            tourItem.files?.forEach { it?.tourItemId = tourItemId }
        }

        return tourModel
    }

}