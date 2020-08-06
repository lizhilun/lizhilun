package com.lizl.demo.passwordbox.mvvm.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.BackupFileListAdapter
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.custom.function.getFileName
import com.lizl.demo.passwordbox.model.OperationModel
import com.lizl.demo.passwordbox.model.TitleBarBtnModel
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.mvvm.viewmodel.BackupFileViewModel
import com.lizl.demo.passwordbox.util.*
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
import java.io.File

class BackupFileListFragment : BaseFragment(R.layout.fragment_backup_file_list)
{
    companion object
    {
        private const val REQUEST_CODE_SELECT_BACKUP_FILE = 666
    }

    private val backupFileListAdapter = BackupFileListAdapter()

    private lateinit var backupFileViewModel: BackupFileViewModel

    override fun initView()
    {
        rv_file_list.adapter = backupFileListAdapter

        val titleBtnList = mutableListOf<TitleBarBtnModel.BaseModel>().apply {
            add(TitleBarBtnModel.ImageBtnModel(R.drawable.ic_folder) { selectBackupFile() })
        }
        ctb_title.setBtnList(titleBtnList)

        backupFileViewModel = AndroidViewModelFactory.getInstance(requireActivity().application).create(BackupFileViewModel::class.java)
    }

    override fun initData()
    {
        backupFileViewModel.backupFileLiveData.observe(this, Observer {
            backupFileListAdapter.setData(it)
        })

        backupFileViewModel.requestBackupFileList()
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
        backupFileListAdapter.setOnItemClickListener { onBackupFileItemClick(it) }
    }

    private fun selectBackupFile()
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        startActivityForResult(intent, REQUEST_CODE_SELECT_BACKUP_FILE)
    }

    private fun onBackupFileItemClick(file: File)
    {
        val operationList = mutableListOf<OperationModel>().apply {

            // 覆盖导入备份
            add(OperationModel(getString(R.string.import_backup_file_overlay)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_overlay),
                        getString(R.string.notify_restore_data_overlay)) { restoreData(Uri.fromFile(file), clearAllData = true) }
            })

            // 合并导入备份
            add(OperationModel(getString(R.string.import_backup_file_merge)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_merge),
                        getString(R.string.notify_restore_data_merge)) { restoreData(Uri.fromFile(file), clearAllData = false) }
            })

            // 删除备份文件
            add(OperationModel(getString(R.string.delete_backup_file)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.delete_backup_file),
                        getString(R.string.notify_delete_backup_file)) {
                    if (FileUtil.deleteFile(file.absolutePath))
                    {
                        backupFileListAdapter.remove(file)
                    }
                }
            })

            // 重命名备份文件
            add(OperationModel(getString(R.string.rename_backup_file)) {
                DialogUtil.showInputDialog(activity as Context, getString(R.string.rename_backup_file), getString(R.string.hint_rename_backup_file)) {
                    if (FileUtil.renameFile(file.absolutePath, it))
                    {
                        backupFileListAdapter.update(file)
                    }
                }
            })

            // 分享备份文件
            add(OperationModel(getString(R.string.share_backup_file)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.share_backup_file),
                        getString(R.string.notify_share_backup_file)) {
                    FileUtil.shareAllTypeFile(file)
                }
            })
        }

        DialogUtil.showOperationListDialog(activity as Context, operationList)
    }

    private fun restoreData(backupFileUri: Uri, password: String = AppConfig.getAppLockPassword(), clearAllData: Boolean)
    {
        BackupUtil.restoreData(backupFileUri, password, clearAllData) { result, failedReason ->
            if (result)
            {
                ToastUtil.showToast(R.string.notify_success_to_restore)
                backToPreFragment()
            }
            else
            {
                if (failedReason == Constant.DATA_RESTORE_FAILED_WRONG_PASSWORD)
                {
                    DialogUtil.showInputDialog(activity as Context, getString(R.string.input_encrypt_password),
                            getString(R.string.hint_input_encrypt_password)) { restoreData(backupFileUri, it, clearAllData) }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        Log.d(TAG, "onActivityResult() called with: requestCode = [$requestCode], resultCode = [$resultCode], data = [${data?.data?.path}]")
        when (requestCode)
        {
            REQUEST_CODE_SELECT_BACKUP_FILE ->
            {
                val fileUri = data?.data ?: return

                if (fileUri.getFileName(requireContext())?.endsWith(BackupUtil.fileSuffixName) != true)
                {
                    ToastUtil.showToast(R.string.notify_wrong_backup_file)
                    return
                }

                val operationList = mutableListOf<OperationModel>().apply {

                    // 覆盖导入备份
                    add(OperationModel(getString(R.string.import_backup_file_overlay)) {
                        DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_overlay),
                                getString(R.string.notify_restore_data_overlay)) { restoreData(fileUri, clearAllData = true) }
                    })

                    // 合并导入备份
                    add(OperationModel(getString(R.string.import_backup_file_merge)) {
                        DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_merge),
                                getString(R.string.notify_restore_data_merge)) { restoreData(fileUri, clearAllData = false) }
                    })
                }

                DialogUtil.showOperationListDialog(activity as Context, operationList)
            }
        }
    }
}