package com.lizl.demo.passwordbox.model.settingmodel

open class SettingBaseItem(open val settingName: String?, open val settingKey: String? = null)
{
    constructor() : this(null, null)

    constructor(settingName: String?) : this(settingName, null)
}