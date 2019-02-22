package com.lizl.demo.passwordbox.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.customview.ScrollTopLayoutManager
import com.lizl.demo.passwordbox.customview.dialog.DialogAccountInfo
import com.lizl.demo.passwordbox.customview.dialog.DialogOperationList
import com.lizl.demo.passwordbox.customview.quicksearchbar.OnQuickSideBarTouchListener
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.DataUtil
import kotlinx.android.synthetic.main.fragment_account_list.*

/**
 * 账号列表界面
 */
class AccountListFragment : BaseFragment(), AccountListAdapter.OnItemClickListener, OnQuickSideBarTouchListener
{
    private var dialogAccountInfo: DialogAccountInfo? = null
    private var dialogOperationList: DialogOperationList? = null
    private lateinit var accountListAdapter: AccountListAdapter

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_account_list
    }

    override fun initView()
    {
        fab_add.setOnClickListener { turnToFragment(AddAccountFragment()) }
        iv_search.setOnClickListener { onSearchButtonClick() }
        iv_setting.setOnClickListener { onSettingButtonClick() }
        qsb_slide.setOnQuickSideBarTouchListener(this)

        getData()
    }

    private fun getData()
    {
        val accountList: MutableList<AccountModel> = DataUtil.getInstance().queryAll(activity)!!

        accountListAdapter = AccountListAdapter(activity as Context, accountList, this)
        rv_password_list.layoutManager = ScrollTopLayoutManager(activity as Context)
        rv_password_list.adapter = accountListAdapter
    }

    private fun onSearchButtonClick()
    {
        turnToFragment(SearchFragment())
    }

    private fun onSettingButtonClick()
    {
        turnToFragment(SettingFragment())
    }

    override fun onAccountItemClick(accountModel: AccountModel)
    {
        dialogAccountInfo = DialogAccountInfo(activity as Context, accountModel)
        dialogAccountInfo?.show()
    }

    override fun onAccountItemLongClick(accountModel: AccountModel): Boolean
    {
        val operationList = mutableListOf<OperationItem>()

        // 修改账号信息
        operationList.add(OperationItem(getString(R.string.modify_account_info), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                val bundle = Bundle()
                bundle.putParcelable(Constant.BUNDLE_DATA, accountModel)
                turnToFragment(AddAccountFragment(), bundle)
            }
        }))

        // 删除账号
        operationList.add(OperationItem(getString(R.string.delete_account_item), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                DataUtil.getInstance().deleteData(activity, accountModel)
                getData()
            }
        }))

        dialogOperationList = DialogOperationList(activity as Context, operationList)
        dialogOperationList?.show()
        return true
    }

    override fun onPause()
    {
        super.onPause()

        dialogAccountInfo?.dismiss()
        dialogOperationList?.dismiss()
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