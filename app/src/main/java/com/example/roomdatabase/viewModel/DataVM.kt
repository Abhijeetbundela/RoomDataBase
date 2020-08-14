package com.example.roomdatabase.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.roomdatabase.database.AppDatabase
import com.example.roomdatabase.model.Data
import com.example.roomdatabase.repository.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DataVM(application: Application) : AndroidViewModel(application) {

    private val dataRepository: DataRepository
    val data: LiveData<List<Data>>
    private var job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val dataDao = AppDatabase.getDatabase(application).dataDao()
        dataRepository = DataRepository(dataDao)
        data = dataRepository.items
    }

    fun insert(data: Data) = scope.launch(Dispatchers.IO) {
        dataRepository.insert(data)
    }

    fun delete(data: Data) = scope.launch(Dispatchers.IO) {
        dataRepository.delete(data)
    }


    fun update(data: Data) = scope.launch(Dispatchers.IO) {
        dataRepository.update(data)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}