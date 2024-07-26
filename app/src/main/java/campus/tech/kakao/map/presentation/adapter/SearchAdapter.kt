package campus.tech.kakao.map.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ItemSearchBinding
import campus.tech.kakao.map.domain.model.SearchData


class SearchAdapter() : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener
    var searchDataList: List<SearchData> = emptyList()

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

    override fun getItemCount(): Int {
        return searchDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchDataList[position])
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener)
    {
        this.itemClickListener = onItemClickListener
    }
}