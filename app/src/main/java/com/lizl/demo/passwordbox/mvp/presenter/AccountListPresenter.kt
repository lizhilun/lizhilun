package com.lizl.demo.passwordbox.mvp.presenter

import com.lizl.demo.passwordbox.mvp.contract.AccountListContract
import com.lizl.demo.passwordbox.util.DataUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AccountListPresenter(private var view: AccountListContract.View?) : AccountListContract.Presenter
{
    override fun getAllAccounts()
    {
        GlobalScope.launch {

            val accountList = DataUtil.getInstance().queryAll()

            GlobalScope.launch(Dispatchers.Main) { view?.showAccountList(accountList) }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}