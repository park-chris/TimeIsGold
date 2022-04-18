package com.crystal.timeisgold.fragments

import android.graphics.Color
import android.icu.text.FormattedValue
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColor
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
import com.crystal.timeisgold.utils.MyValueFormatter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "TargetFragment"

private const val PREF_TAG = "pref_shared_item"

class TargetFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var itemRecyclerView: RecyclerView
    private var itemList = arrayListOf("")
    private var adapter: ItemAdapter? = ItemAdapter(emptyList(), emptyList())


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
        itemRecyclerView = view.findViewById(R.id.target_recycler_view)
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        itemRecyclerView.adapter = adapter

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
                        updateUI(itemList, records)
                    }
                }
            }
        )
        setPieChart()
    }

    private inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: String

        private val itemText: TextView = itemView.findViewById(R.id.item_text)
        private val lineChart: LineChart = itemView.findViewById(R.id.line_chart)

        fun setLineChart(item: String, records: List<Record>) {
            itemText.text = item

//        labels
            val dailyList = mutableListOf<String>()
            val xLabels = mutableListOf<String>()

            for (i in 0 until 7) {
                val cal = Calendar.getInstance()

                cal.get(Calendar.YEAR)
                cal.get(Calendar.MONTH)
                cal.add(Calendar.DATE, i - 7)

                val pattern = SimpleDateFormat("yyyy-MM-dd")
                val today = pattern.format(cal.time)
                val labelPattern = SimpleDateFormat("MM-dd")
                xLabels.add(labelPattern.format(cal.time))
                dailyList.add(today)
            }


            val entries = ArrayList<Entry>()

            for (i in dailyList.indices) {
                var sum = 0

                for (j in records.indices) {
                    val record: Record = records[j]
                    if (item == record.item && dailyList[i] == record.created) {
                        sum += record.durationTime
                    }
                }

                sum /= 3600

                if (sum > 0) {
                    entries.add(Entry(i.toFloat(), sum.toFloat()))
                } else {
                    entries.add(Entry(i.toFloat(), 0f))
                }
            }

            val dataSet = LineDataSet(entries, "")

            dataSet.valueFormatter = MyValueFormatter()

//            x 축 값
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)

            lineChart.getTransformer(YAxis.AxisDependency.LEFT)
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM


            lineChart.axisLeft.setLabelCount(7, true)
//            y축 값
            lineChart.axisLeft.axisMaximum = 24f
            lineChart.axisLeft.axisMinimum = 0f
            lineChart.axisLeft.granularity = 1f

            lineChart.axisRight.setDrawLabels(false)
            lineChart.axisRight.setDrawAxisLine(false)
            lineChart.axisRight.setDrawGridLines(false)
            lineChart.description.text = "최근 날짜"
            lineChart.description.textColor = Color.GRAY
            lineChart.axisLeft.textColor = Color.GRAY
            lineChart.xAxis.textColor = Color.GRAY

            val data = LineData(dataSet)

            lineChart.data = data
            lineChart.invalidate()
        }
    }

    private inner class ItemAdapter(var items: List<String>, var records: List<Record>): RecyclerView.Adapter<ItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = layoutInflater.inflate(R.layout.list_item_graph, parent, false)
            return ItemHolder(view)
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            val item = items[position]
            holder.setLineChart(item, records)
        }

        override fun getItemCount() = items.size

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
                        val result = itemTime.toFloat() / 86400 * 100
                        entries.add(PieEntry(result, itemList[i]))
                    }

                    if (i == itemList.size - 1) {
                        val result = otherTime.toFloat() / 86400 * 100
                        entries.add(PieEntry(result, "null"))

                    }
                }


                requireActivity().runOnUiThread() {

                    pieChart.setUsePercentValues(true)

                    val colorsItems = ArrayList<Int>()

                    for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
                    colorsItems.add(ColorTemplate.getHoloBlue())

                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorsItems
                        valueTextColor = Color.BLACK
                        valueTextSize = 16f
                    }

                    pieDataSet.valueFormatter = PercentFormatter()

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

    private fun updateUI(items: List<String>, records: List<Record>) {
        adapter = ItemAdapter(items, records)
        itemRecyclerView.adapter = adapter
    }

}