package com.lizl.demo.passwordbox.adapter

import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.UiApplication
import kotlinx.android.synthetic.main.item_number_key.view.*

class NumberKeyGridAdapter(keyList: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_number_key, keyList.toMutableList())
{

    private var onNumberKeyClickListener: ((String) -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: String)
    {
        with(helper.itemView) {
            if (item == "#")
            {
                tv_key.isVisible = false
                iv_key.isVisible = true

                iv_key.setImageResource(R.drawable.ic_backspace)
            }
            else
            {
                tv_key.isVisible = true
                iv_key.isVisible = false

                tv_key.text = if (item == "*") UiApplication.instance.getText(R.string.exit) else item
            }

            setOnClickListener { onNumberKeyClickListener?.invoke(item) }
        }
    }


    fun setOnNumberKeyClickListener(onNumberKeyClickListener: (String) -> Unit)
    {
        this.onNumberKeyClickListener = onNumberKeyClickListener
    }
}