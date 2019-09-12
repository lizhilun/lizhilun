package com.lizl.demo.passwordbox.customview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.TitleBarBtnListAdapter
import com.lizl.demo.passwordbox.model.TitleBarBtnItem

class CustomTitleBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ConstraintLayout(context, attrs, defStyleAttr)
{
    private lateinit var backBtn: AppCompatImageView
    private lateinit var titleTextView: AppCompatTextView
    private lateinit var btnListView: RecyclerView

    private var onBackBtnClickListener: OnBackBtnClickListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context, attrs)
    }

    fun initView(context: Context, attrs: AttributeSet?)
    {
        backBtn = AppCompatImageView(context)
        val padding = context.resources.getDimensionPixelOffset(R.dimen.toolbar_back_icon_padding)
        backBtn.scaleType = ImageView.ScaleType.FIT_START
        backBtn.setImageResource(R.mipmap.ic_back)
        backBtn.setPadding(0, padding, 0, padding)
        backBtn.id = generateViewId()
        addView(backBtn)

        titleTextView = AppCompatTextView(context)
        titleTextView.id = generateViewId()
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.toolbar_title_text_size))
        titleTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
        titleTextView.gravity = Gravity.CENTER_VERTICAL
        addView(titleTextView)

        btnListView = RecyclerView(context)
        btnListView.id = generateViewId()
        addView(btnListView)

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar)
        for (index in 0 until typeArray.indexCount)
        {
            when (val attr = typeArray.getIndex(index))
            {
                R.styleable.CustomTitleBar_backBtnVisible -> backBtn.visibility = if (typeArray.getBoolean(attr, true)) View.VISIBLE else View.GONE
                R.styleable.CustomTitleBar_titleText      -> titleTextView.text = typeArray.getString(attr)
            }
        }
        typeArray.recycle()

        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        constraintSet.constrainHeight(backBtn.id, LayoutParams.MATCH_PARENT)
        constraintSet.constrainWidth(backBtn.id, context.resources.getDimensionPixelOffset(R.dimen.toolbar_back_icon_size))
        constraintSet.connect(backBtn.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

        constraintSet.constrainHeight(titleTextView.id, LayoutParams.MATCH_PARENT)
        constraintSet.constrainWidth(titleTextView.id, LayoutParams.WRAP_CONTENT)
        constraintSet.connect(titleTextView.id, ConstraintSet.START, backBtn.id, ConstraintSet.END)

        constraintSet.constrainHeight(btnListView.id, LayoutParams.MATCH_PARENT)
        constraintSet.constrainWidth(btnListView.id, LayoutParams.MATCH_CONSTRAINT)
        constraintSet.connect(btnListView.id, ConstraintSet.START, titleTextView.id, ConstraintSet.END)
        constraintSet.connect(btnListView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        constraintSet.applyTo(this)

        backBtn.setOnClickListener { onBackBtnClickListener?.onBackBtnClick() }
    }

    fun setOnBackBtnClickListener(onBackBtnClickListener: OnBackBtnClickListener)
    {
        this.onBackBtnClickListener = onBackBtnClickListener
    }

    interface OnBackBtnClickListener
    {
        fun onBackBtnClick()
    }

    fun setBtnList(btnList: List<TitleBarBtnItem.BaseItem>)
    {
        btnListView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        btnListView.adapter = TitleBarBtnListAdapter(btnList)
    }

    fun setBackBtnVisible(visible: Boolean)
    {
        if (visible) View.VISIBLE else View.GONE
    }

    fun setTitleText(text : String)
    {
        titleTextView.text = text
    }
}