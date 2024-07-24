package campus.tech.kakao.map.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R

class SavedSearchAdapter() : RecyclerView.Adapter<SavedSearchAdapter.ViewHolder>() {

    private lateinit var savedWordClickListener: OnSavedWordClickListener
    var savedSearchList: MutableList<String> = mutableListOf()

    interface OnSavedWordClickListener {
        fun onDeleteClick(position: Int)
        fun onSavedWordClick(savedWord: String)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val savedWordTextView: TextView = view.findViewById(R.id.savedWord)
        private val deleteSavedWord: Button = view.findViewById(R.id.deleteSavedWord)

        fun bind(savedWord: String) {
            this.savedWordTextView.text = savedWord
            deleteSavedWord.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    savedWordClickListener.onDeleteClick(position)
                }
            }
            savedWordTextView.setOnClickListener {
                savedWordClickListener.onSavedWordClick(savedWord)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_save_word, parent, false)
        return ViewHolder(view)
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