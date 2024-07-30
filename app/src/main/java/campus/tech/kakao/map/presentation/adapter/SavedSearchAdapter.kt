package campus.tech.kakao.map.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ItemSaveWordBinding

class SavedSearchAdapter : RecyclerView.Adapter<SavedSearchAdapter.ViewHolder>() {

    private lateinit var savedWordClickListener: OnSavedWordClickListener
    var savedSearchList: MutableList<String> = mutableListOf()

    interface OnSavedWordClickListener {
        fun onDeleteClick(position: Int)
        fun onSavedWordClick(savedWord: String)
    }

    inner class ViewHolder(val binding: ItemSaveWordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(savedWord: String) {
            binding.savedWord = savedWord
            binding.deleteSavedWord.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    savedWordClickListener.onDeleteClick(position)
                }
            }
            binding.savedWordTextView.setOnClickListener {
                savedWordClickListener.onSavedWordClick(savedWord)
            }
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSaveWordBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_save_word,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return savedSearchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedSearchList[position])
    }

    fun setOnSavedWordClickListener(listener: OnSavedWordClickListener) {
        this.savedWordClickListener = listener
    }
}