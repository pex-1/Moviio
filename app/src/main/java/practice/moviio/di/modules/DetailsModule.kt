package practice.moviio.di.modules

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import practice.moviio.di.scope.DetailsScope
import practice.moviio.ui.details.DetailsActivity

@Module
class DetailsModule {

    @DetailsScope
    @Provides
    fun provideGlide(detailsActivity: DetailsActivity): RequestManager = Glide.with(detailsActivity)
}