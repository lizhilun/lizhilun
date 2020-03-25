package com.lizl.demo.passwordbox.adapter

import android.view.View
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.UiApplication
import kotlinx.android.synthetic.main.item_number_key.view.*

class NumberKeyGridAdapter(keyList: List<String>) : BaseQuickAdapter<String, NumberKeyGridAdapter.ViewHolder>(R.layout.item_number_key, keyList.toMutableList())
{

    private var onNumberKeyClickListener: ((String) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: String)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(keyValue: String)
        {
            if (keyValue == "#")
            {
                itemView.tv_key.isVisible = false
                itemView.iv_key.isVisible = true

                itemView.iv_key.setImageResource(R.mipmap.ic_backspace)
            }
            else
            {
                itemView.tv_key.isVisible = true
                itemView.iv_key.isVisible = false

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