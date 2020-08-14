package com.example.roomdatabase.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.roomdatabase.dao.DataDao
import com.example.roomdatabase.model.Data

class DataRepository(private val dataDao: DataDao) {

    val items: LiveData<List<Data>> = dataDao.getAllItems()

    @WorkerThread
    suspend fun insert(todo: Data) {
        dataDao.insert(todo)
    }

    @WorkerThread
    suspend fun update(todo: Data) {
        dataDao.update(todo)
    }

    @WorkerThread
    suspend fun delete(todo: Data) {
        dataDao.delete(todo)
    }
}