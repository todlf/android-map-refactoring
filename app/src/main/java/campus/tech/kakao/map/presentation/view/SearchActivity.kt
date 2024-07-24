package campus.tech.kakao.map.presentation.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.presentation.adapter.SavedSearchAdapter
import campus.tech.kakao.map.presentation.adapter.SearchAdapter
import campus.tech.kakao.map.domain.model.SearchData
import campus.tech.kakao.map.presentation.viewmodel.KakaoMapViewModel
import campus.tech.kakao.map.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    private lateinit var searchWord: EditText
    private lateinit var deleteSearchWord: Button
    private lateinit var searchNothing: TextView

    private lateinit var savedSearchWordRecyclerView: RecyclerView
    private lateinit var savedSearchAdapter: SavedSearchAdapter


    private val searchViewModel: SearchViewModel by viewModels()

    private val kakaoMapviewModel: KakaoMapViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recyclerView)
        searchWord = findViewById(R.id.searchWord)
        deleteSearchWord = findViewById(R.id.deleteSearchWord)
        searchNothing = findViewById(R.id.searchNothing)
        savedSearchWordRecyclerView = findViewById(R.id.savedSearchWordRecyclerView)
        savedSearchWordRecyclerView.visibility = View.GONE

        adapter = SearchAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        savedSearchAdapter = SavedSearchAdapter()

        savedSearchWordRecyclerView.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )

        savedSearchWordRecyclerView.adapter = savedSearchAdapter

        liveDataObserver()

        searchViewModel.loadSavedWords()
        searchViewModel.fetchData()
        deleteWord()

        textChangeListener()

        itemClickSaveWord()
        savedWordClick()
    }

    private fun liveDataObserver(){
        searchViewModel.searchDataList.observe(this, Observer { data ->
            data?.let {
                adapter.searchDataList = data.toMutableList()
                showDb()
            }
        })

        searchViewModel.savedSearchList.observe(this, Observer { savedWords ->
            savedWords?.let {
                savedSearchAdapter.savedSearchList = savedWords.toMutableList()
                savedSearchAdapter.notifyDataSetChanged()
            }
        })

        searchViewModel.filteredCategoryList.observe(this, Observer { filteredCategory ->
            filteredCategory?.let {
                adapter.searchDataList = it
                adapter.notifyDataSetChanged()
                showFilteredList(it)
            }
        })

        searchViewModel.filteredSavedWordList.observe(this, Observer { filteredSavedWord ->
            filteredSavedWord?.let {
                adapter.searchDataList = it
                adapter.notifyDataSetChanged()
                showFilteredList(it)
            }
        })
    }
    private fun showDb() {
        if (searchWord.text.isEmpty()) {
            adapter.searchDataList = emptyList()
            recyclerView.visibility = View.GONE
            searchNothing.visibility = View.VISIBLE
            savedSearchWordRecyclerView.visibility = View.GONE
        } else {
            adapter.searchDataList = searchViewModel.searchDataList.value ?: emptyList()
            recyclerView.visibility = View.VISIBLE
            searchNothing.visibility = View.GONE
            savedSearchWordRecyclerView.visibility = View.VISIBLE
        }
        adapter.notifyDataSetChanged()
    }

    private fun showFilteredList(filteredList: List<SearchData>) {
        if (filteredList.isEmpty()) {
            recyclerView.visibility = View.GONE
            searchNothing.visibility = View.VISIBLE
            savedSearchWordRecyclerView.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
            searchNothing.visibility = View.GONE
            savedSearchWordRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun deleteWord() {
        deleteSearchWord.setOnClickListener {
            searchWord.text.clear()
            showFilteredList(emptyList())
        }
    }

    private fun textChangeListener(){
        searchWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTerm = s.toString()
                if (searchTerm.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    savedSearchWordRecyclerView.visibility = View.GONE
                } else {
                    searchViewModel.filterByCategory(searchTerm)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun itemClickSaveWord() {
        adapter.setItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val searchData = adapter.searchDataList[position]
                searchViewModel.saveSelectedPlaceName(searchData.name)

                kakaoMapviewModel.saveCoordinates(searchData.x, searchData.y)
                kakaoMapviewModel.saveToBottomSheet(searchData.name, searchData.address)

                val intent = Intent(this@SearchActivity, KakaoMapViewActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun savedWordClick() {
        savedSearchAdapter.setOnSavedWordClickListener(object :
            SavedSearchAdapter.OnSavedWordClickListener {
            override fun onSavedWordClick(savedWord: String) {
                searchViewModel.filterBySavedWord(savedWord)
            }

            override fun onDeleteClick(position: Int) {
                searchViewModel.deleteSavedWord(savedSearchAdapter.savedSearchList[position])
                savedSearchAdapter.savedSearchList.removeAt(position)
                savedSearchAdapter.notifyItemRemoved(position)
            }
        })
    }
}



