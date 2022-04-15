package net.larntech.guasiapa.allstories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.larntech.guasiapa.network.ApiService

class StoriesViewModelFactory (
    private val api: ApiService
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StoryViewModel(api) as T
    }
}