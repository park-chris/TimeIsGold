package com.crystal.timeisgold.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crystal.timeisgold.R
import com.crystal.timeisgold.Target
import com.crystal.timeisgold.TargetRepository
import com.crystal.timeisgold.TargetViewModel
import com.crystal.timeisgold.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "TargetFragment"

class TargetFragment: Fragment() {

    private lateinit var targetRecyclerView: RecyclerView
    private lateinit var addFloating: FloatingActionButton

    private var adapter: TargetAdapter? = TargetAdapter(emptyList())

    private val targetViewModel: TargetViewModel by lazy {
        ViewModelProvider(this).get(TargetViewModel::class.java)
    }
    private val targetRepository = TargetRepository.get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_target, container, false)

        targetRecyclerView = view.findViewById(R.id.target_recycler_view)
        targetRecyclerView.layoutManager = LinearLayoutManager(context)
        targetRecyclerView.addItemDecoration(DividerDecoration(requireContext(), R.drawable.line_divider, 20, 20))
        targetRecyclerView.adapter = adapter

        addFloating = view.findViewById(R.id.add_floating)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        targetViewModel.targetListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { targets ->
                targets?.let {
                    updateUI(targets)
                }
            }
        )


        addFloating.setOnClickListener {
            Toast.makeText(requireContext(),"Clicked add Floating Button", Toast.LENGTH_SHORT).show()
            val dlg = TargetCustomDialog(requireContext())
            dlg.setOnOKClickedListener { list ->

                val target = Target()

                when (list[0]) {
                    "1일" -> target.duration = 1
                    "7일" -> target.duration = 7
                    "30일" -> target.duration = 30
                }
                target.item = list[1]
                target.targetTime = list[2].toInt()
                target.date = Date(System.currentTimeMillis())
                targetViewModel.addTarget(target)
            }
            dlg.start(requireContext())
        }
    }

    private inner class TargetHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var target: Target

        private val targetDateText: TextView = itemView.findViewById(R.id.target_date_text)
        private val targetTimeTextView: TextView = itemView.findViewById(R.id.target_time_text)
        private val timeText: TextView = itemView.findViewById(R.id.time_text)
        private val itemText: TextView = itemView.findViewById(R.id.target_item_text)

        init {
            itemView.setOnClickListener { this }
        }

        fun bind(target: Target) {
            this.target = target
            targetDateText.text = UIUtil.convertTimestampToDate(this.target.date)
            targetTimeTextView.text = UIUtil.getDurationTime(this.target.targetTime)
            timeText.text = UIUtil.getDurationTime(this.target.time)
            itemText.text = this.target.item

        }
    }

    private inner class  TargetAdapter(var targets: List<Target>): RecyclerView.Adapter<TargetHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetHolder {
            val view = layoutInflater.inflate(R.layout.list_item_target, parent, false)
            return TargetHolder(view)
        }

        override fun onBindViewHolder(holder: TargetHolder, position: Int) {
           val target = targets[position]

            holder.bind(target)
        }

        override fun getItemCount() = targets.size

    }


    private fun updateUI(targets: List<Target>) {
        adapter = TargetAdapter(targets)
        targetRecyclerView.adapter = adapter

    }
}