package com.lizl.demo.passwordbox

import android.app.Application
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.blankj.utilcode.util.Utils
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.util.Constant
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

        PinyinUtil.initResource()

        checkFingerprintStatus()
    }

    /**
     * 检查指纹识别状态
     */
    private fun checkFingerprintStatus()
    {
        // 只有在未检测过情况下才检测
        if (AppConfig.getAppFingerprintStatus() != Constant.APP_FINGERPRINT_STATUS_NOT_DETECT)
        {
            return
        }

        try
        {
            val mFingerprintManager = FingerprintManagerCompat.from(instance)
            if (mFingerprintManager.isHardwareDetected)
            {
                AppConfig.setAppFingerprintStatus(Constant.APP_FINGERPRINT_STATUS_SUPPORT)
            }
            else
            {
                AppConfig.setAppFingerprintStatus(Constant.APP_FINGERPRINT_STATUS_NOT_SUPPORT)
            }
        }
        catch (e: ClassNotFoundException)
        {
            AppConfig.setAppFingerprintStatus(Constant.APP_FINGERPRINT_STATUS_NOT_SUPPORT)
        }
    }

    companion object
    {
        lateinit var instance: UiApplication
    }
}