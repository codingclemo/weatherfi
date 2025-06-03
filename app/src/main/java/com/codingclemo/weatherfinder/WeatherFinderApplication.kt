package com.codingclemo.weatherfinder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherFinderApplication : BaseApplication()

open class BaseApplication : Application()