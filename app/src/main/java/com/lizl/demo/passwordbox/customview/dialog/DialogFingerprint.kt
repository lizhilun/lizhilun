package com.lizl.demo.passwordbox.customview.dialog

import android.content.Context
import android.content.DialogInterface
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.util.Log
import com.lizl.demo.passwordbox.R
import kotlinx.android.synthetic.main.dialog_fingerprint.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 用于指纹识别的Dialog
 */
class DialogFingerprint(context: Context, private val fingerprintUnlockCallBack: FingerprintUnlockCallBack) : BaseDialog(context), DialogInterface.OnDismissListener
{
    private var mFingerprintManager: FingerprintManagerCompat = FingerprintManagerCompat.from(context)
    private lateinit var cancellationSignal: android.support.v4.os.CancellationSignal

    override fun getDialogLayoutResId(): Int
    {
        return R.layout.dialog_fingerprint
    }

    override fun initView()
    {
        this.setOnDismissListener(this)
        tv_cancel.setOnClickListener { dismiss() }
    }

    override fun show()
    {
        super.show()

        iv_lock.isSelected = false
        tv_result.text = context.getString(R.string.notify_verify_fingerprint_to_unlock)

        cancellationSignal = android.support.v4.os.CancellationSignal()
        mFingerprintManager.authenticate(null, 0, cancellationSignal, MyCallBack(), null)
    }

    override fun onDismiss(dialog: DialogInterface?)
    {
        cancellationSignal.cancel()
    }

    inner class MyCallBack : FingerprintManagerCompat.AuthenticationCallback()
    {
        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?)
        {
            Log.d(TAG, "onAuthenticationError: " + errString!!)

            tv_result.text = context.getText(R.string.notify_fingerprint_authentication_error)

            // 延时1s让Dialog消失
            GlobalScope.launch(Dispatchers.Main){
                delay(1000)
                dismiss()
                fingerprintUnlockCallBack.onFingerprintUnLockFailed()
            }
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        override fun onAuthenticationFailed()
        {
            Log.d(TAG, "onAuthenticationFailed")

            tv_result.text = context.getText(R.string.notify_fingerprint_authentication_failed)
        }

        override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?)
        {
            Log.d(TAG, "onAuthenticationHelp: " + helpString!!)
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?)
        {
            Log.d(TAG, "onAuthenticationSucceeded")
            iv_lock.isSelected = true

            tv_result.text = context.getText(R.string.notify_fingerprint_authentication_success)

            // 延时1s让Dialog消失
            GlobalScope.launch(Dispatchers.Main){
                delay(1000)
                dismiss()
                fingerprintUnlockCallBack.onFingerprintUnlockSuccess()
            }
        }
    }

    interface FingerprintUnlockCallBack
    {
        fun onFingerprintUnLockFailed()

        fun onFingerprintUnlockSuccess()
    }
}