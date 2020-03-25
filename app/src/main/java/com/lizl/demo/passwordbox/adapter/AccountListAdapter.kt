package com.lizl.demo.passwordbox.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.model.AccountModel
import com.lizl.demo.passwordbox.util.PinyinUtil
import kotlinx.android.synthetic.main.item_account.view.*

class AccountListAdapter : BaseQuickAdapter<AccountModel, AccountListAdapter.ViewHolder>(R.layout.item_account)
{
    private var onAccountItemClickListener: ((AccountModel) -> Unit)? = null
    private var onAccountItemLongClickListener: ((AccountModel) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: AccountModel)
    {
        helper.bindViewHolder(item)
    }

    fun setData(accountList: List<AccountModel>)
    {
        setNewData(accountList.sortedBy { PinyinUtil.getPinyin(it.desPinyin) }.toMutableList())
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(accountModel: AccountModel)
        {
            val position = getItemPosition(accountModel)
            val firstLetter = PinyinUtil.getSortFirstLetter(accountModel.desPinyin)
            var showFirstLetter = true // 是否显示首字母标识
            if (position > 0)
            {
                val lastAccountModel = getItem(position - 1)
                val lastFirstLetter = PinyinUtil.getSortFirstLetter(lastAccountModel.desPinyin)
                // 与上一个item首字母相同的情况下不显示首字母
                if (firstLetter == lastFirstLetter)
                {
                    showFirstLetter = false
                }
            }

            with(itemView) {
                tv_description.text = accountModel.description
                tv_account.text = accountModel.account
                tv_first_letter.text = firstLetter.toString().toUpperCase()

                tv_first_letter.visibility = if (showFirstLetter) View.VISIBLE else View.INVISIBLE

                setOnClickListener { onAccountItemClickListener?.invoke(accountModel) }
                setOnLongClickListener {
                    onAccountItemLongClickListener?.invoke(accountModel)
                    true
                }
            }

        }
    }

    /**
     * 获取第一个首字母为选定字母的item的位置
     */
    fun getFirstLetterPosition(letter: Char): Int
    {
        return data.indexOfFirst { PinyinUtil.getSortFirstLetter(it.desPinyin) == letter.toLowerCase() }
    }

    fun setOnAccountItemClickListener(onAccountItemClickListener: (AccountModel) -> Unit)
    {
        this.onAccountItemClickListener = onAccountItemClickListener
    }

    fun setOnAccountItemLongClickListener(onAccountItemLongClickListener: (AccountModel) -> Unit)
    {
        this.onAccountItemLongClickListener = onAccountItemLongClickListener
    }
}