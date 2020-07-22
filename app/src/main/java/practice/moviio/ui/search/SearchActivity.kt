package practice.moviio.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.view_search.*
import practice.moviio.R
import practice.moviio.data.response.movies.Movie
import practice.moviio.di.viewmodel.ViewModelProviderFactory
import practice.moviio.ui.details.DetailsActivity
import practice.moviio.utils.*
import java.util.*
import javax.inject.Inject

class SearchActivity : DaggerAppCompatActivity(), SearchAdapter.OnMovieClicked {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var itemDecoration: VerticalSpacingItemDecoration

    @Inject
    lateinit var searchAdapter: SearchAdapter

    private var sortedList: List<Movie> = listOf()

    private var position: Int = -1

    companion object {
        private const val SEARCH = "SEARCH"
        fun newInstance(context: Context, query: String): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(SEARCH, query)
            return intent
        }
    }

    override fun onResume() {
        super.onResume()
        if (position > 0) {
            searchAdapter.notifyItemChanged(position)
            position = -1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Log.e("search", "started!")

        viewModel = ViewModelProvider(this, providerFactory).get(SearchViewModel::class.java)

        viewModel.getMovies(intent.getStringExtra(SEARCH)!!)
        subscribeObservers()
        initRecyclerView()
        setToolbar()
        setSearchView()

    }

    fun searchMovies() {
        val query = search_input_text.text.toString()
        if (query.length < 3) {
            this.toast("Query should be at least 3 letters long!")
        } else {
            applicationContext.hideKeyboard(searchLayout)
            viewModel.getMovies(query)
            search_open_view.visibility = View.INVISIBLE
            search_input_text.setText("")
        }
    }


    private fun setSearchView() {
        search_input_text.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchMovies()
                    return true
                }
                return false
            }
        })
        execute_search_button.setOnClickListener {
            searchMovies()
        }
    }


    private fun subscribeObservers() {

        viewModel.movies.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    searchProgressBar.gone()
                    response.data?.search?.let {
                        searchRecyclerView.scheduleLayoutAnimation()
                        sortedList = it
                        Collections.sort(sortedList) { c1, c2 -> c1.year.compareTo(c2.year) }
                        searchAdapter.setData(sortedList)
                        searchAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    searchProgressBar.gone()
                    response.message?.let { message ->
                        searchLayout.snackbar("An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    searchProgressBar.visible()
                }
            }


        })
        viewModel.details.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    searchProgressBar.gone()
                    response.data?.let {
                        viewModel.insertMovie(it)
                        searchLayout.snackbar("Movie stored!")
                    }
                }
                is Resource.Error -> {
                    searchProgressBar.gone()
                    response.message?.let { message ->
                        searchLayout.snackbar("An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    searchProgressBar.visible()
                }
            }
        })
    }


    private fun initRecyclerView() {
        searchRecyclerView.apply {
            addItemDecoration(itemDecoration)
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter

            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
            scheduleLayoutAnimation()
        }
    }

    override fun onClick(movie: Movie, position: Int) {
        this.position = position
        startActivity(DetailsActivity.newInstance(this, movie.imdbID))
    }

    override fun onBookmarkClick(position: Int, movie: Movie) {
        if (Favorites.isFavorite(movie.imdbID)) {
            Favorites.removeFavorite(movie.imdbID)
            viewModel.deleteMovie(movie.imdbID)
            searchLayout.snackbar("Removed from favorites!")
        } else {
            Favorites.addFavorite(movie.imdbID)
            viewModel.getDetails(movie.imdbID)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(searchToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
