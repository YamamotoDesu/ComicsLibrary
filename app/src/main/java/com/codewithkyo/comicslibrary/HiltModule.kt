package com.codewithkyo.comicslibrary

import com.codewithkyo.comicslibrary.model.api.ApiService
import com.codewithkyo.comicslibrary.model.api.MarvelApiRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)
}