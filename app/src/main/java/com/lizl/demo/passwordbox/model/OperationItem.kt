package com.lizl.demo.passwordbox.model

class OperationItem(var operationName: String, val operationItemCallBack: OperationItemCallBack)
{
    interface OperationItemCallBack
    {
        fun onOperationExecute()
    }
}