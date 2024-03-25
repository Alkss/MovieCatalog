package com.alkss.moviecatalog.di

import android.app.Application
import androidx.room.Room
import com.alkss.moviecatalog.core.data.data_source.MovieListDatabase
import com.alkss.moviecatalog.core.data.remote.MyAPI
import com.alkss.moviecatalog.core.data.repository.MovieListLocalRepository
import com.alkss.moviecatalog.core.data.repository.MovieListLocalRepositoryImpl
import com.alkss.moviecatalog.core.data.repository.MovieListRemoteRepository
import com.alkss.moviecatalog.core.data.repository.MovieListRemoteRepositoryImpl
import com.alkss.moviecatalog.feature_movielist.domain.use_cases.FetchMovieListRequest
import com.alkss.moviecatalog.feature_movielist.domain.use_cases.FetchMoviesDb
import com.alkss.moviecatalog.feature_movielist.domain.use_cases.HomeUseCases
import com.alkss.moviecatalog.feature_movielist.domain.use_cases.UpdateFavoriteState
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MyAPI.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                val newRequestBuilder = originalRequest.newBuilder()
                newRequestBuilder.header("Authorization", "Bearer " + MyAPI.API_READ_ACCESS_TOKEN)

                val newRequest = newRequestBuilder.build()
                chain.proceed(newRequest)
            }
            .addInterceptor(logging)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideDatabase(
        app: Application
    ): MovieListDatabase {
        return Room.databaseBuilder(
            app,
            MovieListDatabase::class.java,
            MovieListDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideLocalRepository(db: MovieListDatabase): MovieListLocalRepository {
        return MovieListLocalRepositoryImpl(
            movieDao = db.movieDao
        )
    }

    @Singleton
    @Provides
    fun provideRemoteRepository(
        retrofit: Retrofit
    ): MovieListRemoteRepository {
        return MovieListRemoteRepositoryImpl(
            retrofit = retrofit
        )
    }

    @Singleton
    @Provides
    fun provideHomeUseCases(
        localRepository: MovieListLocalRepository,
        remoteRepository: MovieListRemoteRepository
    ): HomeUseCases {
        return HomeUseCases(
            fetchMovieListRequest = FetchMovieListRequest(
                localRepository = localRepository,
                remoteRepository = remoteRepository
            ),
            fetchMoviesDb = FetchMoviesDb(
                localRepository = localRepository
            ),
            updateFavoriteState = UpdateFavoriteState(
                localRepository = localRepository
            )
        )
    }
}
