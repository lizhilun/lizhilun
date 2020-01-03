package com.lizl.demo.passwordbox.mvp.contract

import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.mvp.base.BasePresenter
import com.lizl.demo.passwordbox.mvp.base.BaseView

class SearchContract
{
    interface View : BaseView
    {
        fun showSearchResult(accountList: List<AccountModel>)
    }

    interface Presenter : BasePresenter<BaseView>
    {
        fun search(keyWord: String)
    }
}