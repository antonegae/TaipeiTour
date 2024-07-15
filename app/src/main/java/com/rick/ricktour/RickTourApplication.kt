package com.rick.ricktour

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //表示整個應用程式都要使用 Hilt 進行注入。
class RickTourApplication:Application() {



}