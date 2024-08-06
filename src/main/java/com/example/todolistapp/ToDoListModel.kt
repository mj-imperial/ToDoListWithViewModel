package com.example.todolistapp

data class ToDoListModel(
    val id : Int,
    val title: String,
    val description: String,
    val isEditing: Boolean = false,
    val isCompleted: Boolean
)

class ToDoListRepository(){
    private var tasks = mutableListOf<ToDoListModel>()

    fun addTask(task : ToDoListModel){
        tasks.add(task)
    }

    fun getAllTasks(): List<ToDoListModel> {
        return tasks.toList()
    }

    fun editTask(updatedTask : ToDoListModel){
        val index = tasks.indexOfFirst{it.id == updatedTask.id}
        tasks[index] = updatedTask
    }

    fun deleteTask(task: ToDoListModel) {
        tasks.removeAll { it.id == task.id }
    }
}