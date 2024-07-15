package com.rick.ricktour.view.main.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rick.ricktour.model.NewsModel
import com.rick.ricktour.repository.NewsFragmentRepository
import com.rick.ricktour.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsFragmentRepository) : ViewModel() {


    val taipeiNews: MutableLiveData<Result<NewsModel?>> = MutableLiveData()


    fun fetchNews(language: String?, page: Int) {
        viewModelScope.launch {

            taipeiNews.value = Result.loading
            taipeiNews.value = try {

                repository.getNews(language, page)

            } catch (e: Exception) {

                Result.failure(e)

            }

        }
    }

}