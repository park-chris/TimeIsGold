package com.crystal.timeisgold.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.crystal.timeisgold.R
import com.crystal.timeisgold.Record
import com.crystal.timeisgold.RecordViewModel
import com.crystal.timeisgold.utils.UIUtil
import java.util.*


private const val TAG = "StopWatchFragment"

class StopWatchFragment : Fragment() {

    interface Callbacks {
        fun onRecordSelected(recordId: UUID)
    }

    private var callbacks: Callbacks? = null

    private val recordViewModel: RecordViewModel by lazy {
        ViewModelProvider(this).get(RecordViewModel::class.java)
    }

    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null


    private lateinit var startButton: Button
    private lateinit var saveButton: Button
    private lateinit var resetButton: Button

    private lateinit var timeTextView: TextView
    private var date: Date = Date(System.currentTimeMillis())




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stop_watch, container, false)

        startButton = view.findViewById(R.id.start_button)
        saveButton = view.findViewById(R.id.save_button)
        resetButton = view.findViewById(R.id.reset_button)
        timeTextView = view.findViewById(R.id.time_text_view)

        setValues()
        setupEvents()

        return view

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?

    }

    private fun setValues() {

        if (time != 0) {
            timeTextView.text = UIUtil.getDurationTime(time)
        } else {
            timeTextView.text = "00: 00: 00"
        }
    }

    private fun setupEvents() {

        startButton.setOnClickListener {
            isRunning = !isRunning
            if (time == 0) {
                date = Date(System.currentTimeMillis())
            }
            if (isRunning) start() else pause()
        }

        resetButton.setOnClickListener {
            reset()
        }


        saveButton.setOnClickListener {
            pause()
            val record = Record()
            recordViewModel.addRecord(record)
            record.date = date
            record.durationTime = time
            recordViewModel.saveRecord(record)
            callbacks?.onRecordSelected(record.id)
        }


    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    private fun start() {

        startButton.setText(R.string.stop_record)

//        timer() 호출
        timerTask = kotlin.concurrent.timer(period = 1000) {
            time++                  // period=1000, 0.01초마다 time를 1씩 증가

            val timerHandler = Handler(Looper.getMainLooper()) {
                Handler(Looper.getMainLooper()).post {
                    timeTextView.text = UIUtil.getDurationTime(time)
                }
            }
            timerHandler.obtainMessage().sendToTarget()

        }

    }


    private fun pause() {

        startButton.setText(R.string.start_record)
        timerTask?.cancel();

    }

    private fun reset() {
        timerTask?.cancel()     // timerTask가 nulldl dkslfkaus cancle() 호출

        time = 0        // 시간저장 변수 초기화
        isRunning = false
        startButton.setText(R.string.start_record)
        timeTextView.text = "00: 00: 00"

    }



}