package cat.copernic.fgascong.progressbarcontrol

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    sealed class JobState {
        object PREPARED : JobState()
        object RUNNING : JobState()
        object CANCELLED : JobState()
        object FINISHED : JobState()
    }

    private val _progressBar1: MutableLiveData<Int> = MutableLiveData(0)
    val progressBar1: LiveData<Int> get() = _progressBar1

    private val _progressBar2: MutableLiveData<Int> = MutableLiveData(0)
    val progressBar2: LiveData<Int> get() = _progressBar2

    private val _jobState1: MutableLiveData<JobState> = MutableLiveData(JobState.PREPARED)
    val jobState1 : LiveData<JobState> get() = _jobState1

    private val _jobState2: MutableLiveData<JobState> = MutableLiveData(JobState.PREPARED)
    val jobState2 : LiveData<JobState> get() = _jobState2

    private var job1: Job? = null
    private var job2: Job? = null


    fun start(number: Int) {
        when (number) {
            1 -> if (job1 == null || job1!!.isCancelled || job1!!.isCompleted) {
                job1 = viewModelScope.launch {
                    _jobState1.value = JobState.RUNNING
                    startProgressBar(1)
                }
            }
            2 -> if (job2 == null || job2!!.isCancelled || job2!!.isCompleted) {
                job2 = viewModelScope.launch {
                    _jobState2.value = JobState.RUNNING
                    startProgressBar(2)
                }
            }
        }
    }

    fun reset(number: Int) {
        when (number) {
            1 -> {
                _progressBar1.value = 0
                _jobState1.value = if (job1!!.isActive) JobState.RUNNING else JobState.PREPARED
            }
            2 -> {
                _progressBar2.value = 0
                _jobState2.value = if (job2!!.isActive) JobState.RUNNING else JobState.PREPARED
            }
        }
    }

    fun stop(number: Int) {
        when (number) {
            1 -> {
                job1?.cancel()
                _jobState1.value = JobState.CANCELLED
            }
            2 -> {
                job2?.cancel()
                _jobState2.value = JobState.CANCELLED
            }
        }
    }

    private suspend fun startProgressBar(number: Int) {
        when (number) {
            1 -> while (progressBar1.value!! < 100) {
                delay(100)
                _progressBar1.value = _progressBar1.value?.inc()
                if(progressBar1.value == 100) _jobState1.value = JobState.FINISHED
            }
            2 -> while (progressBar2.value!! < 100) {
                delay(100)
                _progressBar2.value = _progressBar2.value?.inc()
                if(progressBar2.value == 100) _jobState2.value = JobState.FINISHED
            }
        }
    }
}