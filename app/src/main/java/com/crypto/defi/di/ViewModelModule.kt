package com.crypto.defi.di

import android.app.Application
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.drm.HttpMediaDrmCallback
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
  @Provides
  @ViewModelScoped
  fun providePlayer(app: Application): ExoPlayer {
    return ExoPlayer.Builder(app).build()
  }
}