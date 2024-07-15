package com.rick.ricktour.utils


//Result 是一個帶有泛型參數 T 的 sealed class。它定義了三個不同的子類別,分別表示操作的不同結果狀態
sealed class Result<out T> {

    data class success<out T>(val data: T) : Result<T>()
    data class failure(val exception: Exception) : Result<Nothing>()
    object loading : Result<Nothing>()

}