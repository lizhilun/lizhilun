package com.lizl.demo.passwordbox.dao

import androidx.room.Dao
import androidx.room.Query
import com.lizl.demo.passwordbox.model.AccountModel

@Dao
interface AccountDao : BaseDao<AccountModel>
{
    @Query("select * from accounts")
    fun getAllDiary(): MutableList<AccountModel>

    @Query("DELETE FROM accounts")
    fun deleteAll()
}