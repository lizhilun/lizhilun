package com.lizl.demo.passwordbox.model

open class TitleBarBtnModel
{
    open class BaseModel

    class ImageBtnModel(val imageRedId: Int, val onBtnClickListener: () -> Unit) : BaseModel()

    class TextBtnModel(val text: String, val onBtnClickListener: () -> Unit) : BaseModel()
}