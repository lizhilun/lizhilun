package com.lizl.demo.passwordbox.fragment

import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.NumberKeyGridAdapter
import com.lizl.demo.passwordbox.customview.recylerviewitemdivider.GridDividerItemDecoration
import com.lizl.demo.passwordbox.util.UiApplication
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_lock.*

/**
 * 锁定界面
 */
class LockFragment : BaseFragment(), NumberKeyGridAdapter.OnNumberKeyClickListener
{
    private var inputPassword = ""

    private lateinit var mBiometricPrompt: BiometricPrompt

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_lock
    }

    override fun initView()
    {
        val numberKeyList: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
        val numberKeyGridAdapter = NumberKeyGridAdapter(numberKeyList, this)
        rv_number_key.layoutManager = GridLayoutManager(activity, 3)
        rv_number_key.addItemDecoration(GridDividerItemDecoration())
        rv_number_key.adapter = numberKeyGridAdapter

        mBiometricPrompt = BiometricPrompt.Builder(activity)
                .setTitle(getString(R.string.fingerprint_authentication_dialog_title))
                .setDescription(getString(R.string.fingerprint_authentication_dialog_description))
                .setNegativeButton(getString(R.string.cancel), ContextCompat.getMainExecutor(context), DialogInterface.OnClickListener {
                    _, _ -> Log.d(TAG, "cancel button click")})
                .build()
    }

    override fun onResume()
    {
        super.onResume()

        if (UiApplication.instance.getAppConfig().isAppFingerprintSupport() && UiApplication.instance.getAppConfig().isFingerPrintLockOn())
        {
            tv_hint.text = getString(R.string.hint_verify_fingerprint_or_input_password)

            iv_lock.setOnClickListener { showFingerprintDialog() }
            showFingerprintDialog()

            iv_lock.setImageResource(R.mipmap.ic_fingerprint_on)
        }
        else
        {
            tv_hint.text = getString(R.string.hint_input_password)

            iv_lock.setImageResource(R.mipmap.ic_lock)
        }

        tv_number.text = ""
        inputPassword = ""
    }

    private fun showFingerprintDialog()
    {
        mBiometricPrompt.authenticate(CancellationSignal(), context!!.mainExecutor, object : BiometricPrompt.AuthenticationCallback()
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

    override fun onNumberKeyClick(keyValue: String)
    {
        when (keyValue)
        {
            "*"  -> UiUtil.backToLauncher()
            "#"  ->
            {
                if (inputPassword.isNotEmpty())
                {
                    inputPassword = inputPassword.substring(0, inputPassword.length - 1)
                }
            }
            else -> inputPassword += keyValue
        }

        var numberStr = ""
        for (i in 1..inputPassword.length)
        {
            numberStr += "@"
        }

        tv_number.text = numberStr

        if (inputPassword == UiApplication.instance.getAppConfig().getAppLockPassword())
        {
            onUnlockSuccess()
        }
    }

    private fun onUnlockSuccess()
    {
        backToPreFragment()
    }

    override fun onBackPressed(): Boolean
    {
        UiUtil.backToLauncher()
        return true
    }
}