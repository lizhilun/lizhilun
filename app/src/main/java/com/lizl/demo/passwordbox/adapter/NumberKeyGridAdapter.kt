package com.lizl.demo.passwordbox.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lizl.demo.passwordbox.R
import kotlinx.android.synthetic.main.item_number_key.view.*

class NumberKeyGridAdapter(private val context: Context, private val keyList: List<String>, private val onItemClickListener: OnNumberKeyClickListener) : RecyclerView.Adapter<NumberKeyGridAdapter.ViewHolder>()
{
    private var layoutInflater: LayoutInflater? = null

    init
    {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getItemCount(): Int = keyList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(layoutInflater!!.inflate(R.layout.item_number_key, parent, false))
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

                if (keyValue == "*")
                {
                    itemView.tv_key.text = context.getText(R.string.exit)
                }
                else
                {
                    itemView.tv_key.text = keyValue
                }
            }

            itemView.setOnClickListener { onItemClickListener.onNumberKeyClick(keyValue) }
        }
    }

    interface OnNumberKeyClickListener
    {
        fun onNumberKeyClick(keyValue: String)
    }
}