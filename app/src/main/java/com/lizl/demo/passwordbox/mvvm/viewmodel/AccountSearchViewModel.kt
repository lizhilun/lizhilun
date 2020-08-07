package com.lizl.demo.passwordbox.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lizl.demo.passwordbox.db.AppDatabase
import com.lizl.demo.passwordbox.mvvm.model.AccountModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AccountSearchViewModel : ViewModel()
{
    val searchResultLiveData = MutableLiveData<MutableList<AccountModel>>()

    fun search(keyWord: String)
    {
        GlobalScope.launch {
            searchResultLiveData.postValue(AppDatabase.instance.getAccountDao().search(keyWord))
        }
    }
}