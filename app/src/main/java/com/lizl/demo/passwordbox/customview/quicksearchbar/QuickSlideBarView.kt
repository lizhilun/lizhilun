package com.lizl.demo.passwordbox.customview.quicksearchbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.lizl.demo.passwordbox.R
import java.util.*

class QuickSlideBarView(context: Context, private var attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle)
{
    private var listener: OnQuickSideBarTouchListener? = null
    private var mLetters: List<String>? = null
    private var mChoose = -1
    private val mPaint = Paint()
    private var mTextSize: Float = 0.toFloat()
    private var mTextSizeChoose: Float = 0.toFloat()
    private var mTextColor: Int = 0
    private var mTextColorChoose: Int = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mItemHeight: Float = 0.toFloat()
    private var mItemStartY: Float = 0.toFloat()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        mLetters = Arrays.asList(*context.resources.getStringArray(R.array.quickSideBarLetters))

        mTextColor = context.resources.getColor(android.R.color.black)
        mTextColorChoose = context.resources.getColor(android.R.color.black)
        mTextSize = context.resources.getDimensionPixelSize(R.dimen.quick_search_bar_text_size).toFloat()
        mTextSizeChoose = context.resources.getDimensionPixelSize(R.dimen.quick_search_bar_choose_text_size).toFloat()
        if (attrs != null)
        {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.QuickSideBarView)

            mTextColor = a.getColor(R.styleable.QuickSideBarView_sidebarTextColor, mTextColor)
            mTextColorChoose = a.getColor(R.styleable.QuickSideBarView_sidebarTextColorChoose, mTextColorChoose)
            mTextSize = a.getDimension(R.styleable.QuickSideBarView_sidebarTextSize, mTextSize)
            mTextSizeChoose = a.getDimension(R.styleable.QuickSideBarView_sidebarTextSizeChoose, mTextSizeChoose)
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)
        for (i in mLetters!!.indices)
        {
            mPaint.color = mTextColor

            mPaint.isAntiAlias = true
            mPaint.textSize = mTextSize
            if (i == mChoose)
            {
                mPaint.color = mTextColorChoose
                mPaint.isFakeBoldText = true
                mPaint.typeface = Typeface.DEFAULT_BOLD
                mPaint.textSize = mTextSizeChoose
            }

            //计算位置
            val rect = Rect()
            mPaint.getTextBounds(mLetters!![i], 0, mLetters!![i].length, rect)
            val xPos = ((mWidth - rect.width()) * 0.5).toInt().toFloat()
            val yPos = mItemHeight * i + ((mItemHeight - rect.height()) * 0.5).toInt().toFloat() + mItemStartY

            canvas.drawText(mLetters!![i], xPos, yPos, mPaint)
            mPaint.reset()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = measuredHeight
        mWidth = measuredWidth
        mItemHeight = (mHeight / (mLetters?.size)!!).toFloat()
        mItemStartY = (mHeight - mLetters!!.size * mItemHeight) / 2
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean
    {
        val action = event.action
        val y = event.y
        val oldChoose = mChoose
        val newChoose = ((y - mItemStartY) / mItemHeight).toInt()
        when (action)
        {
            MotionEvent.ACTION_UP ->
            {
                mChoose = -1
                listener?.onLetterTouching(false)
                invalidate()
            }
            else ->
            {
                if (oldChoose != newChoose)
                {
                    if (newChoose >= 0 && newChoose < mLetters!!.size)
                    {
                        mChoose = newChoose
                        //计算位置
                        val rect = Rect()
                        mPaint.getTextBounds(mLetters!![mChoose], 0, mLetters!![mChoose].length, rect)
                        val yPos = mItemHeight * mChoose + ((mItemHeight - rect.height()) * 0.5).toInt().toFloat() + mItemStartY
                        listener?.onLetterChanged(mLetters!![newChoose], mChoose, yPos)
                    }
                    invalidate()
                }
                //如果是cancel也要调用onLetterUpListener 通知
                if (event.action == MotionEvent.ACTION_CANCEL)
                {
                    listener?.onLetterTouching(false)
                }
                else if (event.action == MotionEvent.ACTION_DOWN)
                {//按下调用 onLetterDownListener
                    listener?.onLetterTouching(true)
                }
            }
        }
        return true
    }

    fun setOnQuickSideBarTouchListener(listener: OnQuickSideBarTouchListener)
    {
        this.listener = listener
    }
}