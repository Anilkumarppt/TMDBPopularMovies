package com.anil.tmdbpopularmovies.util

object AppConstants {

    const val BASE_URL = "https://api.themoviedb.org/3/"

    const val GET_POPULAR = "movie/popular"

    const val API_PARAM = "api_key"

    const val MOVIE = "movie/{movie_id}"

    const val CREDITS="movie/{movie_id}/credits"

    const val API_KEY="25e34d6e0c1879dffdee9ef13dc4186d"   //  f60ae473c8d03fdd88e75848cea96a8e


    // related to paging
    const val STARTING_PAGE_INDEX = 1
    const val ENDING_PAGE_INDEX = 50
    const val NUM_PAGES_IN_CACHE = 3
    const val NUM_RESULTS_PER_PAGE = 20

     const val CONNECT_TIMEOUT = 20L
     const val READ_TIMEOUT = 60L
     const val WRITE_TIMEOUT = 120L
}