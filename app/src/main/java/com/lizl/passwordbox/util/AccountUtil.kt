package com.lizl.passwordbox.util

import com.lizl.passwordbox.db.AppDatabase

object AccountUtil
{
    val accountLiveData = AppDatabase.instance.getAccountDao().getAllAccountLiveData()
}