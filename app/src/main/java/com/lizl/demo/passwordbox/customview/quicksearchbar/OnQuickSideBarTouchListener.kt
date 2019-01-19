package com.lizl.demo.passwordbox.customview.quicksearchbar

interface OnQuickSideBarTouchListener
{
    fun onLetterChanged(letter: String, position: Int, y: Float)

    fun onLetterTouching(touching: Boolean)
}