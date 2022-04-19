package com.crystal.timeisgold

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.crystal.timeisgold.databinding.ActivityMainBinding
import com.crystal.timeisgold.fragments.*
import com.crystal.timeisgold.utils.ContextUtil
import com.crystal.timeisgold.utils.ThemeManager
import java.util.*

lateinit var binding: ActivityMainBinding

private const val TAG_STOP_WATCH = "stop_watch_fragment"
private const val TAG_RECORD = "record_fragment"
private const val TAG_TARGET = "target_fragment"
private const val TAG_SETTINGS = "settings_fragment"
private const val TAG_RECORD_DETAIL = " record_detail_fragment"

private const val KEY_TAG = "current_fragment"

class MainActivity : AppCompatActivity(), RecordFragment.Callbacks, StopWatchFragment.Callbacks, SettingsFragment.Callbacks {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val savedTheme = ContextUtil.getSavedTheme(this)

        if (savedTheme) {
            ThemeManager.applyTheme("dark")
        }

        val currentFragment = savedInstanceState?.getString(KEY_TAG, null) ?: null

        if (currentFragment != null) {
            when (currentFragment) {
                "StopWatchFragment" -> setFragment(TAG_STOP_WATCH, StopWatchFragment())
                "RecordFragment" -> setFragment(TAG_RECORD, RecordFragment())
                "TargetFragment" -> setFragment(TAG_TARGET, TargetFragment())

                "SettingsFragment" -> setFragment(TAG_SETTINGS, SettingsFragment())
            }
            true
        }   else {
            setFragment(TAG_STOP_WATCH, StopWatchFragment())
        }

        setupEvents()

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val mainFM  = supportFragmentManager.findFragmentById(R.id.fragment_container)
        savedInstanceState.putString(KEY_TAG, mainFM.toString())
    }




    //    프래그먼트 컨트롤 함수
    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

//        트랜잭션에 tag로 전달된 fragment가 없을 경우 add
        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.fragment_container, fragment, tag)
        }




        val stopWatch = manager.findFragmentByTag(TAG_STOP_WATCH)
        val record = manager.findFragmentByTag(TAG_RECORD)
        val target = manager.findFragmentByTag(TAG_TARGET)
        val settings = manager.findFragmentByTag(TAG_SETTINGS)

        manager.apply {
            for (f in fragments) {
                if (f == manager.findFragmentByTag(TAG_RECORD_DETAIL)){
                    popBackStack()
                }
            }
        }

        if (stopWatch != null) {
            ft.hide(stopWatch)
        }
        if (record != null) {
            ft.hide(record)
        }
        if (target != null) {
            ft.hide(target)
        }
        if (settings != null) {
            ft.hide(settings)
        }


        if(tag == TAG_STOP_WATCH) {
            if (stopWatch != null){
                ft.show(stopWatch)
            }
        } else if (tag == TAG_RECORD) {
            if (record != null) {
                ft.show(record)
            }
        } else if (tag == TAG_TARGET) {
            if (target != null) {
                ft.show(target)
            }
        } else if (tag == TAG_SETTINGS) {
            if (settings != null) {
                ft.show(settings)
            }
        }

        ft.commitAllowingStateLoss()

    }

    private fun setupEvents() {
        binding.mainNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.stop_watch_fragment -> setFragment(TAG_STOP_WATCH, StopWatchFragment())
                R.id.record_fragment -> setFragment(TAG_RECORD, RecordFragment())
                R.id.target_fragment -> setFragment(TAG_TARGET, TargetFragment())
                R.id.settings_fragment -> setFragment(TAG_SETTINGS, SettingsFragment())
            }
            true
        }

    }

    //    스탑워치 프래그먼트에서 레코드디테일프래그먼트 호출
    override fun onRecordSelected(recordId: UUID) {
        val fragment = RecordDetailFragment.newInstance(recordId)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, TAG_RECORD_DETAIL)
            .addToBackStack(null)
            .commit()
    }

    override fun onThemeSelected(theme: String) {
        ThemeManager.applyTheme(theme)
    }

}

