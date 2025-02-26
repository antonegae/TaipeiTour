package com.rick.ricktour.view.main.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rick.ricktour.api.TourItem
import com.rick.ricktour.api.TourModel
import com.rick.ricktour.model.Language
import com.rick.ricktour.repository.MainActivityRepository
import com.rick.ricktour.utils.TourItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


//@Inject 此處的 repository 依賴需要 Hilt 進行注入。Hilt 會在建構這個 ViewModel 的時候，自動提供一個 MainActivityRepository 的實例給它。
// 這裡注入語言列表
@HiltViewModel //表示該 ViewModel 要使用 Hilt 來注入其依賴。因為是viewModel生命週期有所不同，需要使用到HitViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MainActivityRepository,
    val languages: List<Language>,
    private val tourItemDao: TourItemDao,
) : ViewModel() {

    private val TAG = MainActivityViewModel::class.java.simpleName

    val allTourItems: LiveData<MutableList<TourItem>> = tourItemDao.getAll()



    /*
     * true 可以繼續往下讀取
     * false 阻擋
     * */
    private val _isRvLoading = MutableLiveData(true)  //init
    val isRvLoading: LiveData<Boolean> get() = _isRvLoading //getter
    fun setIsRvLoading(value: Boolean) {                    //setter
        _isRvLoading.value = value
    }

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading
    fun setLoading(value: Boolean) {
        _isLoading.value = value
    }

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> get() = _isError

    // 跟踪當前的頁碼
    private val _currentPage = MutableLiveData(1)

    val currentPage: MutableLiveData<Int> get() = _currentPage


    val totalDenominator = MutableLiveData("0")

    val taipeiTourData: MutableLiveData<TourModel?> = MutableLiveData()

    val changeLanguageStatus = MutableLiveData(false)


    private val _selectedTabIndex = MutableLiveData(0)  // 默認選中第一個tab
    val selectedTabIndex: LiveData<Int> get() = _selectedTabIndex
    fun switchTab(index: Int) {
        _selectedTabIndex.value = index
    }

    fun callApiTaipeiTour(language: Language?, currentPage: Int?) {

        viewModelScope.launch {
            try {

                val response = repository.callTaipeiService(language?.code.toString(), currentPage)
                if (response.isSuccessful) {
                    //UI refresh
                    taipeiTourData.postValue(response.body())
                    totalDenominator.postValue(response.body()?.total.toString())
                    _isLoading.postValue(false)

                } else {

                    _isError.postValue(true)

                }

            } catch (e: Exception) {

                Log.d(TAG, "rick error: ${e.localizedMessage}")
                _isError.postValue(true)

            }
        }
    }

    fun incrementPage() {

        _isRvLoading.value = false
        val current = _currentPage.value ?: 0
        currentPage.value = current + 1

    }

    fun getSelectedLanguage(): Language? {
        return languages.firstOrNull { it.isSelected }
    }

    //目前選中的語言
    private val _currentLanguage = MutableLiveData<Language>().apply {
        value = languages.first { it.isSelected }
    }

    val currentLanguage: MutableLiveData<Language> get() = _currentLanguage

    fun updateLanguage(language: Language) {

        _isLoading.value = true
        languages.forEach { it.isSelected = false } //選中條件全部改成false
        language.isSelected = true

        _currentPage.value = 1 // 每當語言更改時，將頁碼重置為1
        _currentLanguage.value = language //更改當前語言

    }

    //判斷中文 繁體 簡體字串中間 ，是否有 "-"
    fun getLocale(language: Language): Locale {

        //中文 繁體 簡體中間會有 "-" 判斷是否有 "-"
        val parts = language.code.split("-")
        return if (parts.size > 1) {
            Locale(parts[0], parts[1].toUpperCase())
        } else {
            Locale(parts[0])
        }
    }

}