package com.crystal.timeisgold.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.crystal.timeisgold.R
import com.crystal.timeisgold.utils.ContextUtil
import com.crystal.timeisgold.utils.CustomDialog
import com.crystal.timeisgold.utils.ThemeCustomDialog
import com.crystal.timeisgold.utils.ThemeManager

private const val TAG = "SettingsFragment"

class SettingsFragment: Fragment() {

    interface Callbacks {
        fun onThemeSelected(theme: String)
    }

    private var callbacks: Callbacks? = null

    private lateinit var languageTextView: TextView
    private lateinit var themeTextView: TextView
    private lateinit var itemSettingsTextView: TextView
    private val themeList: ArrayList<String> = arrayListOf("Light Theme", "Dark Theme")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        languageTextView = view.findViewById(R.id.language_text_view)
        themeTextView = view.findViewById(R.id.theme_text_view)
        itemSettingsTextView = view.findViewById(R.id.item_settings_text_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEvents()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun setEvents() {

        languageTextView.setOnClickListener {
            Toast.makeText(requireContext(), "현재 한국어로만 사용가능합니다.", Toast.LENGTH_SHORT).show()
        }

        themeTextView.setOnClickListener {

            val dlg = ThemeCustomDialog(requireContext())
            dlg.setOnOKClickedListener { content ->
                Log.d(TAG, "content : $content")
                callbacks?.onThemeSelected(content)
            }
            dlg.start()

        }

    }

}