package net.larntech.guasiapa.allstories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.larntech.guasiapa.model.stories.StoriesResponse
import net.larntech.guasiapa.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class StoryPagingSource (
    private val service: ApiService
) : PagingSource<Int, StoriesResponse.ListStoryBean>() {


    private  val TMDB_STARTING_PAGE_INDEX = 1
    private val NETWORK_PAGE_SIZE = 10

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoriesResponse.ListStoryBean> {
        val pageIndex = params.key ?: TMDB_STARTING_PAGE_INDEX
        return try {
            val response = service.allStories(
                page = pageIndex
            )
            val stories = response;
            val nextKey =
                if (stories.listStory!!.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = response.listStory!!,
                prevKey = if (pageIndex == TMDB_STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, StoriesResponse.ListStoryBean>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


}