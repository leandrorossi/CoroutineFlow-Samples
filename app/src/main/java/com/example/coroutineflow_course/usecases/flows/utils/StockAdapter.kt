package com.example.coroutineflow_course.usecases.flows.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coroutineflow_course.R
import com.example.coroutineflow_course.databinding.RecyclerviewItemStockBinding
import com.example.coroutineflow_course.usecases.flows.mock.Stock
import java.text.NumberFormat

class StockAdapter : RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    var stockList: List<Stock>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerviewItemStockBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item_stock, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        val stock = stockList?.get(position) ?: return@with
        tvRank.text = stock.rank.toString()
        tvName.text = stock.name
        val currentPriceFormatted: String = formatter.format(stock.currentPrice)
        tvCurrentPrice.text = currentPriceFormatted
    }

    override fun getItemCount(): Int {
        return stockList?.size ?: 0
    }

}