package practice.moviio.ui.favorites

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_movie_favorites.view.*
import kotlinx.android.synthetic.main.item_movie_search.view.itemBookmarkImageView
import kotlinx.android.synthetic.main.item_movie_search.view.itemDurationTextView
import kotlinx.android.synthetic.main.item_movie_search.view.itemPosterImageView
import kotlinx.android.synthetic.main.item_movie_search.view.itemTitleTextView
import practice.moviio.R
import practice.moviio.data.response.details.MovieDetails
import practice.moviio.utils.getDuration
import practice.moviio.utils.gone
import practice.moviio.utils.inflate
import practice.moviio.utils.visible
import javax.inject.Inject

class FavoritesAdapter @Inject constructor(private val clickListener: OnMovieClicked, private val glide: RequestManager) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    private var movieList = listOf<MovieDetails>()

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnMovieClicked {
        fun onClick(movie: MovieDetails)
        fun onBookmarkClick(position: Int, movie: MovieDetails)
    }

    class SearchItemDiffCallback(private val oldFavoritesList: List<MovieDetails>, private val newFavoritesList: List<MovieDetails>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldFavoritesList[oldItemPosition].imdbID == newFavoritesList[newItemPosition].imdbID
        }

        override fun getOldListSize(): Int = oldFavoritesList.size

        override fun getNewListSize(): Int = newFavoritesList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldFavoritesList[oldItemPosition] == newFavoritesList[newItemPosition]
        }
    }

    fun setData(newMovieList: List<MovieDetails>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(SearchItemDiffCallback(movieList, newMovieList))
        movieList = newMovieList
        diffResult.dispatchUpdatesTo(this)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(parent.inflate(R.layout.item_movie_favorites))
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val movie = movieList[position]

        with(holder.itemView) {
            itemTitleTextView.text = movie.title
            itemDurationTextView.text = getDuration(movie.runtime.split(" ")[0])
            itemGenreTextView.text = movie.genre
            itemReleaseDateTextView.text = movie.released
            itemImdbRating.text = movie.imdbRating
            glide.load(movie.poster)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(itemPosterImageView)



            rootView.setOnClickListener {
                clickListener.onClick(movie)
            }

            itemBookmarkImageView.setOnClickListener {
                clickListener.onBookmarkClick(position, movie)
            }


            if (position == 0 || movieList[position - 1].year != movie.year) {
                favoritesLayoutYearTextView.text = movie.year.toString()
                favoritesItemYearLayout.visible()
            } else favoritesItemYearLayout.gone()


        }
    }


}