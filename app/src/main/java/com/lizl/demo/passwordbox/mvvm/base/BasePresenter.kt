package com.lizl.demo.passwordbox.mvvm.base

interface BasePresenter<T : BaseView>
{
    fun onDestroy()
}