package practice.moviio.di.modules

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import practice.moviio.di.scope.SearchScope
import practice.moviio.ui.search.SearchActivity
import practice.moviio.ui.search.SearchAdapter

@Module
class SearchModule {

    @SearchScope
    @Provides
    fun provideClickListener(searchActivity: SearchActivity): SearchAdapter.OnMovieClicked = searchActivity

    @SearchScope
    @Provides
    fun provideAdapter(clickListener: SearchAdapter.OnMovieClicked, glide: RequestManager): SearchAdapter = SearchAdapter(clickListener, glide)

    @SearchScope
    @Provides
    fun provideGlide(searchActivity: SearchActivity): RequestManager = Glide.with(searchActivity)

}