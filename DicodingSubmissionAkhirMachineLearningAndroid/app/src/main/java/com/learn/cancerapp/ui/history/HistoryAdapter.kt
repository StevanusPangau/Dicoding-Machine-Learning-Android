package com.learn.cancerapp.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learn.cancerapp.data.history.entity.HistoryEntity
import com.learn.cancerapp.databinding.ItemRowHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyViewHolder {
        val binding =
            ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(review)
        }
    }

    class MyViewHolder(val binding: ItemRowHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity) {
            // Menampilkan gambar menggunakan Glide
            Glide.with(itemView)
                .load(history.gambar)
                .into(binding.ivPhoto)

            binding.tvHasil.text = history.hasilPrediksi

            // Menampilkan tanggal
            val dateFormat = SimpleDateFormat("dd MMMM yyyy | HH:mm", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(history.tanggal))
            binding.tvTanggal.text = formattedDate
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: HistoryEntity)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}