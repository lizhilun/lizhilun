package com.lizl.demo.passwordbox.mvp.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.customview.ScrollTopLayoutManager
import com.lizl.demo.passwordbox.customview.quicksearchbar.OnQuickSideBarTouchListener
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.model.TitleBarBtnItem
import com.lizl.demo.passwordbox.mvp.presenter.EmptyPresenter
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.DataUtil
import com.lizl.demo.passwordbox.util.DialogUtil
import kotlinx.android.synthetic.main.fragment_account_list.*

/**
 * 账号列表界面
 */
class AccountListFragment : BaseFragment<EmptyPresenter>(), AccountListAdapter.OnItemClickListener, OnQuickSideBarTouchListener
{
    private lateinit var accountListAdapter: AccountListAdapter

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_account_list
    }

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        fab_add.setOnClickListener { turnToFragment(R.id.addAccountFragment) }

        val titleBtnList = mutableListOf<TitleBarBtnItem.BaseItem>().apply {
            add(TitleBarBtnItem.ImageBtnItem(R.mipmap.ic_setting) { turnToFragment(R.id.settingFragment) })
            add(TitleBarBtnItem.ImageBtnItem(R.mipmap.ic_search) { turnToFragment(R.id.searchFragment) })
        }
        ctb_title.setBtnList(titleBtnList)

        qsb_slide.setOnQuickSideBarTouchListener(this)

        getData()
    }

    private fun getData()
    {
        val accountList: MutableList<AccountModel> = DataUtil.getInstance().queryAll()

        accountListAdapter = AccountListAdapter(accountList, this)
        rv_password_list.layoutManager = ScrollTopLayoutManager(activity as Context)
        rv_password_list.adapter = accountListAdapter
    }

    override fun onAccountItemClick(accountModel: AccountModel)
    {
        DialogUtil.showAccountInfoDialog(activity as Context, accountModel)
    }

    override fun onAccountItemLongClick(accountModel: AccountModel): Boolean
    {
        val operationList = mutableListOf<OperationItem>()

        // 修改账号信息
        operationList.add(OperationItem(getString(R.string.modify_account_info)) {
            val bundle = Bundle()
            bundle.putSerializable(Constant.BUNDLE_DATA, accountModel)
            turnToFragment(R.id.addAccountFragment, bundle)
        })

        // 删除账号
        operationList.add(OperationItem(getString(R.string.delete_account_item)) {
            DataUtil.getInstance().deleteData(accountModel)
            getData()
        })

        DialogUtil.showOperationListDialog(activity as Context, operationList)
        return true
    }

    override fun onBackPressed(): Boolean
    {
        return false
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
        if (touching) tv_select_letter.visibility = View.VISIBLE else tv_select_letter.visibility = View.GONE
    }
}