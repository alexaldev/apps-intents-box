package com.pesimatik.nerdlaunch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Adapter
import android.widget.TextView
import androidx.annotation.Dimension.SP
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pesimatik.app_box.R
import com.pesimatik.nerdlaunch.model.ActivityEntry
import com.pesimatik.nerdlaunch.model.ActivityUsagesRepository
import com.pesimatik.nerdlaunch.ui.ActivitiesViewModel
import com.pesimatik.nerdlaunch.ui.UsageToSizeDispatcher

class MainFragment : Fragment() {

    private lateinit var mainRv: RecyclerView
    private lateinit var rvAdapter: ActivityAdapter
    private lateinit var activitiesViewModel: ActivitiesViewModel

    private val itemLongPressListener = View.OnLongClickListener { navigateToSettings() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activitiesViewModel =
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
                .create(ActivitiesViewModel::class.java)

        mainRv = view.findViewById(R.id.app_rv)
        mainRv.layoutManager = LinearLayoutManager(context)

        val activityCLickListener : (ActivityEntry) -> Unit = {
            val activityInfo = it.resolveInfo.activityInfo
            val intent = Intent(Intent.ACTION_MAIN).apply {
                setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            activitiesViewModel.activitiesUsages.value!!.merge(activityInfo.loadLabel(requireActivity().packageManager).toString(), 50, Int::plus)
            rvAdapter.notifyDataSetChanged()
            ActivityUsagesRepository.storeUsages(requireContext(), activitiesViewModel.activitiesUsages.value!!)
            requireActivity().startActivity(intent)
        }
        rvAdapter = ActivityAdapter(mutableListOf(), activityCLickListener, itemLongPressListener)
        mainRv.adapter = rvAdapter

        activitiesViewModel.activitiesEntries.observe(viewLifecycleOwner) { activityEntries ->
            activityEntries.forEach {
                activitiesViewModel.activitiesUsages.value!!.putIfAbsent(it.name, 1)
            }
            rvAdapter.updateList(activityEntries)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    fun navigateToSettings(): Boolean {

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, SettingsFragment.newInstance())
            .addToBackStack(null)
            .commit()
        return true
    }

    private inner class ActivityHolder(
        itemView: View,
        val clickListener: (ActivityEntry) -> Unit,
        val longPressListener: View.OnLongClickListener
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val nameTextView = itemView as TextView
        private lateinit var activityEntry: ActivityEntry

        init {
            nameTextView.setOnClickListener(this)
            nameTextView.setOnLongClickListener { longPressListener.onLongClick(it) }
        }

        fun bindActivityEntry(activityEntry: ActivityEntry) {
            this.activityEntry = activityEntry
            nameTextView.text = activityEntry.name
            val size = UsageToSizeDispatcher.calculateFontSizes(activityEntry.name, activitiesViewModel.activitiesUsages.value!!)
            nameTextView.setTextSize(SP, size)
        }

        override fun onClick(p0: View?) {
            clickListener(activityEntry)
        }
    }

    private inner class ActivityAdapter(
        val activities: MutableList<ActivityEntry>,
        val clickListener: (ActivityEntry) -> Unit,
        val lpl: View.OnLongClickListener
    ) :
        RecyclerView.Adapter<ActivityHolder>() {

        override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ActivityHolder {
            val layoutInflater = LayoutInflater.from(container.context)
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, container, false)
            return ActivityHolder(view, clickListener, lpl)
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            holder.bindActivityEntry(activities[position])
        }

        override fun getItemCount(): Int {
            return activities.size
        }

        fun updateList(newActivities: List<ActivityEntry>) {
            this.activities.clear()
            this.activities.addAll(newActivities)
            notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}