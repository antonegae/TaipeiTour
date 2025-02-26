package com.rick.ricktour.view.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rick.ricktour.R
import com.rick.ricktour.api.TourItem
import com.rick.ricktour.databinding.RvLoadingBinding
import com.rick.ricktour.databinding.RvMainItemBinding
import com.squareup.picasso.Picasso


class TourAdapter(var activity: FragmentActivity?, private val itemClick: (TourItem) -> Unit, private val onDataUpdated: () -> Unit) :
    ListAdapter<TourItem, RecyclerView.ViewHolder>(DiffItemCallback()) {

    private var TYPE_NORMAL = 0
    private var TYPE_FOOTER = 1

    //是否顯示Footer
    private var showFooter: Boolean = true

    inner class AttractionAdapterViewHolder(var binding: RvMainItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult", "SetTextI18n")
        fun bind(model: TourItem) {

            binding.attractionName.text = model.name ?: activity?.getString(R.string.attraction)

            binding.attractionDescription.text = model.introduction ?: activity?.getString(R.string.attraction_introduction)

            //image
            if (model.images.isNotEmpty() && model.images.get(0) != null) {

                val src = model.images[0]?.src
                Picasso.get().load(src ?: "").placeholder(R.drawable.taipei_icon).into(binding.attractionImage)

            } else {

                Picasso.get().load(R.drawable.taipei_icon).into(binding.attractionImage)

            }

            binding.mainView.setOnClickListener {

                itemClick(model)

            }

        }

    }

    inner class FooterViewHolder(var binding: RvLoadingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {

            binding.tvLoading.visibility = if (!showFooter) View.GONE else View.VISIBLE

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (TYPE_NORMAL == viewType) {

            val itemView = RvMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AttractionAdapterViewHolder(itemView)

        } else {

            val itemView = RvLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FooterViewHolder(itemView)

        }

    }

    override fun getItemCount(): Int {

        val count = currentList.size
        return if (count > 0) count + 1 else 0

    }

    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount - 1) {

            TYPE_FOOTER

        } else {

            TYPE_NORMAL

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {

            TYPE_NORMAL -> {

                val item = currentList[position]
                item.let {

                    (holder as AttractionAdapterViewHolder).bind(it)

                }

            }

            else -> {//TYPE_FOOTER

                (holder as FooterViewHolder).bind()

            }


        }

    }

    //資料疊加
    fun updateData(data: MutableList<TourItem>?) {

        val newItems = mutableListOf<TourItem>()
        newItems.addAll(currentList)
        data?.let { newItems.addAll(it) }
        submitList(newItems)

    }

    fun showFooter(show: Boolean) {

        if (currentList.isEmpty()) {

            showFooter = false

        } else if (showFooter != show) {

            showFooter = show
            submitList(currentList.toList())

        }

    }

    fun getAttractionsSize(): String = currentList.count().toString()

    override fun onCurrentListChanged(
        previousList: MutableList<TourItem>,
        currentList: MutableList<TourItem>,
    ) {
        super.onCurrentListChanged(previousList, currentList)
        onDataUpdated() // 調用回調
    }

}

//是否有更新比較
private class DiffItemCallback : DiffUtil.ItemCallback<TourItem>() {
    override fun areItemsTheSame(oldItem: TourItem, newItem: TourItem): Boolean {

        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(oldItem: TourItem, newItem: TourItem): Boolean {

        return oldItem == newItem

    }

}


