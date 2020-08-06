package com.lizl.demo.passwordbox

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.lizl.demo.passwordbox.util.BackupUtil
import com.lizl.demo.passwordbox.util.PinyinUtil

class UiApplication : Application()
{
    init
    {
        instance = this
    }

    override fun onCreate()
    {
        super.onCreate()

        Utils.init(this)

        BackupUtil.init()

        PinyinUtil.initResource()
    }

    companion object
    {
        lateinit var instance: UiApplication
    }
}