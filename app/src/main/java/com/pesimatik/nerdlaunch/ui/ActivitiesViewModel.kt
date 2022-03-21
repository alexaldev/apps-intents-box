package com.pesimatik.nerdlaunch.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pesimatik.nerdlaunch.alphabeticSorter
import com.pesimatik.nerdlaunch.model.ActivityRepository
import com.pesimatik.nerdlaunch.model.ActivityEntry
import com.pesimatik.nerdlaunch.model.ActivityUsagesRepository

class ActivitiesViewModel(val app: Application) : AndroidViewModel(app) {

    val activitiesEntries: LiveData<List<ActivityEntry>>
    val activitiesUsages: LiveData<MutableMap<String, Int>>

    init {
        val pm = getApplication<Application>().packageManager
        val sorter = alphabeticSorter(pm)
        activitiesEntries = MutableLiveData(ActivityRepository.fetchActivities(pm, sorter))
        activitiesUsages = MutableLiveData(ActivityUsagesRepository.getUsages(app).toMutableMap())
    }

    override fun onCleared() {
        super.onCleared()
        ActivityUsagesRepository.storeUsages(app, activitiesUsages.value!!)
    }
}