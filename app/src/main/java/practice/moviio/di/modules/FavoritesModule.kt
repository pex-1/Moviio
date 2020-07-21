package practice.moviio.di.modules

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import practice.moviio.di.scope.FavoritesScope
import practice.moviio.ui.favorites.FavoritesActivity
import practice.moviio.ui.favorites.FavoritesAdapter

@Module
class FavoritesModule {

    @FavoritesScope
    @Provides
    fun provideClickListener(favoritesActivity: FavoritesActivity): FavoritesAdapter.OnMovieClicked = favoritesActivity

    @FavoritesScope
    @Provides
    fun provideAdapter(clickListener: FavoritesAdapter.OnMovieClicked, glide: RequestManager): FavoritesAdapter = FavoritesAdapter(clickListener, glide)

    @FavoritesScope
    @Provides
    fun provideGlide(favoritesActivity: FavoritesActivity): RequestManager = Glide.with(favoritesActivity)
}