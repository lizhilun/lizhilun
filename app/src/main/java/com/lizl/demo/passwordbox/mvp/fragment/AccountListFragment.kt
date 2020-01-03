package com.lizl.demo.passwordbox.mvp.fragment

import android.content.Context
import android.os.Bundle
import androidx.core.view.isVisible
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.customview.other.ScrollTopLayoutManager
import com.lizl.demo.passwordbox.customview.quicksearchbar.OnQuickSideBarTouchListener
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.model.TitleBarBtnItem
import com.lizl.demo.passwordbox.mvp.contract.AccountListContract
import com.lizl.demo.passwordbox.mvp.presenter.AccountListPresenter
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.DataUtil
import com.lizl.demo.passwordbox.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_account_list.*

/**
 * 账号列表界面
 */
class AccountListFragment : BaseFragment<AccountListPresenter>(), AccountListContract.View, OnQuickSideBarTouchListener
{
    private lateinit var accountListAdapter: AccountListAdapter

    override fun getLayoutResId() = R.layout.fragment_account_list

    override fun initPresenter() = AccountListPresenter(this)

    override fun initView()
    {
        fab_add.setOnClickListener { turnToFragment(R.id.addAccountFragment) }

        val titleBtnList = mutableListOf<TitleBarBtnItem.BaseItem>().apply {
            add(TitleBarBtnItem.ImageBtnItem(R.mipmap.ic_setting) { turnToFragment(R.id.settingFragment) })
            add(TitleBarBtnItem.ImageBtnItem(R.mipmap.ic_search) { turnToFragment(R.id.searchFragment) })
        }
        ctb_title.setBtnList(titleBtnList)

        accountListAdapter = AccountListAdapter()
        rv_password_list.layoutManager = ScrollTopLayoutManager(activity as Context)
        rv_password_list.adapter = accountListAdapter

        accountListAdapter.setOnAccountItemClickListener { DialogUtil.showAccountInfoDialog(activity as Context, it) }

        accountListAdapter.setOnAccountItemLongClickListener { onAccountItemLongClick(it) }

        qsb_slide.setOnQuickSideBarTouchListener(this)

        presenter.getAllAccounts()
    }

    override fun showAccountList(accountList: List<AccountModel>)
    {
        accountListAdapter.setData(accountList)
    }

    private fun onAccountItemLongClick(accountModel: AccountModel): Boolean
    {
        val operationList = mutableListOf<OperationItem>().apply {

            // 修改账号信息
            add(OperationItem(getString(R.string.modify_account_info)) { turnToFragment(R.id.addAccountFragment, accountModel) })

            // 删除账号
            add(OperationItem(getString(R.string.delete_account_item)) {
                DataUtil.getInstance().deleteData(accountModel)
                presenter.getAllAccounts()
            })
        }

        DialogUtil.showOperationListDialog(activity as Context, operationList)
        return true
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

    override fun onBackPressed() = false
}