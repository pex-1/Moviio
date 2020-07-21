package practice.moviio.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import practice.moviio.di.modules.DetailsModule
import practice.moviio.di.modules.FavoritesModule
import practice.moviio.di.modules.SearchModule
import practice.moviio.di.scope.DetailsScope
import practice.moviio.di.scope.FavoritesScope
import practice.moviio.di.scope.SearchScope
import practice.moviio.ui.details.DetailsActivity
import practice.moviio.ui.favorites.FavoritesActivity
import practice.moviio.ui.search.SearchActivity

@Module
abstract class ActivityBuildersModule {

    @SearchScope
    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun contributeSearchActivity(): SearchActivity

    @FavoritesScope
    @ContributesAndroidInjector(modules = [FavoritesModule::class])
    abstract fun contributeFavoritesActivity(): FavoritesActivity

    @DetailsScope
    @ContributesAndroidInjector(modules = [DetailsModule::class])
    abstract fun contributeDetailsActivity(): DetailsActivity

}