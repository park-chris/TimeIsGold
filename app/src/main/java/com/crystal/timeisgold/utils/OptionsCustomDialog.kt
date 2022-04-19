package com.crystal.timeisgold.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crystal.timeisgold.R

private const val PREF_TAG = "pref_shared_item"

class OptionsCustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var closeButton: Button
    private lateinit var optionsRecyclerView: RecyclerView
    private val prefList = ContextUtil.getArrayPref(context, PREF_TAG)
    private var adapter: OptionsAdapter? = OptionsAdapter(prefList)


    fun start(context: Context) {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)    // 타이틀바 제거
        dialog.setContentView(R.layout.dialog_custom_item_setting)           // 다이얼로그에 사용할 xml 파일 호출
        dialog.setCancelable(false)            // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히도록 함

        closeButton = dialog.findViewById(R.id.close_button)
        optionsRecyclerView = dialog.findViewById(R.id.options_recycler_view)
        optionsRecyclerView.layoutManager = LinearLayoutManager(context)
        optionsRecyclerView.adapter = adapter

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private inner class OptionsHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var option: String

        private val optionTextView: TextView = itemView.findViewById(R.id.option_text_view)
        private val removeButton: Button = itemView.findViewById(R.id.remove_button)

        fun bind(option: String) {


            this.option = option
            optionTextView.text = this.option

            removeButton.setOnClickListener {
                val context: Context = itemView.context
                val alert = AlertDialog.Builder(context)
                alert.setCancelable(false)
                alert.setMessage("이 기록을 삭제하시겠습니까?")
                alert.setPositiveButton("삭제", DialogInterface.OnClickListener{ _, _ ->
                    prefList.remove(this.option)
                    ContextUtil.setArrayPref(context, PREF_TAG ,prefList)
                    ContextUtil.getArrayPref(context, PREF_TAG)
                    optionsRecyclerView.adapter = adapter
                })
                alert.setNegativeButton("취소", null)
                alert.show()
            }
        }

    }

    private inner class OptionsAdapter(var options: List<String>): RecyclerView.Adapter<OptionsHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsHolder {

            val context: Context = parent.context
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view: View = inflater.inflate(R.layout.list_item_options, parent, false)

            return OptionsHolder(view)
        }

        override fun onBindViewHolder(holder: OptionsHolder, position: Int) {
            val option = options[position]
            holder.bind(option)
        }

        override fun getItemCount() = options.size

    }


}