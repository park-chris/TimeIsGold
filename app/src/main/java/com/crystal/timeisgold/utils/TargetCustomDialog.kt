package com.crystal.timeisgold.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.crystal.timeisgold.R

private const val PREF_TAG = "pref_shared_item"

class TargetCustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var listener: CustomDialogClickedListener
    private val prefList = ContextUtil.getArrayPref(context, PREF_TAG)


    fun start(context: Context) {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)    // 타이틀바 제거
        dialog.setContentView(R.layout.dialog_custom_item_setting)           // 다이얼로그에 사용할 xml 파일 호출
        dialog.setCancelable(false)            // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히도록 함



        dialog.show()

    }

    fun setOnOKClickedListener() {
        this.listener = object : CustomDialogClickedListener {
            override fun onOKClicked() {
            }
        }
    }

    interface CustomDialogClickedListener {
        fun onOKClicked()
    }


}