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

class CustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var itemEditText: EditText
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var listener: CustomDialogClickedListener

    fun start() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)    // 타이틀바 제거
        dialog.setContentView(R.layout.dialog_custom)           // 다이얼로그에 사용할 xml 파일 호출
        dialog.setCancelable(false)            // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지않도록 함

        itemEditText = dialog.findViewById(R.id.item_edit_text)
        okButton = dialog.findViewById(R.id.ok_button)
        cancelButton = dialog.findViewById(R.id.cancel_button)

        okButton.setOnClickListener {
            Log.d("RecordDetailFragment", "current EditText : ${itemEditText.text}")
            listener.onOKClicked("${itemEditText.text}")
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
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