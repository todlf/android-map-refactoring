package campus.tech.kakao.map.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ItemSearchBinding
import campus.tech.kakao.map.data.local.search.SearchData


class SearchAdapter : ListAdapter<SearchData, SearchAdapter.ViewHolder>(SearchDataDiffCallback()) {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onClick(v:View,position:Int)
    }

    inner class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchData: SearchData) {
            binding.searchData = searchData
            binding.executePendingBindings()
            itemView.setOnClickListener {
                if (::itemClickListener.isInitialized) {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onClick(it, position)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSearchBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener)
    {
        this.itemClickListener = onItemClickListener
    }

    private class SearchDataDiffCallback : DiffUtil.ItemCallback<SearchData>() {
        override fun areItemsTheSame(oldItem: SearchData, newItem: SearchData): Boolean {
            return oldItem.name == newItem.name && oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: SearchData, newItem: SearchData): Boolean {
            return oldItem == newItem
        }
    }
}