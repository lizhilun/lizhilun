package com.lizl.demo.passwordbox.mvvm.activity

import android.hardware.biometrics.BiometricPrompt
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.NumberKeyGridAdapter
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.custom.view.recyclerview.GridDividerItemDecoration
import com.lizl.demo.passwordbox.mvvm.base.BaseActivity
import com.lizl.demo.passwordbox.util.BiometricAuthenticationUtil
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_lock.*

class LockActivity : BaseActivity(R.layout.fragment_lock)
{
    private val numberKeyGridAdapter = NumberKeyGridAdapter(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#"))

    override fun initView()
    {
        rv_number_key.addItemDecoration(GridDividerItemDecoration())
        rv_number_key.adapter = numberKeyGridAdapter

        numberKeyGridAdapter.setOnNumberKeyClickListener {
            when (it)
            {
                "*"  -> UiUtil.backToLauncher()
                "#"  -> tv_number.backspace()
                else -> tv_number.add(it)
            }

            if (tv_number.getInputText() == AppConfig.getAppLockPassword())
            {
                onUnlockSuccess()
            }
        }
    }

    override fun onStart()
    {
        super.onStart()

        if (BiometricAuthenticationUtil.isFingerprintSupport() && AppConfig.isFingerPrintLockOn())
        {
            tv_hint.text = getString(R.string.hint_verify_fingerprint_or_input_password)

            iv_lock.setOnClickListener { startFingerprintAuthentication() }
            startFingerprintAuthentication()

            iv_lock.setImageResource(R.drawable.ic_fingerprint)
        }
        else
        {
            tv_hint.text = getString(R.string.hint_input_password)

            iv_lock.setImageResource(R.drawable.ic_lock)
        }

        tv_number.text = ""
    }

    fun onUnlockSuccess()
    {
        lastAppStopTime = Long.MAX_VALUE
        if (ActivityUtils.getActivityList().size == 1)
        {
            turnToActivity(MainActivity::class.java)
        }
        finish()
    }

    override fun onBackPressed()
    {
        UiUtil.backToLauncher()
    }

    private fun startFingerprintAuthentication()
    {
        BiometricAuthenticationUtil.authenticate(object : BiometricPrompt.AuthenticationCallback()
        {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
            {
                Log.d(TAG, "onAuthenticationError() called with: errorCode = [$errorCode], errString = [$errString]")
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed()
            {
                Log.d(TAG, "onAuthenticationFailed")
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?)
            {
                Log.d(TAG, "onAuthenticationHelp() called with: helpCode = [$helpCode], helpString = [$helpString]")
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?)
            {
                Log.d(TAG, "onAuthenticationSucceeded() called with: result = [$result]")
                super.onAuthenticationSucceeded(result)
                onUnlockSuccess()
            }
        })
    }
}