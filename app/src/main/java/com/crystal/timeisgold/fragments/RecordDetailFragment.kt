package com.crystal.timeisgold.fragments

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.crystal.timeisgold.R
import com.crystal.timeisgold.Record
import com.crystal.timeisgold.RecordViewModel
import java.util.*
import androidx.lifecycle.Observer

private const val ARG_RECORD_ID = "record_id"

class RecordDetailFragment : Fragment() {

    private lateinit var record: Record
    private lateinit var spinner: Spinner
    private lateinit var memoEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var durationTimeTextView: TextView
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView


    private val recordViewModel: RecordViewModel by lazy {
        ViewModelProvider(this).get(RecordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        record = Record()
        val recordId: UUID = arguments?.getSerializable(ARG_RECORD_ID) as UUID
        recordViewModel.loadRecord(recordId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record_detail, container, false)

        spinner = view.findViewById(R.id.item_spinner)
        memoEditText = view.findViewById(R.id.memo_edit_text)
        dateTextView = view.findViewById(R.id.date_text_view)
        durationTimeTextView = view.findViewById(R.id.duration_time_text)
        cancelButton = view.findViewById(R.id.cancel_button)
        saveButton = view.findViewById(R.id.save_button)
        startTimeTextView = view.findViewById(R.id.start_time_text)
        endTimeTextView = view.findViewById(R.id.end_time_text)


//        Spinner 드롭다운에서 띄울 항목
        val itemList: MutableList<String> = mutableListOf("기본", "운동", "공부")
//        Spinner Adapter 설정
        val adapter =
            ArrayAdapter(requireContext(), R.layout.custom_spinner_item, R.id.text_first, itemList)
        spinner.adapter = adapter
//        Spinner 클릭 이벤트 설정
/*        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(requireContext(), itemList[position], Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordViewModel.recordLiveDate.observe(
            viewLifecycleOwner,
            Observer { record ->
                record?.let {
                    this.record = record
                    updateUI()
                }

            }
        )

    }

    override fun onStop() {
        super.onStop()
        recordViewModel.saveRecord(record)
        updateUI()
    }




    private fun updateUI() {
        dateTextView.text = recordViewModel.convertTimestampToDate(record.date)
        memoEditText.setText(record.memo)
        startTimeTextView.text = recordViewModel.convertTimestampToTime(record.date)
        endTimeTextView.text = recordViewModel.getEndTime(record.date, record.durationTime)
        durationTimeTextView.text = recordViewModel.getDurationTime(record.durationTime)
    }

    companion object {
        fun newInstance(id: UUID): RecordDetailFragment {

            val args = Bundle().apply {
                putSerializable(ARG_RECORD_ID, id)
            }

            return RecordDetailFragment().apply {
                arguments = args
            }

        }


    }


}