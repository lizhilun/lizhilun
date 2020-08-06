package com.lizl.demo.passwordbox.adapter

import android.view.View
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.TitleBarBtnModel
import kotlinx.android.synthetic.main.item_title_bar_image_btn.view.*
import kotlinx.android.synthetic.main.item_title_bar_text_btn.view.*

class TitleBarBtnListAdapter(btnList: List<TitleBarBtnModel.BaseModel>) :
        BaseDelegateMultiAdapter<TitleBarBtnModel.BaseModel, TitleBarBtnListAdapter.ViewHolder>(btnList.toMutableList())
{

    companion object
    {
        const val ITEM_TYPE_IMAGE = 1
        const val ITEM_TYPE_TEXT = 2
    }

    init
    {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<TitleBarBtnModel.BaseModel>()
        {
            override fun getItemType(data: List<TitleBarBtnModel.BaseModel>, position: Int): Int
            {
                return when (data[position])
                {
                    is TitleBarBtnModel.ImageBtnModel -> ITEM_TYPE_IMAGE
                    is TitleBarBtnModel.TextBtnModel  -> ITEM_TYPE_TEXT
                    else                              -> ITEM_TYPE_TEXT
                }
            }
        })

        getMultiTypeDelegate()?.let {
            it.addItemType(ITEM_TYPE_IMAGE, R.layout.item_title_bar_image_btn)
            it.addItemType(ITEM_TYPE_TEXT, R.layout.item_title_bar_text_btn)
        }
    }

    override fun convert(helper: ViewHolder, item: TitleBarBtnModel.BaseModel)
    {
        when (item)
        {
            is TitleBarBtnModel.ImageBtnModel -> helper.bindImageHolder(item)
            is TitleBarBtnModel.TextBtnModel  -> helper.bindTextHolder(item)
        }
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindImageHolder(imageBtnModel: TitleBarBtnModel.ImageBtnModel)
        {
            itemView.iv_image_btn.setImageResource(imageBtnModel.imageRedId)
            itemView.setOnClickListener { imageBtnModel.onBtnClickListener.invoke() }
        }

        fun bindTextHolder(textBtnModel: TitleBarBtnModel.TextBtnModel)
        {
            itemView.tv_text_btn.text = textBtnModel.text
            itemView.setOnClickListener { textBtnModel.onBtnClickListener.invoke() }
        }
    }
}