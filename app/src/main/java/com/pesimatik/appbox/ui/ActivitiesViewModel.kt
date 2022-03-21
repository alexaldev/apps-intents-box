package com.pesimatik.appbox.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pesimatik.appbox.alphabeticSorter
import com.pesimatik.appbox.model.ActivityRepository
import com.pesimatik.appbox.model.ActivityEntry
import com.pesimatik.appbox.model.ActivityUsagesRepository
import com.pesimatik.appbox.utils.newTaskActivity

class ActivitiesViewModel(val app: Application) : AndroidViewModel(app) {

    val activitiesEntries: LiveData<List<ActivityEntry>>
//    val activitiesUsages: LiveData<MutableMap<String, Float>>

    init {
        val pm = getApplication<Application>().packageManager
        val sorter = alphabeticSorter(pm)
        activitiesEntries = MutableLiveData(ActivityRepository.fetchActivities(pm, sorter))
//        activitiesUsages = MutableLiveData(ActivityUsagesRepository.getUsages(app).toMutableMap())
    }

    fun onEntryClick(entry: ActivityEntry) {

//        activitiesUsages.value!!.merge(entry.name, 1f, Float::plus)
//        ActivityUsagesRepository.storeUsages(
//            app,
//            activitiesUsages.value!!
//        )
        app.startActivity(newTaskActivity(entry.resolveInfo.activityInfo))
    }
    override fun onCleared() {
        super.onCleared()
//        ActivityUsagesRepository.storeUsages(app, activitiesUsages.value!!)
    }
}