package com.lizl.demo.passwordbox.mvvm.fragment

import android.content.Context
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.custom.view.quicksearchbar.OnQuickSideBarTouchListener
import com.lizl.demo.passwordbox.custom.view.recyclerview.ScrollTopLayoutManager
import com.lizl.demo.passwordbox.db.AppDatabase
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.mvvm.model.AccountModel
import com.lizl.demo.passwordbox.mvvm.model.OperationModel
import com.lizl.demo.passwordbox.mvvm.model.TitleBarBtnModel
import com.lizl.demo.passwordbox.util.AccountUtil
import com.lizl.demo.passwordbox.util.DialogUtil
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_account_list.*

/**
 * 账号列表界面
 */
class AccountListFragment : BaseFragment(R.layout.fragment_account_list), OnQuickSideBarTouchListener
{
    private val accountListAdapter = AccountListAdapter()

    override fun initView()
    {
        fab_add.setOnClickListener { turnToFragment(R.id.addAccountFragment) }

        val titleBtnList = mutableListOf<TitleBarBtnModel.BaseModel>().apply {
            add(TitleBarBtnModel.ImageBtnModel(R.drawable.ic_setting) { turnToFragment(R.id.settingFragment) })
            add(TitleBarBtnModel.ImageBtnModel(R.drawable.ic_search) { turnToFragment(R.id.searchFragment) })
        }
        ctb_title.setBtnList(titleBtnList)

        rv_password_list.layoutManager = ScrollTopLayoutManager(requireContext())
        rv_password_list.adapter = accountListAdapter
    }

    override fun initData()
    {
        AccountUtil.accountLiveData.observe(this, Observer { accountListAdapter.setData(it) })
    }

    override fun initListener()
    {
        accountListAdapter.setOnAccountItemClickListener { DialogUtil.showAccountInfoDialog(activity as Context, it) }

        accountListAdapter.setOnAccountItemLongClickListener { onAccountItemLongClick(it) }

        qsb_slide.setOnQuickSideBarTouchListener(this)
    }

    private fun onAccountItemLongClick(accountModel: AccountModel)
    {
        val operationList = mutableListOf<OperationModel>().apply {

            // 修改账号信息
            add(OperationModel(getString(R.string.modify_account_info)) { turnToFragment(R.id.addAccountFragment, accountModel) })

            // 删除账号
            add(OperationModel(getString(R.string.delete_account_item)) {
                AppDatabase.instance.getAccountDao().delete(accountModel)
            })
        }

        DialogUtil.showOperationListDialog(activity as Context, operationList)
    }

    override fun onLetterChanged(letter: String, position: Int, y: Float)
    {
        tv_select_letter.text = letter

        val firstLetterPosition = accountListAdapter.getFirstLetterPosition(letter[0])
        if (firstLetterPosition >= 0)
        {
            rv_password_list.smoothScrollToPosition(firstLetterPosition)
        }
    }

    override fun onLetterTouching(touching: Boolean)
    {
        tv_select_letter.isVisible = touching
    }

    override fun onBackPressed(): Boolean
    {
        UiUtil.backToLauncher()
        return true
    }
}