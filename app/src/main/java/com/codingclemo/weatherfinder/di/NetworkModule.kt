package com.codingclemo.weatherfinder.di

import android.content.Context
import com.codingclemo.weatherfinder.data.mapper.WeatherMapper
import com.codingclemo.weatherfinder.data.remote.WeatherApiService
import com.codingclemo.weatherfinder.BuildConfig
import com.codingclemo.weatherfinder.data.repository.LocationsRepository
import com.codingclemo.weatherfinder.data.repository.LocationsRepositoryImpl
import com.codingclemo.weatherfinder.data.repository.WeatherRepository
import com.codingclemo.weatherfinder.data.repository.WeatherRepositoryImpl
import com.codingclemo.weatherfinder.util.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApiKey(): String {
        return BuildConfig.OPENWEATHER_API_KEY
    }

    @Provides
    @Singleton
    fun provideWeatherMapper(): WeatherMapper {
        return WeatherMapper()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherApiService: WeatherApiService,
        apiKey: String,
        weatherMapper: WeatherMapper
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherApiService, apiKey, weatherMapper)
    }

    @IoDispatcher
    @Provides
    @Singleton
    fun provideIoDispatcher() = Dispatchers.IO
} 