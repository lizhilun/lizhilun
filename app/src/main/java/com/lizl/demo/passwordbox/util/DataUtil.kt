package com.lizl.demo.passwordbox.util

import android.content.Context
import android.text.TextUtils
import com.lizl.demo.passwordbox.db.DbManager
import com.lizl.demo.passwordbox.model.AccountModel

class DataUtil private constructor()
{
    private val accountList = mutableListOf<AccountModel>()

    init
    {
        queryAll(UiApplication.getInstance())
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

    /**
     * 添加数据至数据库

     * @param context
     * *
     * @param stu
     */
    fun insertData(context: Context?, model: AccountModel)
    {
        if (!accountList.contains(model))
        {
            accountList.add(model)
        }
        DbManager.getInstance(context!!)?.getDaoSession(context)?.accountModelDao?.insert(model)
    }


    /**
     * 将数据实体通过事务添加至数据库

     * @param context
     * *
     * @param list
     */
    fun insertData(context: Context?, list: List<AccountModel>?)
    {
        if (null == list || list.isEmpty())
        {
            return
        }
        for (model in list)
        {
            if (!accountList.contains(model))
            {
                accountList.add(model)
            }
        }
        DbManager.getInstance(context!!)?.getDaoSession(context)?.accountModelDao?.insertInTx(list)
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；

     * @param context
     * *
     * @param model
     */
    fun saveData(context: Context?, model: AccountModel)
    {
        if (!accountList.contains(model))
        {
            accountList.add(model)
        }
        DbManager.getInstance(context!!)?.getDaoSession(context)?.accountModelDao?.save(model)
    }

    /**
     * 删除数据至数据库

     * @param context
     * *
     * @param model 删除具体内容
     */
    fun deleteData(context: Context?, model: AccountModel)
    {
        accountList.remove(model)
        DbManager.getInstance(context!!)?.getDaoSession(context)?.accountModelDao?.delete(model)
    }

    /**
     * 删除全部数据
     */
    fun deleteAllData(context: Context?)
    {
        accountList.clear()
        DbManager.getInstance(context!!)?.getDaoSession(context)?.accountModelDao?.deleteAll()
    }

    /**
     * 更新数据库
     */
    fun updateData(context: Context?, model: AccountModel)
    {
        DbManager.getInstance(context!!)?.getDaoSession(context)?.accountModelDao?.update(model)
    }


    /**
     * 查询所有数据
     */
    fun queryAll(context: Context?): MutableList<AccountModel>?
    {
        if (accountList.isEmpty())
        {
            val builder = DbManager.getInstance(context!!)?.getDaoSession(context)?.accountModelDao?.queryBuilder()
            accountList.addAll(builder?.build()?.list()!!)
        }
        return accountList
    }

    /**
     * 关键词搜索
     */
    fun searchByKeyword(keyWord: String): List<AccountModel>
    {
        val resultList = mutableListOf<AccountModel>()

        if (!TextUtils.isEmpty(keyWord))
        {
            for (accountModel in accountList)
            {
                if (accountModel.description.contains(keyWord) || accountModel.account.contains(keyWord) || accountModel.desPinyin.contains(keyWord))
                {
                    resultList.add(accountModel)
                }
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