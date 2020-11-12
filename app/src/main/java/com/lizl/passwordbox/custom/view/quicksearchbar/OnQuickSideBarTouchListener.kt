package com.lizl.passwordbox.custom.view.quicksearchbar

interface OnQuickSideBarTouchListener
{
    fun onLetterChanged(letter: String, position: Int, y: Float)

    fun onLetterTouching(touching: Boolean)
}