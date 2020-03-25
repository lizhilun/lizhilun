package com.lizl.demo.passwordbox.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.demo.passwordbox.R
import kotlinx.android.synthetic.main.item_backup_file.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter : BaseQuickAdapter<File, BackupFileListAdapter.ViewHolder>(R.layout.item_backup_file)
{
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    private var onItemClickListener: ((File) -> Unit)? = null

    fun setData(fileList: List<File>)
    {
        setNewData(fileList.sortedByDescending { it.lastModified() }.toMutableList())
    }

    override fun convert(helper: ViewHolder, item: File)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(file: File)
        {
            itemView.tv_file_name.text = file.name

            itemView.tv_file_time.text = formatter.format(file.lastModified())
            itemView.setOnClickListener { onItemClickListener?.invoke(file) }
        }
    }

    fun setOnItemClickListener(onItemClickListener: ((File) -> Unit))
    {
        this.onItemClickListener = onItemClickListener
    }
}