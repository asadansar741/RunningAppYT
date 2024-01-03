package com.androiddevs.runningappyt.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.other.SortType
import com.androiddevs.runningappyt.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    private val runSortedByDate=mainRepository.getAllRunsSortedByDate()
    private val runSortedByDistance=mainRepository.getAllRunsSortedByDistance()
    private val runSortedByCaloriesBurned=mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runSortedByTimeInMillis=mainRepository.getAllRunsSortedByTimeInMillis()
    private val runSortedByAvgSpeed=mainRepository.getAllRunsSortedByAvgSpeed()

    val runs= MediatorLiveData<List<Run>>()
    var sortType= SortType.DATE

    init {
        runs.addSource(runSortedByDate){ result->
            if (sortType==SortType.DATE){
                result?.let {
                    runs.value=it
                }
            }
        }
        runs.addSource(runSortedByAvgSpeed){ result->
            if (sortType==SortType.AVG_SPEED){
                result?.let {
                    runs.value=it
                }
            }
        }
        runs.addSource(runSortedByCaloriesBurned){ result->
            if (sortType==SortType.CALORIES_BURNED){
                result?.let {
                    runs.value=it
                }
            }
        }
        runs.addSource(runSortedByTimeInMillis){ result->
            if (sortType==SortType.RUN_TIME){
                result?.let {
                    runs.value=it
                }
            }
        }
        runs.addSource(runSortedByDistance){ result->
            if (sortType==SortType.DISTANCE){
                result?.let {
                    runs.value=it
                }
            }
        }
    }

    fun sortRuns(sortType: SortType)=when(sortType){
        SortType.DATE->runSortedByDate?.value?.let { runs.value=it }
        SortType.RUN_TIME->runSortedByTimeInMillis?.value?.let { runs.value=it }
        SortType.AVG_SPEED->runSortedByAvgSpeed?.value?.let { runs.value=it }
        SortType.DISTANCE->runSortedByDistance?.value?.let { runs.value=it }
        SortType.CALORIES_BURNED->runSortedByCaloriesBurned?.value?.let { runs.value=it }
    }.also {
        this.sortType=sortType
    }

    fun insertRun(run: Run)=viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}