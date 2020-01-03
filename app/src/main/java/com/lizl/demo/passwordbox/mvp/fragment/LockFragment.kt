package com.lizl.demo.passwordbox.mvp.fragment

import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.NumberKeyGridAdapter
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.customview.recylerviewitemdivider.GridDividerItemDecoration
import com.lizl.demo.passwordbox.mvp.contract.LockContract
import com.lizl.demo.passwordbox.mvp.presenter.LockPresenter
import com.lizl.demo.passwordbox.util.BiometricAuthenticationUtil
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_lock.*

/**
 * 锁定界面
 */
class LockFragment : BaseFragment<LockPresenter>(), LockContract.View
{

    override fun initPresenter() = LockPresenter(this)

    override fun getLayoutResId() = R.layout.fragment_lock

    override fun initView()
    {
        val numberKeyList: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
        val numberKeyGridAdapter = NumberKeyGridAdapter(numberKeyList)
        rv_number_key.layoutManager = GridLayoutManager(activity, 3)
        rv_number_key.addItemDecoration(GridDividerItemDecoration())
        rv_number_key.adapter = numberKeyGridAdapter

        numberKeyGridAdapter.setOnNumberKeyClickListener {

            when (it)
            {
                "*"  -> UiUtil.backToLauncher()
                "#"  -> tv_number.backspace()
                else -> tv_number.add(it)
            }

            presenter.checkInputPassword(tv_number.getInputText())
        }
    }

    override fun onStart()
    {
        super.onStart()

        if (BiometricAuthenticationUtil.isFingerprintSupport() && AppConfig.isFingerPrintLockOn())
        {
            tv_hint.text = getString(R.string.hint_verify_fingerprint_or_input_password)

            iv_lock.setOnClickListener { presenter.startFingerprintAuthentication() }
            presenter.startFingerprintAuthentication()

            iv_lock.setImageResource(R.mipmap.ic_fingerprint_on)
        }
        else
        {
            tv_hint.text = getString(R.string.hint_input_password)

            iv_lock.setImageResource(R.mipmap.ic_lock)
        }

        tv_number.text = ""
    }

    override fun onUnlockSuccess()
    {
        backToPreFragment()
    }

    override fun onBackPressed(): Boolean
    {
        UiUtil.backToLauncher()
        return true
    }
}