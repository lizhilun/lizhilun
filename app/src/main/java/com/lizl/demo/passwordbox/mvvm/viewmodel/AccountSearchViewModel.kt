package com.lizl.demo.passwordbox.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.util.AppDatabase
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