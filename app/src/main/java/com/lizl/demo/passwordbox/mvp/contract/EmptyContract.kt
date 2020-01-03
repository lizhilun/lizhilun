package com.lizl.demo.passwordbox.mvp.contract

import com.lizl.demo.passwordbox.mvp.base.BasePresenter
import com.lizl.demo.passwordbox.mvp.base.BaseView

class EmptyContract
{
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}