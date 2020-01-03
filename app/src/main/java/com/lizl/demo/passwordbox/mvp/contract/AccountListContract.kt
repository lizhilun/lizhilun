package com.lizl.demo.passwordbox.mvp.contract

import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.mvp.base.BasePresenter
import com.lizl.demo.passwordbox.mvp.base.BaseView

class AccountListContract
{
    interface View : BaseView
    {
        fun showAccountList(accountList: List<AccountModel>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun getAllAccounts()
    }
}