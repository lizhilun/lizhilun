package com.lizl.demo.passwordbox.model

open class TitleBarBtnItem
{
    open class BaseItem

    class ImageBtnItem(val imageRedId: Int, val onBtnClickListener: () -> Unit) : BaseItem()

    class TextBtnItem(val text: String, val onBtnClickListener: () -> Unit) : BaseItem()
}