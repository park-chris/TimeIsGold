package com.crystal.timeisgold.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.crystal.timeisgold.R
import com.crystal.timeisgold.RecordRepository
import com.crystal.timeisgold.RecordViewModel
import com.crystal.timeisgold.utils.ContextUtil
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.runBlocking

private const val TAG = "TargetFragment"

private const val PREF_TAG = "pref_shared_item"

class TargetFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private var itemList = arrayListOf<String>("")


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



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                    if (itemTime != 0) {
                        otherTime -= itemTime
                        val result = itemTime.toFloat() % 86400 * 100
                        entries.add(PieEntry(result, itemList[i]))
                    }
                }

                val result = otherTime.toFloat() % 86400 * 100

                entries.add(PieEntry(result, "null"))

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
}