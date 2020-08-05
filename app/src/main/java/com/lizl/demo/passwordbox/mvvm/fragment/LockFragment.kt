package com.lizl.demo.passwordbox.mvvm.fragment

import android.hardware.biometrics.BiometricPrompt
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.NumberKeyGridAdapter
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.custom.view.recyclerview.GridDividerItemDecoration
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.util.BiometricAuthenticationUtil
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_lock.*

/**
 * 锁定界面
 */
class LockFragment : BaseFragment(R.layout.fragment_lock)
{
    private val numberKeyGridAdapter = NumberKeyGridAdapter(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#"))

    override fun initView()
    {
        rv_number_key.layoutManager = GridLayoutManager(activity, 3)
        rv_number_key.addItemDecoration(GridDividerItemDecoration())
        rv_number_key.adapter = numberKeyGridAdapter
    }

    override fun initListener()
    {
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

            iv_lock.setImageResource(R.mipmap.ic_fingerprint_on)
        }
        else
        {
            tv_hint.text = getString(R.string.hint_input_password)

            iv_lock.setImageResource(R.mipmap.ic_lock)
        }

        tv_number.text = ""
    }

    fun onUnlockSuccess()
    {
        backToPreFragment()
    }

    override fun onBackPressed(): Boolean
    {
        UiUtil.backToLauncher()
        return true
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