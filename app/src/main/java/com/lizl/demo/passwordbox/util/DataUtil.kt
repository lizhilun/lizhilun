package com.lizl.demo.passwordbox.util

import android.content.Context
import android.text.TextUtils
import com.lizl.demo.passwordbox.model.AccountModel
import java.util.regex.Pattern

class DataUtil private constructor()
{
    private val accountList = mutableListOf<AccountModel>()

    init
    {
        queryAll(UiApplication.instance)
    }

    private object Holder
    {
        val instance = DataUtil()
    }

    companion object
    {
        fun getInstance(): DataUtil
        {
            return Holder.instance
        }
    }

    fun saveData(model: AccountModel)
    {
        if (!accountList.contains(model))
        {
            accountList.add(model)
        }
        AppDatabase.instance.getAccountDao().insert(model)
    }

    fun deleteData(model: AccountModel)
    {
        accountList.remove(model)
        AppDatabase.instance.getAccountDao().delete(model)
    }

    fun deleteAllData()
    {
        accountList.clear()
        AppDatabase.instance.getAccountDao().deleteAll()
    }

    /**
     * 查询所有数据
     */
    fun queryAll(context: Context?): MutableList<AccountModel>?
    {
        if (accountList.isEmpty())
        {
            accountList.addAll(AppDatabase.instance.getAccountDao().getAllDiary())
        }
        return accountList
    }

    /**
     * 关键词搜索
     */
    fun searchByKeyword(keyWord: String): List<AccountModel>
    {
        if (TextUtils.isEmpty(keyWord))
        {
            return emptyList()
        }

        val resultList = mutableListOf<AccountModel>()

        val regStr = StringBuilder()
        regStr.append(".*")
        for (str in keyWord.toCharArray())
        {
            regStr.append(str).append(".*")
        }
        val pattern = Pattern.compile(regStr.toString(), Pattern.CASE_INSENSITIVE)

        for (accountModel in accountList)
        {
            if (pattern.matcher(accountModel.description).matches() || pattern.matcher(accountModel.account).matches() || pattern.matcher(
                            accountModel.desPinyin).matches())
            {
                resultList.add(accountModel)
            }
        }

        return resultList
    }

    /**
     * 通过描述和账号寻找已经存在的账号item
     */
    fun getAccountByDesAndAccount(description: String, account: String): AccountModel?
    {
        for (accountModel in accountList)
        {
            if (accountModel.description == description && accountModel.account == account)
            {
                return accountModel
            }
        }
        return null
    }
}