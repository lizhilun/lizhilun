package com.lizl.passwordbox.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.passwordbox.R
import kotlinx.android.synthetic.main.item_backup_file.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter : BaseQuickAdapter<File, BaseViewHolder>(R.layout.item_backup_file)
{
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun setData(fileList: List<File>)
    {
        setNewData(fileList.sortedByDescending { it.lastModified() }.toMutableList())
    }

    override fun convert(helper: BaseViewHolder, item: File)
    {
        with(helper.itemView) {
            tv_file_name.text = item.name
            tv_file_time.text = formatter.format(item.lastModified())
        }
    }

    fun setOnItemClickListener(onItemClickListener: ((File) -> Unit))
    {
        setOnItemClickListener { _, _, position ->
            getItemOrNull(position)?.let { onItemClickListener.invoke(it) }
        }
    }

    fun update(file: File)
    {
        val position = getItemPosition(file)
        if (position >= 0)
        {
            setData(position, file)
        }
    }
}