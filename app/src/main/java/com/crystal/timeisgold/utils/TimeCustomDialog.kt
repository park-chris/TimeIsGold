package com.crystal.timeisgold.utils

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import com.crystal.timeisgold.R
import com.crystal.timeisgold.fragments.RecordDetailFragment

private const val PREF_TAG = "pref_shared_item"

class TimeCustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var hourTimePicker: NumberPicker
    private lateinit var minTimePicker: NumberPicker
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var listener: CustomDialogClickedListener
    private var timeList = arrayListOf<Int>(0, 0)

    fun start() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)    // 타이틀바 제거
        dialog.setContentView(R.layout.dialog_custom_time)           // 다이얼로그에 사용할 xml 파일 호출
        dialog.setCancelable(true)            // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지않도록 함

        hourTimePicker = dialog.findViewById(R.id.hour_number_picker)
        minTimePicker = dialog.findViewById(R.id.min_number_picker)
        okButton = dialog.findViewById(R.id.ok_button)
        cancelButton = dialog.findViewById(R.id.cancel_button)

        hourTimePicker.minValue = 0
        hourTimePicker.maxValue = 100
        minTimePicker.minValue = 0
        minTimePicker.maxValue = 59

        okButton.setOnClickListener {
            timeList[0] = hourTimePicker.value
            timeList[1] = minTimePicker.value
            listener.onOKClicked(timeList)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    fun setOnOKClickedListener(listener: (List<Int>) -> Unit) {
        this.listener = object : CustomDialogClickedListener {
            override fun onOKClicked(content: List<Int>) {
                listener(content)
            }
        }
    }

    interface CustomDialogClickedListener {
        fun onOKClicked(content: List<Int>)
    }


}