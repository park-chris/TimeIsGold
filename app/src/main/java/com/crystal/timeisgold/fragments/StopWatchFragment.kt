package com.crystal.timeisgold.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.crystal.timeisgold.R
import java.util.*

private const val TAG = "StopWatchFragment"


class StopWatchFragment : Fragment() {

    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null


    private lateinit var startButton: Button
    private lateinit var saveButton: Button
    private lateinit var resetButton: Button

    private lateinit var timeTextView: TextView


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


    private fun setValues() {
        timeTextView.text = "0: 0: 0"
    }

    private fun setupEvents() {

        startButton.setOnClickListener {
            isRunning = !isRunning

            if (isRunning) start() else pause()
        }

        resetButton.setOnClickListener {
            reset()
        }

    }


    private fun start() {

        startButton.setText(R.string.stop_record)

//        timer() 호출
        timerTask = kotlin.concurrent.timer(period = 1000) {
            time++                  // period=1000, 0.01초마다 time를 1씩 증가
            Log.d(TAG, "타임: $time")

            var hour = 0
            var min = 0
            var sec = 0

            if (time >= 3600) {
                hour = time / 3600
                val extra = time % 3600
                min = extra / 60
                sec = extra % 60

            }

            min = time / 60
            sec = time % 60

            val handler: Handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    timeTextView.text = "$hour: $min: $sec"
                }
            }
            handler.obtainMessage().sendToTarget()

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
        timeTextView.text = "0: 0: 0"

    }


}