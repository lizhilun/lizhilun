package com.lizl.demo.passwordbox.mvp.contract

import com.lizl.demo.passwordbox.mvp.base.BasePresenter
import com.lizl.demo.passwordbox.mvp.base.BaseView

class LockContract
{
    interface View : BaseView
    {
        fun onUnlockSuccess()
    }

    interface Presenter : BasePresenter<View>
    {
        fun startFingerprintAuthentication()

        fun checkInputPassword(password: String)
    }
}