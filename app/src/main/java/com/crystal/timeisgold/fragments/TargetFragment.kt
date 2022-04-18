package com.crystal.timeisgold.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.crystal.timeisgold.utils.ContextUtil
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

private const val TAG = "TargetFragment"

private const val PREF_TAG = "pref_shared_item"

class TargetFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var lineChart: LineChart
    private var itemList = arrayListOf("")

    private val recordViewModel: RecordViewModel by lazy {
        ViewModelProvider(this).get(RecordViewModel::class.java)
    }

    private val recordRepository = RecordRepository.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_target, container, false)

        pieChart = view.findViewById(R.id.pie_chart)
        lineChart = view.findViewById(R.id.line_chart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemList = ContextUtil.getArrayPref(requireContext(), PREF_TAG)
        recordViewModel.recordDailyLiveData.observe(
            viewLifecycleOwner,
            Observer { records ->
                records?.let {
                    if (records.isNotEmpty()) {
                        setLineChart(itemList, records)
                    }
                }
            }
        )


        setPieChart()
    }


    private fun setPieChart() {

        itemList = ContextUtil.getArrayPref(requireContext(), PREF_TAG)

        val entries = ArrayList<PieEntry>()

        runBlocking {
            Thread {
                var otherTime = 86400
                for (i in 0 until itemList.size) {
                    val itemTime = recordRepository.getTime(itemList[i])

                    if (itemTime <= 60) {
                        entries.add(PieEntry(100f, "null"))
                        break
                    } else {
                        otherTime -= itemTime
                        val result = itemTime.toFloat() % 86400 * 100
                        entries.add(PieEntry(result, itemList[i]))
                    }

                    if (i == itemList.size - 1) {
                        val result = otherTime.toFloat() % 86400 * 100
                        entries.add(PieEntry(result, "null"))

                    }
                }


                requireActivity().runOnUiThread() {

                    pieChart.setUsePercentValues(true)

                    val colorsItems = ArrayList<Int>()

                    for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
                    colorsItems.add(ColorTemplate.getHoloBlue())

                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorsItems
                        valueTextColor = Color.BLACK
                        valueTextSize = 16f
                    }

                    val pieData = PieData(pieDataSet)
                    pieChart.apply {
                        data = pieData
//            description : 해당 그래프 오른쪽 아래 그래프의 이름을 표시
                        description.isEnabled = false
//            isRotationEnable : 그래프의 회전 애니메이션으로 드래그를 통해 그래프를 회전판처럼 돌리는게 가능
                        isRotationEnabled = false
//            그래프 한 가운데에 들어갈 텍스트
                        centerText = "Today"
//            그래프 아이템의 이름의 색을 지정 (디폴트: 흰색)
                        setEntryLabelColor(Color.BLACK)
//            최초 그래프가 실행 시 동작하는 애니메이션, 12시를 시작으로 한바귀 돔
                        animateY(1400, Easing.EaseInOutQuad)
                        animate()
                    }

                }

            }.start()


        }
    }

    private fun setLineChart(items: List<String>, records: List<Record>) {

//        data 가져오기
        val priceList = mutableListOf<Int>()

//        labels
        val dailyList = mutableListOf<String>()

        for (i in 0 until 7) {
            val cal = Calendar.getInstance()

            cal.get(Calendar.YEAR)
            cal.get(Calendar.MONTH)
            cal.add(Calendar.DATE, i-7)

            val pattern = SimpleDateFormat("yyyy-MM-dd")
            val today = pattern.format(cal.time)
            dailyList.add(today)

            Log.d(TAG, "dailyList $i 추가 : $today")
        }


        val entries = ArrayList<Entry>()

        for (i in items.indices) {
            for (j in records.indices) {
                val record: Record = records[j]
                if (items[i] == record.item) {
                    for (d in dailyList.indices) {
                        if (record.created == dailyList[d]) {
                            Log.d(TAG, "${dailyList[d]}의 레코드는 : ${record.created}")
                            entries.add(Entry(d.toFloat(), record.durationTime.toFloat()))
                        } else {
                            entries.add(Entry(d.toFloat(), 0f))
                        }
                    }
                }
            }
        }

        val dataSet = LineDataSet(entries, "")
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(dailyList)

        lineChart.getTransformer(YAxis.AxisDependency.LEFT)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val data = LineData(dataSet)

        lineChart.data = data
        lineChart.invalidate()
    }
}