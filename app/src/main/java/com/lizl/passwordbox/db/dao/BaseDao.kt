package com.lizl.passwordbox.db.dao

import androidx.room.*

@Dao
interface BaseDao<T>
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(elements: MutableList<T>)

    @Delete
    fun delete(element: T)

    @Update
    fun update(element: T)

    @Delete
    fun deleteList(elements: MutableList<T>)
}