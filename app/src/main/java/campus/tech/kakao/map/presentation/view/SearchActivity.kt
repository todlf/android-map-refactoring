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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.presentation.adapter.SavedSearchAdapter
import campus.tech.kakao.map.presentation.adapter.SearchAdapter
import campus.tech.kakao.map.domain.model.SearchData
import campus.tech.kakao.map.presentation.viewmodel.KakaoMapViewModel
import campus.tech.kakao.map.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter
    private val searchViewModel: SearchViewModel by viewModels()
    private val kakaoMapviewModel: KakaoMapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        binding.search = this
        binding.searchViewModel = searchViewModel

        binding.savedSearchWordRecyclerView.visibility = View.GONE

        adapter = SearchAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        savedSearchAdapter = SavedSearchAdapter()

        binding.savedSearchWordRecyclerView.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )

        binding.savedSearchWordRecyclerView.adapter = savedSearchAdapter

        liveDataObserver()
        initView()

    }

    private fun liveDataObserver() {
        searchViewModel.searchDataList.observe(this) { data ->
            data?.let {
                adapter.searchDataList = data.toMutableList()
                showDb()
            }
        }

        searchViewModel.savedSearchList.observe(this) { savedWords ->
            savedWords?.let {
                savedSearchAdapter.savedSearchList = savedWords.toMutableList()
                savedSearchAdapter.notifyDataSetChanged()
            }
        }

        searchViewModel.filteredCategoryList.observe(this) { filteredCategory ->
            filteredCategory?.let {
                adapter.searchDataList = it
                adapter.notifyDataSetChanged()
                showFilteredList(it)
            }
        }

        searchViewModel.filteredSavedWordList.observe(this) { filteredSavedWord ->
            filteredSavedWord?.let {
                adapter.searchDataList = it
                adapter.notifyDataSetChanged()
                showFilteredList(it)
            }
        }
    }

    private fun showDb() {
        if (binding.searchWord.text.isEmpty()) {
            adapter.searchDataList = emptyList()
            binding.recyclerView.visibility = View.GONE
            binding.searchNothing.visibility = View.VISIBLE
            binding.savedSearchWordRecyclerView.visibility = View.GONE
        } else {
            adapter.searchDataList = searchViewModel.searchDataList.value ?: emptyList()
            binding.recyclerView.visibility = View.VISIBLE
            binding.searchNothing.visibility = View.GONE
            binding.savedSearchWordRecyclerView.visibility = View.VISIBLE
        }
        adapter.notifyDataSetChanged()
    }

    private fun showFilteredList(filteredList: List<SearchData>) {
        if (filteredList.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.searchNothing.visibility = View.VISIBLE
            binding.savedSearchWordRecyclerView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.searchNothing.visibility = View.GONE
            binding.savedSearchWordRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun deleteWord() {
        binding.deleteSearchWord.setOnClickListener {
            binding.searchWord.text.clear()
            showFilteredList(emptyList())
        }
    }

    private fun textChangeListener() {
        binding.searchWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTerm = s.toString()
                if (searchTerm.isEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                    binding.savedSearchWordRecyclerView.visibility = View.GONE
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

    private fun initView() {
        searchViewModel.loadSavedWords()
        searchViewModel.fetchData()
        deleteWord()
        textChangeListener()
        itemClickSaveWord()
        savedWordClick()
    }
}



