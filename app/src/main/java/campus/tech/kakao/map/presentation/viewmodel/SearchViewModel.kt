package campus.tech.kakao.map.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.SearchRepository
import campus.tech.kakao.map.domain.model.SearchData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchDataList = MutableLiveData<List<SearchData>>()
    val searchDataList: LiveData<List<SearchData>> get() = _searchDataList

    private val _savedSearchList = MutableLiveData<List<String>>()
    val savedSearchList: LiveData<List<String>> get() = _savedSearchList

    private val _filteredCategoryList = MutableLiveData<List<SearchData>>()
    val filteredCategoryList: LiveData<List<SearchData>> get() = _filteredCategoryList

    private val _filteredSavedWordList = MutableLiveData<List<SearchData>>()
    val filteredSavedWordList: LiveData<List<SearchData>> get() = _filteredSavedWordList

    val searchWord = MutableLiveData<String>()

    fun fetchData() {
        viewModelScope.launch {
            try {
                searchRepository.fetchApi()
                val data = searchRepository.loadDb()
                _searchDataList.value = data
            } catch (e: Exception) {
                Log.e("fetchDataError", "fetchData error!")
            }
        }
    }

    fun loadSavedWords() {
        viewModelScope.launch {
            val savedWords = searchRepository.getAllSavedWords()
            _savedSearchList.value = savedWords
        }
    }

    fun deleteSavedWord(word: String) {
        viewModelScope.launch {
            searchRepository.deleteSavedWord(word)
            loadSavedWords()
        }
    }

    fun saveSelectedPlaceName(name: String) {
        viewModelScope.launch {
            searchRepository.savePlaceName(name)
            loadSavedWords()
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch{
            _filteredCategoryList.value = _searchDataList.value?.filter { it.category == category }
        }
    }

    fun filterBySavedWord(savedWord: String) {
        viewModelScope.launch{
            _filteredSavedWordList.value = _searchDataList.value?.filter { it.name == savedWord }
        }
    }

    fun clearSearchWord() {
        searchWord.value = ""
        filterByCategory("")
    }
}
