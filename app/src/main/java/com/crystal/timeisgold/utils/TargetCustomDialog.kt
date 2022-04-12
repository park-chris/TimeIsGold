package com.crystal.timeisgold.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.*
import com.crystal.timeisgold.R
import kotlin.collections.ArrayList

private const val PREF_TAG = "pref_shared_item"

class TargetCustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private lateinit var dateSpinner: Spinner
    private lateinit var itemSpinner: Spinner
    private lateinit var timeText: TextView
    private lateinit var listener: CustomDialogClickedListener
    private val itemList: ArrayList<String> = arrayListOf("1일", "7일", "30일")
    private var addList: ArrayList<String> = arrayListOf("하루", "없음", "0")
    private val prefList = ContextUtil.getArrayPref(context, PREF_TAG)


    fun start(context: Context) {

        val dateAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, itemList)

        prefList.add("추가하기")
        val itemAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, prefList)


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)    // 타이틀바 제거
        dialog.setContentView(R.layout.dialog_custom_target_add)           // 다이얼로그에 사용할 xml 파일 호출
        dialog.setCancelable(false)            // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히도록 함

        addButton = dialog.findViewById(R.id.add_button)
        cancelButton = dialog.findViewById(R.id.cancel_button)
        dateSpinner = dialog.findViewById(R.id.date_spinner)
        itemSpinner = dialog.findViewById(R.id.item_spinner)
        timeText = dialog.findViewById(R.id.time_text)

        dateSpinner.adapter = dateAdapter

        dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                addList[0] = itemList[position]
                dateSpinner.setSelection(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        itemSpinner.adapter = itemAdapter

        itemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if ("추가하기" == prefList[position]) {
                    val dlg = CustomDialog(context)
                    dlg.setOnOKClickedListener { content ->
                        prefList.remove("추가하기")
                        prefList.add(content)
                        ContextUtil.setArrayPref(context, PREF_TAG, prefList)
                        prefList.add("추가하기")
                        val adapter = ArrayAdapter(context,android.R.layout.simple_spinner_item, prefList)
                        itemSpinner.adapter = adapter
                    }
                    dlg.start()
                }
                addList[1] = prefList[position]
                itemSpinner.setSelection(position)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        timeText.setOnClickListener {
            val dlg = TimeCustomDialog(context)
            dlg.setOnOKClickedListener { list ->
                val h = list[0]
                val m = list[1]
                val time = h * 3600 + m * 60
                addList[2] = time.toString()
                timeText.text = UIUtil.getDurationTime(time)
            }
            dlg.start()

        }

        addButton.setOnClickListener {
            listener.onOKClicked(addList)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    fun setOnOKClickedListener(listener: (List<String>) -> Unit) {
        this.listener = object : CustomDialogClickedListener {
            override fun onOKClicked(content: List<String>) {
                listener(content)
            }
        }
    }

    interface CustomDialogClickedListener {
        fun onOKClicked(content: List<String>)
    }


}