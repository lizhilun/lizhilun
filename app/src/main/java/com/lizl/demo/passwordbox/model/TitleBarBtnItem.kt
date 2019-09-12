package com.lizl.demo.passwordbox.model

open class TitleBarBtnItem
{
    open class BaseItem

    class ImageBtnItem(val imageRedId: Int, val onBtnClickListener: OnBtnClickListener) : BaseItem()

    class TextBtnItem(val text: String, val onBtnClickListener: OnBtnClickListener) : BaseItem()

    interface OnBtnClickListener
    {
        fun onBtnClick()
    }
}