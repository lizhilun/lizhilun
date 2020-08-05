package com.lizl.demo.passwordbox.mvvm.fragment

import android.content.Context
import android.text.InputFilter
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.AccountListAdapter
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.model.OperationModel
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.mvvm.viewmodel.AccountSearchViewModel
import com.lizl.demo.passwordbox.db.AppDatabase
import com.lizl.demo.passwordbox.util.DialogUtil
import com.lizl.demo.passwordbox.util.UiUtil
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * 搜索界面
 */
class SearchFragment : BaseFragment(R.layout.fragment_search)
{
    private val accountListAdapter = AccountListAdapter()
    private lateinit var accountSearchViewModel: AccountSearchViewModel

    override fun initView()
    {
        iv_cancel.visibility = View.GONE

        et_search.filters = arrayOf(InputFilter.LengthFilter(20), UiUtil.getNoWrapOrSpaceFilter())

        rv_result_list.layoutManager = LinearLayoutManager(activity)
        rv_result_list.adapter = accountListAdapter

        accountSearchViewModel = AndroidViewModelFactory.getInstance(requireActivity().application).create(AccountSearchViewModel::class.java)

        UiUtil.showSoftKeyboard(et_search)
    }

    override fun initData()
    {
        accountSearchViewModel.searchResultLiveData.observe(this, Observer {
            accountListAdapter.setData(it)
        })
    }

    override fun initListener()
    {
        iv_back.setOnClickListener { onBackButtonClick() }
        iv_cancel.setOnClickListener { et_search.setText("") }

        et_search.addTextChangedListener {
            accountSearchViewModel.search(it.toString())
            iv_cancel.isVisible = it.toString().isNotEmpty()
        }

        et_search.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER)
            {
                if (event.action == KeyEvent.ACTION_DOWN)
                {
                    UiUtil.hideSoftKeyboard(et_search)
                }
                true
            }
            false
        }

        accountListAdapter.setOnAccountItemClickListener { DialogUtil.showAccountInfoDialog(activity as Context, it) }
        accountListAdapter.setOnAccountItemLongClickListener { onAccountItemLongClick(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            android.R.id.home -> onBackButtonClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onAccountItemLongClick(accountModel: AccountModel)
    {
        val operationList = mutableListOf<OperationModel>().apply {

            add(OperationModel(getString(R.string.modify_account_info)) {
                turnToFragment(R.id.addAccountFragment, accountModel)
            })

            add(OperationModel(getString(R.string.delete_account_item)) {
                AppDatabase.instance.getAccountDao().delete(accountModel)
                accountListAdapter.remove(accountModel)
            })
        }

        DialogUtil.showOperationListDialog(activity as Context, operationList)
    }

    override fun onStop()
    {
        super.onStop()

        UiUtil.hideSoftKeyboard(et_search)
    }

    private fun onBackButtonClick()
    {
        backToPreFragment()
    }

    override fun onBackPressed() = false
}