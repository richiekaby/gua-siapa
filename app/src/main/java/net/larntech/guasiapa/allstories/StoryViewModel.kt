package net.larntech.guasiapa.allstories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import net.larntech.guasiapa.model.stories.StoriesResponse
import net.larntech.guasiapa.network.ApiService
import java.util.concurrent.Flow

class StoryViewModel (
    private val api: ApiService
) : ViewModel() {
    fun getStories() =
        Pager(config = PagingConfig(pageSize = 10), pagingSourceFactory = {
            StoryPagingSource(api)
        }).flow.cachedIn(viewModelScope)


}