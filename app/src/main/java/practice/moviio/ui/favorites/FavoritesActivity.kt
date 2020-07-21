package practice.moviio.ui.favorites

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favorites.*
import practice.moviio.R
import practice.moviio.data.response.details.MovieDetails
import practice.moviio.di.viewmodel.ViewModelProviderFactory
import practice.moviio.ui.details.DetailsActivity
import practice.moviio.ui.search.SearchActivity
import practice.moviio.utils.*
import javax.inject.Inject


class FavoritesActivity : DaggerAppCompatActivity(), FavoritesAdapter.OnMovieClicked {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: FavoritesViewModel

    @Inject
    lateinit var itemDecoration: VerticalSpacingItemDecoration

    @Inject
    lateinit var favoritesAdapter: FavoritesAdapter

    private var movies: ArrayList<MovieDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        viewModel = ViewModelProvider(this, providerFactory).get(FavoritesViewModel::class.java)

        subscribeObservers()
        initRecyclerView()

        setSearchView()

    }

    private fun setSearchView() {
        favoritesSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                favoritesSearchView.setQuery("", false)
                favoritesSearchView.clearFocus()
                if (query != null && query.trim().length > 2) {
                    startActivity(SearchActivity.newInstance(this@FavoritesActivity, query))
                } else {
                    favoritesLayout.snackbar("Query should be at least 3 letters long!")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initRecyclerView() {
        favoritesRecyclerView.apply {
            addItemDecoration(itemDecoration)
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter

            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
            scheduleLayoutAnimation()
        }
    }

    private fun subscribeObservers() {
        favoritesProgressBar.visible()
        viewModel.favorites.observe(this, Observer {
            favoritesProgressBar.gone()
            if (it != null) {
                movies = it
                favoritesRecyclerView.scheduleLayoutAnimation()
                favoritesAdapter.setData(it)
                favoritesAdapter.notifyDataSetChanged()
                Favorites.setFavorites(it)
            }
            if(movies.size<1) favoritesEmptyStateLayout.visible()
            else favoritesEmptyStateLayout.gone()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    override fun onClick(movie: MovieDetails) {
        startActivity(DetailsActivity.newInstanceFavorites(this, movie))
    }

    override fun onBookmarkClick(position: Int, movie: MovieDetails) {
        viewModel.deleteMovie(movie)
        Favorites.removeFavorite(movie.imdbID)
        movies.removeAt(position)

        if(movies.size == 0) favoritesEmptyStateLayout.visible()
        favoritesAdapter.setData(movies)
        favoritesAdapter.notifyDataSetChanged()

        //favoritesAdapter.notifyItemRemoved(position)
    }


}
