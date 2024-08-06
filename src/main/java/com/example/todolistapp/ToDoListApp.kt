package com.example.todolistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToDoListApp(viewModel: ToDoListViewModel){
    val tasks by viewModel.getTasksLiveData().observeAsState(emptyList())
    var isEditing by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<ToDoListModel?>(null) }
    var showDialog by remember { mutableStateOf(false)}
    var newTitle by remember { mutableStateOf("")}
    var newDescription by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "To Do List",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = { showDialog = true },
        ) {
            Text("Add Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ){
            items(tasks) {
                task ->
                if(task.isEditing){
                    ToDoListEditor(
                        task = task,
                        onEditComplete = {
                            editedTitle, editedDescription ->
                            viewModel.editTask(task.copy(title = editedTitle, description = editedDescription, isEditing = false))
                            isEditing = false
                            taskToEdit = null
                        }
                    )
                }else{
                    ToDoListItem(
                        task = task,
                        onTaskCheckChange = {
                                            isChecked  ->
                                            viewModel.editTask(task.copy(isCompleted = isChecked))
                        },
                        onDeleteClick = {
                            viewModel.deleteTask(task)
                        },
                        onEditClick = {
                            taskToEdit = task.copy(isEditing = true)
                            viewModel.editTask(task.copy(isEditing = true))
                        }
                    )
                }
            }
        }

        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Button(
                            onClick = {
                                if(newTitle.isNotEmpty() && newDescription.isNotEmpty()){
                                    viewModel.addTask(
                                        ToDoListModel(
                                        id = 0,
                                        title = newTitle,
                                        description = newDescription,
                                        isCompleted = false,
                                        isEditing = false
                                    )
                                    )

                                    newTitle = ""
                                    newDescription = ""
                                    showDialog = false
                                }
                        }) {
                            Text("Add")
                        }
                        
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                },
                title = {Text("Add To-Do List Item")},
                text = {
                    Column {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = {newTitle = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        OutlinedTextField(
                            value = newDescription,
                            onValueChange = {newDescription = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            )
        }

    }
}

@Composable
fun ToDoListItem(
    task: ToDoListModel,
    onTaskCheckChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
): Unit {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, Color.DarkGray),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Checkbox(checked = task.isCompleted, onCheckedChange = onTaskCheckChange)

        val textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None

        Text(
            text = task.title,
            modifier = Modifier.padding(8.dp),
            textDecoration = textDecoration
        )

        Text(
            text = task.description,
            modifier = Modifier.padding(8.dp),
            textDecoration = textDecoration
        )

        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun ToDoListEditor(
    task : ToDoListModel,
    onEditComplete: (String, String) -> Unit
){
    var editedTitle by remember { mutableStateOf(task.title) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var isEditing by remember { mutableStateOf(task.isEditing)}

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column{
            BasicTextField(
                value = editedTitle,
                onValueChange = {editedTitle = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

            BasicTextField(
                value = editedDescription,
                onValueChange = {editedDescription = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }

        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedTitle, editedDescription)
            }
        ) {
            Text("Save")
        }
    }
}