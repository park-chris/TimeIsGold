package com.crystal.timeisgold.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.crystal.timeisgold.R
import com.crystal.timeisgold.utils.*
import java.util.*

class SettingsFragment: Fragment() {

    interface Callbacks {
        fun onThemeSelected(theme: String)
    }

    private var callbacks: Callbacks? = null

    private lateinit var languageTextView: TextView
    private lateinit var themeTextView: TextView
    private lateinit var itemSettingsTextView: TextView
    private lateinit var tipTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        languageTextView = view.findViewById(R.id.language_text_view)
        themeTextView = view.findViewById(R.id.theme_text_view)
        itemSettingsTextView = view.findViewById(R.id.item_settings_text_view)
        tipTextView = view.findViewById(R.id.tip_text_view)


        val savedTheme = ContextUtil.getSavedTheme(requireContext())

        if (savedTheme) {
            themeTextView.text = "Dark"
        } else {
            themeTextView.text = "Light"
        }

        val lang = getSystemLanguage(requireContext())
        if (lang == "ko") {
            languageTextView.text = lang
        } else {
            languageTextView.text = "us"
        }

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

        themeTextView.setOnClickListener {


            val dlg = ThemeCustomDialog(requireContext())
            dlg.setOnOKClickedListener { content ->
                callbacks?.onThemeSelected(content)
                themeTextView.text = content
                if (content == "dark") {
                ContextUtil.setSavedTheme(requireContext(), true)
                } else {
                ContextUtil.setSavedTheme(requireContext(), false)
                }
            }
            dlg.start()

        }

        itemSettingsTextView.setOnClickListener {
            val dlg = OptionsCustomDialog(requireContext())
            dlg.start(requireContext())
        }

        tipTextView.setOnClickListener {
            val dlg = TipCustomDialog(requireContext())
            dlg.start()
        }

    }

//    시스템 언어설정 가져오기
    private fun getSystemLanguage(context: Context): String {
        val systemLocale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            systemLocale = context.resources.configuration.locales.get(0)
        } else {
            systemLocale = context.resources.configuration.locale
        }
        return systemLocale.language
    }

}