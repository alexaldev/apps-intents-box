package com.pesimatik.appbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pesimatik.app_box.R
import com.pesimatik.appbox.ui.ActivitiesViewModel
import com.pesimatik.appbox.ui.Config
import com.pesimatik.appbox.ui.EntriesAdapter
import com.pesimatik.appbox.ui.RenderableActivityEntry

class MainFragment : Fragment() {

    // UI
    private lateinit var mainRv: RecyclerView
    private lateinit var mainAdapter: EntriesAdapter
    private val activitiesViewModel: ActivitiesViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(ActivitiesViewModel::class.java)
    }
    private val entryClickListener: (RenderableActivityEntry) -> Unit = {
        activitiesViewModel.onEntryClick(it.activityEntry)
    }
    private val itemLongPressListener: (RenderableActivityEntry) -> Unit = { navigateToSettings() }

    // --------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val v = inflater.inflate(R.layout.fragment_main, container, false)
        mainRv = v.findViewById(R.id.app_rv)
        mainRv.layoutManager = LinearLayoutManager(requireContext())

        val adapterConfig = Config(mutableListOf(), entryClickListener, itemLongPressListener)
        mainAdapter = EntriesAdapter(adapterConfig)
        mainRv.adapter = mainAdapter

        activitiesViewModel.renderableEntries.observe(viewLifecycleOwner) { renderableActivities ->
            mainAdapter.updateEntries(renderableActivities)
        }

        return v
    }

    private fun navigateToSettings(): Boolean {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, SettingsFragment.newInstance())
            .addToBackStack(null)
            .commit()
        return true
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}