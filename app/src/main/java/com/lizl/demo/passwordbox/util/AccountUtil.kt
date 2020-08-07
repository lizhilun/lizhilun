package com.lizl.demo.passwordbox.util

import com.lizl.demo.passwordbox.db.AppDatabase

object AccountUtil
{
    val accountLiveData = AppDatabase.instance.getAccountDao().getAllAccountLiveData()
}