package com.lizl.demo.passwordbox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.UiApplication
import kotlinx.android.synthetic.main.item_number_key.view.*

class NumberKeyGridAdapter(private val keyList: List<String>) : RecyclerView.Adapter<NumberKeyGridAdapter.ViewHolder>()
{

    private var onNumberKeyClickListener: ((String) -> Unit)? = null

    override fun getItemCount(): Int = keyList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_number_key, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindViewHolder(keyList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindViewHolder(keyValue: String)
        {
            if (keyValue == "#")
            {
                itemView.tv_key.visibility = View.GONE
                itemView.iv_key.visibility = View.VISIBLE

                itemView.iv_key.setImageResource(R.mipmap.ic_backspace)
            }
            else
            {
                itemView.tv_key.visibility = View.VISIBLE
                itemView.iv_key.visibility = View.GONE
                itemView.tv_key.text = if (keyValue == "*") UiApplication.instance.getText(R.string.exit) else keyValue
            }

            itemView.setOnClickListener { onNumberKeyClickListener?.invoke(keyValue) }
        }
    }

    fun setOnNumberKeyClickListener(onNumberKeyClickListener: (String) -> Unit)
    {
        this.onNumberKeyClickListener = onNumberKeyClickListener
    }
}