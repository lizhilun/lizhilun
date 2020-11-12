package com.lizl.passwordbox.util

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.Utils
import com.lizl.passwordbox.R

object BiometricAuthenticationUtil
{
    private val mBiometricPrompt: BiometricPrompt by lazy {
        BiometricPrompt.Builder(Utils.getApp()).setTitle(Utils.getApp().getString(R.string.fingerprint_authentication_dialog_title))
            .setDescription(Utils.getApp().getString(R.string.fingerprint_authentication_dialog_description))
            .setNegativeButton(Utils.getApp().getString(R.string.cancel), ContextCompat.getMainExecutor(Utils.getApp()),
                    DialogInterface.OnClickListener { _, _ -> }).build()
    }

    private val mBiometricManager: BiometricManager by lazy { Utils.getApp().getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager }

    fun authenticate(authenticateCallback: BiometricPrompt.AuthenticationCallback)
    {
        mBiometricPrompt.authenticate(CancellationSignal(), ContextCompat.getMainExecutor(Utils.getApp()), authenticateCallback)
    }

    fun isFingerprintSupport() = mBiometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
}