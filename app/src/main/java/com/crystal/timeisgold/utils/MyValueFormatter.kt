package com.crystal.timeisgold.utils

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class MyValueFormatter: ValueFormatter() {

    private val format = DecimalFormat ("###,##")

    override fun getPointLabel(entry: Entry?): String {
        return format.format(entry?.y)
    }

}