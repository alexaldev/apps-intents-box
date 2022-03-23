package com.pesimatik.appbox.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pesimatik.appbox.INITIAL_FONT_SIZE
import com.pesimatik.appbox.alphabeticSorter
import com.pesimatik.appbox.model.ActivityEntry
import com.pesimatik.appbox.model.ActivityRepository
import com.pesimatik.appbox.model.ActivityUsagesRepository
import com.pesimatik.appbox.utils.newTaskActivity

class ActivitiesViewModel(val app: Application) : AndroidViewModel(app) {

    private val entriesClickHistoryRepository = ActivityUsagesRepository
    private val activitiesRepository = ActivityRepository

    private val activitiesEntries: LiveData<List<ActivityEntry>>
    private val entriesUsageHistory: MutableLiveData<MutableMap<String, Float>>

    val renderableEntries: MediatorLiveData<List<RenderableActivityEntry>>

    init {

        val pm = getApplication<Application>().packageManager
        val sorter = alphabeticSorter(pm)
        activitiesEntries = MutableLiveData(activitiesRepository.fetchActivities(pm, sorter))
        entriesUsageHistory = MutableLiveData(entriesClickHistoryRepository.getUsages(app))

        renderableEntries = MediatorLiveData()
        renderableEntries.addSource(activitiesEntries) { entries ->
            renderableEntries.value = entries.map {
                RenderableActivityEntry(
                    it,
                    INITIAL_FONT_SIZE
                )
            }
        }
        renderableEntries.addSource(entriesUsageHistory) { entriesUsageHistory: Map<String, Float> ->
            renderableEntries.value = mergeEntriesWith(entriesUsageHistory)
        }
    }

    private fun mergeEntriesWith(entriesUsage: Map<String, Float>): List<RenderableActivityEntry> {

        return if (entriesUsage.isEmpty()) {
            activitiesEntries.value!!
                .map { RenderableActivityEntry(it, INITIAL_FONT_SIZE) }
        } else {
            activitiesEntries.value!!
                .associateByTo(hashMapOf()) { it.name }
                .map { RenderableActivityEntry(it.value, entriesUsage[it.key]!!) }
        }
    }

    fun onEntryClick(entry: ActivityEntry) {

        if (!entriesClickHistoryRepository.entriesHistoryExists(app)) {
            dispatchNonExistentHistory()
        } else {
            entriesUsageHistory.value!!.merge(entry.name, 1f, Float::plus)
            entriesClickHistoryRepository.storeUsages(app, HashMap(entriesUsageHistory.value!!))
        }

        app.startActivity(newTaskActivity(entry.resolveInfo.activityInfo))
    }


    // TODO: RETHINK THIS
    private fun dispatchNonExistentHistory() {

        // Init the entries history with the initial value
        entriesUsageHistory.value = activitiesEntries.value!!
            .map { it.name }
            .zip((List(activitiesEntries.value!!.size) { INITIAL_FONT_SIZE }))
            .toMap()
            .toMutableMap()

        entriesClickHistoryRepository.storeUsages(app, HashMap(entriesUsageHistory.value!!))
    }

}