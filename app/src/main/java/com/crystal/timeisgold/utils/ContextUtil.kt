package com.crystal.timeisgold.utils

import android.content.Context
import android.util.Log
import org.json.JSONArray

class ContextUtil {
    companion object {
        private val prefName = "timeIsGoldPref"

        fun setArrayPref(context: Context, tag: String, list: ArrayList<String>) {

            var jsonArr = JSONArray()
            for (i in list) {
                jsonArr.put(i)
            }

            var result = jsonArr.toString()

            val pref = context.getSharedPreferences (prefName, Context.MODE_PRIVATE) ?: return
            with(pref.edit()) {
                putString(tag, result)
                commit()
            }

        }

        fun getArrayPref(context: Context, tag: String): ArrayList<String> {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

            val getString = pref.getString(tag, "")
            var resultArr: ArrayList<String> = ArrayList()

            if (getString == "") {
                resultArr.add("default")
                return resultArr
            }

            var arrJson = JSONArray(getString)

            for (i in 0 until arrJson.length()) {
                resultArr.add(arrJson.optString(i))
            }

            return resultArr
        }
    }
}