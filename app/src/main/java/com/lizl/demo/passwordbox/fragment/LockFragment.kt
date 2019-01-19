package com.lizl.demo.passwordbox.fragment

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.NumberKeyGridAdapter
import com.lizl.demo.passwordbox.customview.dialog.DialogFingerprint
import com.lizl.demo.passwordbox.customview.recylerviewitemdivider.GridDividerItemDecoration
import com.lizl.demo.passwordbox.util.UiApplication
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_lock.*

/**
 * 锁定界面
 */
class LockFragment : BaseFragment(), DialogFingerprint.FingerprintUnlockCallBack, NumberKeyGridAdapter.OnNumberKeyClickListener
{
    private var dialogFingerprint: DialogFingerprint? = null
    private var inputPassword = ""

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_lock
    }

    override fun initView()
    {
        val numberKeyList: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
        val numberKeyGridAdapter = NumberKeyGridAdapter(activity as Context, numberKeyList, this)
        rv_number_key.layoutManager = GridLayoutManager(activity, 3)
        rv_number_key.addItemDecoration(GridDividerItemDecoration())
        rv_number_key.adapter = numberKeyGridAdapter
    }

    override fun onResume()
    {
        super.onResume()

        if (UiApplication.getInstance().getAppConfig().isAppFingerprintSupport() && UiApplication.getInstance().getAppConfig().isFingerPrintLockOn())
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

    override fun onPause()
    {
        super.onPause()

        dialogFingerprint?.dismiss()
    }

    private fun showFingerprintDialog()
    {
        if (dialogFingerprint == null)
        {
            dialogFingerprint = DialogFingerprint(activity as Context, this)
        }
        dialogFingerprint?.show()
    }

    override fun onNumberKeyClick(keyValue: String)
    {
        if (keyValue == "*")
        {
            UiUtil.backToLauncher()
        }
        else if (keyValue == "#")
        {
            if (inputPassword.isNotEmpty())
            {
                inputPassword = inputPassword.substring(0, inputPassword.length - 1)
            }
        }
        else
        {
            inputPassword += keyValue
        }

        var numberStr = ""
        for (i in 1..inputPassword.length)
        {
            numberStr += "@"
        }

        tv_number.text = numberStr

        if (inputPassword == UiApplication.getInstance().getAppConfig().getAppLockPassword())
        {
            onUnlockSuccess()
        }
    }

    override fun onFingerprintUnLockFailed()
    {
        Log.d(TAG, "onLockFailed")
    }

    override fun onFingerprintUnlockSuccess()
    {
        Log.d(TAG, "onFingerprintUnlockSuccess")

        onUnlockSuccess()
    }

    private fun onUnlockSuccess()
    {
        if (UiApplication.getTopFragment() == null)
        {
            turnToFragment(AccountListFragment())
        }
        else
        {
            backToPreFragment()
        }
    }

    override fun onBackPressed(): Boolean
    {
        if (dialogFingerprint != null && dialogFingerprint!!.isShowing)
        {
            dialogFingerprint?.dismiss()
            return true
        }
        UiUtil.backToLauncher()
        return true
    }
}