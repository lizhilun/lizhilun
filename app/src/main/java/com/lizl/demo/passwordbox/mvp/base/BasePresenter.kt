package com.lizl.demo.passwordbox.mvp.base

interface BasePresenter<T : BaseView>
{
    fun onDestroy()
}