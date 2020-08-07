package com.lizl.demo.passwordbox.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lizl.demo.passwordbox.UiApplication
import com.lizl.demo.passwordbox.db.dao.AccountDao
import com.lizl.demo.passwordbox.mvvm.model.AccountModel

@Database(entities = [AccountModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    private object Singleton
    {
        val singleton: AppDatabase = Room.databaseBuilder(UiApplication.instance, AppDatabase::class.java, "Account.db").allowMainThreadQueries().build()
    }

    companion object
    {
        val instance = Singleton.singleton
    }

    abstract fun getAccountDao(): AccountDao
}