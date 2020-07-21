package practice.moviio.di.modules

import dagger.Module
import dagger.Provides
import practice.moviio.utils.Constants
import practice.moviio.utils.VerticalSpacingItemDecoration
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideVerticalSpacing(): VerticalSpacingItemDecoration = VerticalSpacingItemDecoration(Constants.VERTICAL_SPACING)

}