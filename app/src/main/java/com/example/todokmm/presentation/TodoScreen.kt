package com.example.todokmm.presentation
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todokmm.data.Todo
import com.example.todokmm.viewmodel.TodoViewModel
import com.example.todokmm.viewmodel.TodoViewModelFactory
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun TodoScreen() {
    val context = LocalContext.current
    val viewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(context))
    val todoList by viewModel.todoList.collectAsState()

    var newTask by remember { mutableStateOf("") }
    var editMode by remember { mutableStateOf(false) }
    var editingTodo by remember { mutableStateOf<Todo?>(null) }

    val sharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    val isDarkThemeSaved = sharedPreferences.getBoolean("isDarkTheme", false)
    var isDarkTheme by remember { mutableStateOf(isDarkThemeSaved) }

    LaunchedEffect(isDarkTheme) {
        sharedPreferences.edit().putBoolean("isDarkTheme", isDarkTheme).apply()
    }

    val backgroundColor = if (isDarkTheme) Color(0xFF303030) else Color.White
    val inputTextColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.White else Color.Black

    val pendingTasks = todoList.filter { !it.isDone }
    val completedTasks = todoList.filter { it.isDone }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Add New Task Section (NO CARD)
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                OutlinedTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    label = {
                        Text(
                            text = if (editMode) "Edit task" else "Add new task",
                            fontWeight = FontWeight.Bold,
                            color = inputTextColor
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = LocalTextStyle.current.copy(color = inputTextColor),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = inputTextColor,
                        cursorColor = inputTextColor,
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        focusedLabelColor = inputTextColor,
                        unfocusedLabelColor = inputTextColor.copy(alpha = 0.6f)
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (newTask.isNotBlank()) {
                                if (editMode && editingTodo != null) {
                                    viewModel.updateTodo(editingTodo!!.copy(task = newTask))
                                    editMode = false
                                    editingTodo = null
                                } else {
                                    viewModel.addTodo(newTask)
                                }
                                newTask = ""
                            }
                        }
                    )
                )
            }

            // Pending Tasks Section (Card)
            if (pendingTasks.isNotEmpty()) {
                Card(
                    elevation = 6.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = Color(0xFFdcdcdc)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Pending", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        for (todo in pendingTasks) {
                            TodoItem(
                                todo = todo,
                                onToggle = { viewModel.toggleTodo(it) },
                                onDelete = { viewModel.deleteTodo(it) },
                                onEdit = {
                                    editMode = true
                                    editingTodo = it
                                    newTask = it.task
                                }
                            )
                        }
                    }
                }
            }

            // Completed Tasks Section (Card)
            if (completedTasks.isNotEmpty()) {
                Card(
                    elevation = 6.dp,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = Color(0xFFdcdcdc)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Completed", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        for (todo in completedTasks) {
                            TodoItem(
                                todo = todo,
                                onToggle = { viewModel.toggleTodo(it) },
                                onDelete = { viewModel.deleteTodo(it) },
                                onEdit = {
                                    editMode = true
                                    editingTodo = it
                                    newTask = it.task
                                }
                            )
                        }
                    }
                }
            }
        }

        // Theme Toggle FAB
        FloatingActionButton(
            onClick = { isDarkTheme = !isDarkTheme },
            backgroundColor = if (isDarkTheme) Color.White else Color.Black,
            contentColor = if (isDarkTheme) Color.Black else Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = if (isDarkTheme) "🌞" else "🌙", fontSize = 20.sp)
        }
    }
}




@Composable
fun TodoItem(
    todo: Todo,
    onToggle: (Todo) -> Unit,
    onDelete: (Todo) -> Unit,
    onEdit: (Todo) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                if (todo.isDone) Color(0xFFB2FF59) else Color(0xFFFFFF00),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Task Name
            Text(
                text = todo.task,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(2f)
                    .padding(end = 8.dp)
            )

            // Toggle Button
            Button(
                onClick = { onToggle(todo) },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (todo.isDone) Color.Red else Color(0xFF4CAF50),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (todo.isDone) "Again?" else "Completed?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Edit and Delete Icons
            Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                IconButton(onClick = { onEdit(todo) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { onDelete(todo) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

