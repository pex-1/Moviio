package practice.moviio.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_search.*
import practice.moviio.R
import practice.moviio.data.response.details.MovieDetails
import practice.moviio.di.viewmodel.ViewModelProviderFactory
import practice.moviio.utils.*
import javax.inject.Inject

class DetailsActivity : DaggerAppCompatActivity() {

    companion object {
        private const val DETAILS = "DETAILS"
        fun newInstance(context: Context, imdbId: String): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(DETAILS, imdbId)
            return intent
        }

        private const val DETAILS_FAVORITES = "DETAILS"
        fun newInstanceFavorites(context: Context, movie: MovieDetails): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(DETAILS_FAVORITES, movie)
            intent.putExtra(DETAILS_FAVORITES, bundle)
            return intent
        }
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: DetailsViewModel

    @Inject
    lateinit var glide: RequestManager

    private lateinit var movie: MovieDetails

    private var menuItem: Menu? = null

    private var database = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setToolbar()

        viewModel = ViewModelProvider(this, providerFactory).get(DetailsViewModel::class.java)
        val bundle = intent.getBundleExtra(DETAILS_FAVORITES)
        val myObject = bundle?.getParcelable<MovieDetails>(DETAILS_FAVORITES)

        if (bundle != null && myObject != null) {
            movie = myObject
            setUi(myObject)
            database = true
        } else {
            networkSetUi()
        }


    }

    private fun networkSetUi() {
        val id = intent.getStringExtra(DETAILS)
        if (id != null) {
            viewModel.getDetails(id)
        }
        subscribeObservers()
    }


    private fun subscribeObservers() {
        viewModel.details.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    detailsProgressBar.gone()
                    response.data?.let {
                        movie = it
                        setUi(it)
                        if (Favorites.isFavorite(movie.imdbID)) menuItem?.getItem(0)
                                ?.setIcon(R.drawable.ic_bookmark)
                    }
                }
                is Resource.Error -> {
                    detailsProgressBar.gone()
                    response.message?.let { message ->
                        detailsLayout.snackbar("An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    detailsProgressBar.visible()
                }
            }
        })
    }


    private fun createView(text: String) {

        val textView = TextView(this)
        val textParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val cardView = CardView(this)
        val cardParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        cardParams.marginEnd = 10.toPx()

        cardView.layoutParams = cardParams
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhiteTransparent))
        cardView.setContentPadding(2.toPx(), 0, 4.toPx(), 0)

        textView.layoutParams = textParams
        textView.text = text
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorTitle))
        cardView.addView(textView)
        genreLayout.addView(cardView)
    }

    private fun setUi(movie: MovieDetails) {
        glide.load(movie.poster)
                .centerCrop()
                .into(detailsCoverImageView)

        detailsTitleTextView.text = movie.title
        releaseDateAndDurationTextView.text = "${movie.released}  \u2022  ${getDuration(movie.runtime.split(" ")[0])}"
        detailsPlotTextView.text = movie.plot
        detailsImdbRating.text = "${movie.imdbRating}/10"
        detailsImdbVotes.text = movie.imdbVotes

        detailsToolbar.title = movie.title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val genres = movie.genre.split(",")

        for (g in genres) {
            createView(g)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuItem = menu
        menuInflater.inflate(R.menu.details_menu, menu)
        if (database && Favorites.isFavorite(movie.imdbID)) menu?.getItem(0)
                ?.setIcon(R.drawable.ic_bookmark)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.detailsBookmarkMenu -> {
                if (Favorites.isFavorite(movie.imdbID)) {
                    item.setIcon(R.drawable.ic_bookmark_border)
                    detailsLayout.snackbar("Movie removed!")
                    viewModel.deleteMovie(movie)
                    Favorites.removeFavorite(movie.imdbID)
                } else {
                    item.setIcon(R.drawable.ic_bookmark)
                    detailsLayout.snackbar("Movie stored!")
                    viewModel.insertMovie(movie)
                    Favorites.addFavorite(movie.imdbID)
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(detailsToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
