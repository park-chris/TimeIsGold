package com.crystal.timeisgold.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.crystal.timeisgold.R


class TipCustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var closeButton: Button


    fun start() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)    // 타이틀바 제거
        dialog.setContentView(R.layout.dialog_custom_tip)           // 다이얼로그에 사용할 xml 파일 호출
        dialog.setCancelable(true)            // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지않도록 함

        closeButton = dialog.findViewById(R.id.close_button)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

}