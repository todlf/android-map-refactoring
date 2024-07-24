package campus.tech.kakao.map.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.SavedSearchDao
import campus.tech.kakao.map.data.SearchDataDao
import campus.tech.kakao.map.data.SearchDbHelper
import campus.tech.kakao.map.data.SearchRepository
import campus.tech.kakao.map.domain.model.SearchData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel(){
    private val _searchDataList = MutableLiveData<List<SearchData>>()
    val searchDataList: LiveData<List<SearchData>> get() = _searchDataList

    private val _savedSearchList = MutableLiveData<List<String>>()
    val savedSearchList: LiveData<List<String>> get() = _savedSearchList

    fun fetchData() {
        viewModelScope.launch {
            try {
                searchRepository.fetchApi()
                val data = withContext(Dispatchers.IO) { searchRepository.loadDb() }
                _searchDataList.value = data
            } catch (e: Exception) {
                Log.e("fetchDataError", "fetchData error!")
            }
        }
    }

    fun loadSavedWords() {
        viewModelScope.launch {
            val savedWords = withContext(Dispatchers.IO) { searchRepository.getAllSavedWords() }
            _savedSearchList.value = savedWords
        }
    }
    fun deleteSavedWord(word: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchRepository.deleteSavedWord(word)
                loadSavedWords()
            }
        }
    }

    fun saveSelectedPlaceName(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchRepository.savePlaceName(name)
            }
            loadSavedWords()
        }
    }

}
