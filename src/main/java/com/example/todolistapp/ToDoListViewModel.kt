package com.example.todolistapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToDoListViewModel() : ViewModel() {
    private val repository : ToDoListRepository = ToDoListRepository()
    private val toDoListLiveData = MutableLiveData<List<ToDoListModel>>()
    private var currentId = 0

    fun addTask(task : ToDoListModel){
        repository.addTask(task.copy(id = currentId++))
        toDoListLiveData.value = repository.getAllTasks()
    }

    fun getTasksLiveData(): LiveData<List<ToDoListModel>>{
        return toDoListLiveData
    }

    fun editTask(updatedTask : ToDoListModel){
        repository.editTask(updatedTask)
        toDoListLiveData.value = repository.getAllTasks()
    }

    fun deleteTask(task: ToDoListModel) {
        repository.deleteTask(task)
        toDoListLiveData.value = repository.getAllTasks()
    }
}