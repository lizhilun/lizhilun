package com.lizl.demo.passwordbox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.TitleBarBtnItem
import kotlinx.android.synthetic.main.item_title_bar_image_btn.view.*
import kotlinx.android.synthetic.main.item_title_bar_text_btn.view.*

class TitleBarBtnListAdapter(private val btnList: List<TitleBarBtnItem.BaseItem>) : RecyclerView.Adapter<TitleBarBtnListAdapter.ViewHolder>()
{

    companion object
    {
        const val ITEM_TYPE_IMAGE = 1
        const val ITEM_TYPE_TEXT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return when (viewType)
        {
            ITEM_TYPE_IMAGE -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_title_bar_image_btn, parent, false))
            else            -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_title_bar_text_btn, parent, false))
        }
    }

    override fun getItemCount() = btnList.size

    override fun getItemViewType(position: Int): Int
    {
        if (position >= itemCount)
        {
            return -1
        }
        return when
        {
            btnList[position] is TitleBarBtnItem.ImageBtnItem -> ITEM_TYPE_IMAGE
            btnList[position] is TitleBarBtnItem.TextBtnItem  -> ITEM_TYPE_TEXT
            else                                              -> -1
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        when (getItemViewType(position))
        {
            ITEM_TYPE_IMAGE -> holder.bindImageHolder(btnList[position] as TitleBarBtnItem.ImageBtnItem)
            ITEM_TYPE_TEXT  -> holder.bindTextHolder(btnList[position] as TitleBarBtnItem.TextBtnItem)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindImageHolder(imageBtnItem: TitleBarBtnItem.ImageBtnItem)
        {
            itemView.iv_image_btn.setImageResource(imageBtnItem.imageRedId)
            itemView.setOnClickListener { imageBtnItem.onBtnClickListener.invoke() }
        }

        fun bindTextHolder(textBtnItem: TitleBarBtnItem.TextBtnItem)
        {
            itemView.tv_text_btn.text = textBtnItem.text
            itemView.setOnClickListener { textBtnItem.onBtnClickListener.invoke() }
        }
    }
}