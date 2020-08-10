package com.lizl.demo.passwordbox.adapter

import androidx.core.view.isInvisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.mvvm.model.AccountModel
import com.lizl.demo.passwordbox.util.PinyinUtil
import kotlinx.android.synthetic.main.item_account.view.*

class AccountListAdapter : BaseQuickAdapter<AccountModel, BaseViewHolder>(R.layout.item_account)
{
    override fun convert(helper: BaseViewHolder, item: AccountModel)
    {
        with(helper.itemView) {
            val position = getItemPosition(item)
            val firstLetter = PinyinUtil.getSortFirstLetter(item.desPinyin)
            val showFirstLetter = when
            {
                position > 0 ->
                {
                    // 与上一个item首字母相同的情况下不显示首字母
                    firstLetter != PinyinUtil.getSortFirstLetter(getItem(position - 1).desPinyin)
                }
                else         -> true
            }

            tv_description.text = item.description
            tv_account.text = item.account
            tv_first_letter.text = firstLetter.toString().toUpperCase(java.util.Locale.getDefault())

            tv_first_letter.isInvisible = !showFirstLetter
        }
    }

    fun setData(accountList: List<AccountModel>)
    {
        setNewData(accountList.sortedBy { PinyinUtil.getPinyin(it.desPinyin) }.toMutableList())
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
        setOnItemClickListener { _, _, position ->
            getItemOrNull(position)?.let { onAccountItemClickListener.invoke(it) }
        }
    }

    fun setOnAccountItemLongClickListener(onAccountItemLongClickListener: (AccountModel) -> Unit)
    {
        setOnItemLongClickListener { _, _, position ->
            getItemOrNull(position)?.let { onAccountItemLongClickListener.invoke(it) }
            true
        }
    }
}