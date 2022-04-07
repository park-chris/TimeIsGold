package com.crystal.timeisgold.utils

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.crystal.timeisgold.R
import com.crystal.timeisgold.fragments.RecordDetailFragment

private const val PREF_TAG = "pref_shared_item"

class ThemeCustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var lightButton: Button
    private lateinit var darkButton: Button
    private lateinit var listener: CustomDialogClickedListener

    fun start() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)    // 타이틀바 제거
        dialog.setContentView(R.layout.dialog_custom_theme)           // 다이얼로그에 사용할 xml 파일 호출
        dialog.setCancelable(true)            // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히도록 함

        lightButton = dialog.findViewById(R.id.light_button)
        darkButton = dialog.findViewById(R.id.dark_button)

        lightButton.setOnClickListener {
            listener.onOKClicked("light")
            dialog.dismiss()
        }

        darkButton.setOnClickListener {
            listener.onOKClicked("dark")
            dialog.dismiss()
        }

        dialog.show()

    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object : CustomDialogClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }

    interface CustomDialogClickedListener {
        fun onOKClicked(content: String)
    }
}