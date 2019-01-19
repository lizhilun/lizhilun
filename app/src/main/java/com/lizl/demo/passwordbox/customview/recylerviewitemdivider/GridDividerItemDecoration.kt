package com.lizl.demo.passwordbox.customview.recylerviewitemdivider

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class GridDividerItemDecoration() : RecyclerView.ItemDecoration()
{
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?)
    {
        outRect?.top = 1

        if ((parent?.getChildLayoutPosition(view)!! + 1) % 3 == 0)
        {
            outRect?.right = 0
        }
        else
        {
            outRect?.right = 1
        }
    }
}
