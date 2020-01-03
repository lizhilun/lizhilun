package com.lizl.demo.passwordbox.mvp.presenter

import com.lizl.demo.passwordbox.mvp.contract.SearchContract
import com.lizl.demo.passwordbox.util.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchPresenter(private var view: SearchContract.View?) : SearchContract.Presenter
{
    override fun search(keyWord: String)
    {
        GlobalScope.launch {

            val result = AppDatabase.instance.getAccountDao().search(keyWord)

            GlobalScope.launch(Dispatchers.Main) { view?.showSearchResult(result) }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}