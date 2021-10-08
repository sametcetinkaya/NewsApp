package com.sametcetinkaya.newsfeedapp.di

import android.content.Context
import androidx.room.Room
import com.sametcetinkaya.newsfeedapp.api.NewsApi
import com.sametcetinkaya.newsfeedapp.db.ArticleDao
import com.sametcetinkaya.newsfeedapp.db.ArticleDatabase
import com.sametcetinkaya.newsfeedapp.repository.Repository
import com.sametcetinkaya.newsfeedapp.utils.Constants
import com.sametcetinkaya.newsfeedapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    fun provideBaseurl() = Constants.BASE_URL


    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context,
        ArticleDatabase::class.java,"ArtcleTable.db"
    ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideDao(database: ArticleDatabase) = database.getDao()

    @Provides
    @Singleton
    fun provideNewsApi():NewsApi = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(NewsApi::class.java)

    @Provides
    @Singleton
    fun providesRepository(api: NewsApi, dao: ArticleDao) = Repository(api,dao)
}