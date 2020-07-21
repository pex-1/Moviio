package practice.moviio.utils

import practice.moviio.data.response.details.MovieDetails

object Favorites {
    private var ids: HashMap<String, Boolean> = HashMap()

    fun addFavorite(id: String) = ids.put(id, true)

    fun removeFavorite(id: String) = ids.remove(id)

    fun setFavorites(favorites: List<MovieDetails>) {
        for (movie in favorites) {
            ids[movie.imdbID] = true
        }
    }

    fun isFavorite(id: String): Boolean = ids.containsKey(id)


}