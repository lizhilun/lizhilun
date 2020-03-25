package com.lizl.demo.passwordbox.adapter

import android.view.View
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.TitleBarBtnItem
import kotlinx.android.synthetic.main.item_title_bar_image_btn.view.*
import kotlinx.android.synthetic.main.item_title_bar_text_btn.view.*

class TitleBarBtnListAdapter(btnList: List<TitleBarBtnItem.BaseItem>) :
        BaseDelegateMultiAdapter<TitleBarBtnItem.BaseItem, TitleBarBtnListAdapter.ViewHolder>(btnList.toMutableList())
{

    companion object
    {
        const val ITEM_TYPE_IMAGE = 1
        const val ITEM_TYPE_TEXT = 2
    }

    init
    {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<TitleBarBtnItem.BaseItem>()
        {
            override fun getItemType(data: List<TitleBarBtnItem.BaseItem>, position: Int): Int
            {
                return when (data[position])
                {
                    is TitleBarBtnItem.ImageBtnItem -> ITEM_TYPE_IMAGE
                    is TitleBarBtnItem.TextBtnItem  -> ITEM_TYPE_TEXT
                    else                            -> ITEM_TYPE_TEXT
                }
            }
        })

        getMultiTypeDelegate()?.let {
            it.addItemType(ITEM_TYPE_IMAGE, R.layout.item_title_bar_image_btn)
            it.addItemType(ITEM_TYPE_TEXT, R.layout.item_title_bar_text_btn)
        }
    }

    override fun convert(helper: ViewHolder, item: TitleBarBtnItem.BaseItem)
    {
        when (item)
        {
            is TitleBarBtnItem.ImageBtnItem -> helper.bindImageHolder(item)
            is TitleBarBtnItem.TextBtnItem  -> helper.bindTextHolder(item)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
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