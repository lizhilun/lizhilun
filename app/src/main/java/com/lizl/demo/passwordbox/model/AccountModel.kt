package com.lizl.demo.passwordbox.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "accounts")
class AccountModel : Serializable, Comparable<AccountModel>
{
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    @ColumnInfo
    var description: String = "" // 账号描述

    @ColumnInfo
    var account: String = "" // 账号

    @ColumnInfo
    var password: String = "" // 密码

    @ColumnInfo
    var desPinyin: String = ""

    override fun compareTo(other: AccountModel): Int
    {
        var firstLetter = this.desPinyin.toCharArray()[0]
        var toFirstLetter = other.desPinyin.toCharArray()[0]

        if (!Character.isLetter(firstLetter))
        {
            firstLetter = '#'
        }

        if (!Character.isLetter(toFirstLetter))
        {
            toFirstLetter = '#'
        }

        if (firstLetter < toFirstLetter)
        {
            return -1
        }
        else if (firstLetter > toFirstLetter)
        {
            return 1
        }
        return 0
    }
}