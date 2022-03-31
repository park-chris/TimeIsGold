package com.crystal.timeisgold.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.crystal.timeisgold.R
import com.crystal.timeisgold.Record
import java.util.*

const val DIALOG_DATE = "dialogDate"
const val REQUEST_DATE = 0

class RecordDetailFragment: Fragment(), DatePickerFragment.Callbacks {

    private lateinit var record: Record
    private lateinit var spinner: Spinner
    private lateinit var memoEditText: EditText
    private lateinit var dateButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        record = Record()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_record_detail, container ,false)

        spinner = view.findViewById(R.id.item_spinner)
        memoEditText = view.findViewById(R.id.memo_edit_text) as EditText
        dateButton = view.findViewById(R.id.date_button) as TextView

        dateButton.setOnClickListener {
            DatePickerFragment().apply {
                setTargetFragment(this@RecordDetailFragment, REQUEST_DATE)
                show(this@RecordDetailFragment.getParentFragmentManager(), DIALOG_DATE )
            }
        }


//        Spinner 드롭다운에서 띄울 항목
        val itemList: MutableList<String> = mutableListOf("기본", "운동", "공부")
//        Spinner Adapter 설정
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, R.id.text_first, itemList)
        spinner.adapter = adapter
//        Spinner 클릭 이벤트 설정
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(requireContext(), itemList[position], Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val memoWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                record.memo = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
            }

        }
        memoEditText.addTextChangedListener(memoWatcher)
    }

    companion object {
        fun newInstance(): RecordDetailFragment {
            return RecordDetailFragment()
        }
    }

    override fun onDateSelected(date: Date) {
        record.date = date
    }

}