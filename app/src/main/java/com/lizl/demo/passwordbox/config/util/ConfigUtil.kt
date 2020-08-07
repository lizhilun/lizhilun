package com.lizl.demo.passwordbox.config.util

import com.blankj.utilcode.util.SPUtils
import com.lizl.demo.passwordbox.config.annotation.BooleanConfig
import com.lizl.demo.passwordbox.config.annotation.LongConfig
import com.lizl.demo.passwordbox.config.annotation.StringConfig
import com.lizl.demo.passwordbox.config.constant.ConfigConstant

object ConfigUtil
{
    private val defaultConfigMap = HashMap<String, Any>()

    fun initConfig()
    {
        ConfigConstant::class.java.declaredMethods.forEach { method ->
            val configKey = method.name.replace("\$annotations", "")
            method.annotations.forEach {
                when (it)
                {
                    is BooleanConfig -> defaultConfigMap[configKey] = it.defaultValue
                    is LongConfig    -> defaultConfigMap[configKey] = it.defaultValue
                    is StringConfig  -> defaultConfigMap[configKey] = it.defaultValue
                }
            }
        }
    }

    fun getBoolean(configKey: String): Boolean
    {
        return SPUtils.getInstance().getBoolean(configKey, getBooleanDefault(configKey))
    }

    fun getLong(configKey: String): Long
    {
        return SPUtils.getInstance().getLong(configKey, getLongDefault(configKey))
    }

    fun getString(configKey: String): String
    {
        return SPUtils.getInstance().getString(configKey, getStringDefault(configKey))
    }

    fun set(configKey: String, value: Any)
    {
        when (value)
        {
            is Boolean -> SPUtils.getInstance().put(configKey, value)
            is String  -> SPUtils.getInstance().put(configKey, value)
            is Long    -> SPUtils.getInstance().put(configKey, value)
        }
    }

    private fun getBooleanDefault(configKey: String): Boolean
    {
        val defaultValue = defaultConfigMap[configKey]
        if (defaultValue is Boolean)
        {
            return defaultValue
        }
        return false
    }

    private fun getLongDefault(configKey: String): Long
    {
        val defaultValue = defaultConfigMap[configKey]
        if (defaultValue is Long)
        {
            return defaultValue
        }
        return 0L
    }

    private fun getStringDefault(configKey: String): String
    {
        val defaultValue = defaultConfigMap[configKey]
        if (defaultValue is String)
        {
            return defaultValue
        }
        return ""
    }
}