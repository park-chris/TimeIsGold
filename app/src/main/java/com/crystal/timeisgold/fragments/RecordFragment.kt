package com.crystal.timeisgold.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crystal.timeisgold.R
import com.crystal.timeisgold.Record
import com.crystal.timeisgold.RecordRepository
import com.crystal.timeisgold.RecordViewModel
import com.crystal.timeisgold.utils.UIUtil
import java.util.*

private const val TAG = "RecordListFragment"

class RecordFragment : Fragment() {

//    MainActivity에서 구현할 인터페이스
    interface Callbacks {
        fun onRecordSelected(recordId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var recordRecyclerView: RecyclerView
    private var adapter: RecordAdapter? = RecordAdapter(emptyList())

    private val recordViewModel: RecordViewModel by lazy {
        ViewModelProvider(this).get(RecordViewModel::class.java)
    }

    private val recordRepository = RecordRepository.get()
    val recordListLiveData = recordRepository.getRecords()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)

        recordRecyclerView = view.findViewById(R.id.record_recycler_view) as RecyclerView
        recordRecyclerView.layoutManager = LinearLayoutManager(context)
        recordRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordViewModel.recordListLiveData.observe(
            viewLifecycleOwner,
            Observer { records ->
                records?.let {
                    updateUI(records)
                }
            }
        )

    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private inner class RecordHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var record: Record

        private val startTimeTextView: TextView = itemView.findViewById(R.id.start_time_text)
        private val endTimeTextView: TextView = itemView.findViewById(R.id.end_time_text)
        private val durationTimeTextView: TextView = itemView.findViewById(R.id.duration_time_text)
        private val itemTextView: TextView = itemView.findViewById(R.id.item_text)
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(record: Record) {
            this.record = record
            startTimeTextView.text = "시작 시간  ${UIUtil.getStartTime(this.record.date)}"
            endTimeTextView.text = "종료 시간  ${UIUtil.getEndTime(this.record.date, record.durationTime)}"
            durationTimeTextView.text = "소요 시간  ${UIUtil.getDurationTime(this.record.durationTime)}"
            itemTextView.text = this.record.item
            dateTextView.text = UIUtil.convertTimestampToDate(this.record.date)
        }

        override fun onClick(v: View?) {
            callbacks?.onRecordSelected(record.id)
        }
    }

    private inner class RecordAdapter(var records: List<Record>): RecyclerView.Adapter<RecordHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
            val view = layoutInflater.inflate(R.layout.list_item_record, parent, false)
            return RecordHolder(view)
        }

        override fun onBindViewHolder(holder: RecordHolder, position: Int) {
            val record = records[position]
            holder.bind(record)
        }

        override fun getItemCount() = records.size

    }

    private fun updateUI(records: List<Record>) {
        adapter = RecordAdapter(records)
        recordRecyclerView.adapter = adapter
    }

}