package com.sametcetinkaya.news.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sametcetinkaya.news.R
import com.sametcetinkaya.news.db.ArticleDatabase
import com.sametcetinkaya.news.network.NewsApi
import com.sametcetinkaya.news.utils.Constants
import com.sametcetinkaya.news.utils.Constants.Companion.NEWS_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun articleDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app,
        ArticleDatabase::class.java,
        NEWS_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun articleDao(dataBase: ArticleDatabase) = dataBase.getArticleDao()


    @Singleton
    @Provides
    fun newsApi(): NewsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(NewsApi::class.java)
    }
    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )
}