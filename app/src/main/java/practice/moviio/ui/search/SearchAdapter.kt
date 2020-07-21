package practice.moviio.ui.search

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_movie_search.view.*
import practice.moviio.R
import practice.moviio.data.response.movies.Movie
import practice.moviio.utils.Favorites
import practice.moviio.utils.inflate
import javax.inject.Inject

class SearchAdapter @Inject constructor(private val clickListener: OnMovieClicked, private val glide: RequestManager) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var movieList = listOf<Movie>()

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnMovieClicked {
        fun onClick(movie: Movie, position: Int)
        fun onBookmarkClick(position: Int, movie: Movie)
    }

    class SearchItemDiffCallback(private val oldSearchList: List<Movie>, private val newSearchList: List<Movie>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSearchList[oldItemPosition].imdbID == newSearchList[newItemPosition].imdbID
        }

        override fun getOldListSize(): Int = oldSearchList.size

        override fun getNewListSize(): Int = newSearchList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSearchList[oldItemPosition] == newSearchList[newItemPosition]
        }
    }

    fun setData(newMovieList: List<Movie>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(SearchItemDiffCallback(movieList, newMovieList))
        movieList = newMovieList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(parent.inflate(R.layout.item_movie_search))
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movie = movieList[position]

        with(holder.itemView) {
            itemTitleTextView.text = movie.title
            itemDurationTextView.text = movie.year.toString()
            glide.load(movie.poster)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(itemPosterImageView)


            rootView.setOnClickListener {
                clickListener.onClick(movie, position)
            }



            setBookmarkIcon(movie, false)

            itemBookmarkImageView.setOnClickListener {
                setBookmarkIcon(movie, true)
                clickListener.onBookmarkClick(position, movie)
            }

        }
    }

    private fun View.setBookmarkIcon(movie: Movie, switch: Boolean) {
        if (Favorites.isFavorite(movie.imdbID) != switch) itemBookmarkImageView.setImageResource(R.drawable.ic_bookmark)
        else itemBookmarkImageView.setImageResource(R.drawable.ic_bookmark_border)
    }
}