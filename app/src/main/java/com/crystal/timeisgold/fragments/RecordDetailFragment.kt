package com.crystal.timeisgold.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.crystal.timeisgold.utils.ContextUtil
import kotlin.collections.ArrayList

private const val ARG_RECORD_ID = "record_id"

private const val TAG = "RecordDetailFragment"

private const val PREF_TAG = "pref_shared_item"

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
    private var spinnerValue = ""
    private var itemList: ArrayList<String> = arrayListOf("default")

    private val recordViewModel: RecordViewModel by lazy {
        ViewModelProvider(this).get(RecordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        record = Record()
        val recordId: UUID = arguments?.getSerializable(ARG_RECORD_ID) as UUID

        recordViewModel.loadRecord(recordId)
    }

    override fun onStart() {
        super.onStart()
        setupEvents()

        val prefList = ContextUtil.getArrayPref(requireContext(), PREF_TAG)

        if (prefList.size > 0) {
            itemList = prefList
        }


//        Spinner Adapter 설정

        val adapter =
            ArrayAdapter(requireContext(), R.layout.custom_spinner_item, R.id.text_first, itemList)
        spinner.adapter = adapter


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
        ContextUtil.setArrayPref(requireContext(), PREF_TAG, itemList)
    }


    private fun setupEvents() {

        val memoWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                record.memo = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        memoEditText.addTextChangedListener(memoWatcher)


        saveButton.setOnClickListener {
            if (spinnerValue != "") {
                record.item = spinnerValue
            }
            recordViewModel.saveRecord(record)
            updateUI()
        }




//        Spinner 클릭 이벤트 설정
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerValue = itemList[position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

        }

    }


    private fun updateUI() {
        dateTextView.text = recordViewModel.convertTimestampToDate(record.date)
        memoEditText.setText(record.memo)
        startTimeTextView.text = recordViewModel.convertTimestampToTime(record.date)
        endTimeTextView.text = recordViewModel.getEndTime(record.date, record.durationTime)
        durationTimeTextView.text = recordViewModel.getDurationTime(record.durationTime)

        for (i in 0 until itemList.size) {
            if (itemList[i] == record.item) {
                spinner.setSelection(i)
            }
        }
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