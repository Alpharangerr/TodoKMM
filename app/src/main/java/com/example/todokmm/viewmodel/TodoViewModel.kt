package com.example.todokmm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todokmm.data.Todo
import com.example.todokmm.data.TodoDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoViewModel(private val dao: TodoDao) : ViewModel() {

    private val _todoList = MutableStateFlow<List<Todo>>(emptyList())
    val todoList: StateFlow<List<Todo>> = _todoList.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getAllTodos().collect { todos ->
                _todoList.value = todos
            }
        }
    }

    fun addTodo(task: String) {
        viewModelScope.launch {
            dao.insertTodo(Todo(task = task))
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            dao.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            dao.deleteTodo(todo)
        }
    }

    fun toggleTodo(todo: Todo) {
        viewModelScope.launch {
            val updated = todo.copy(isDone = !todo.isDone)
            dao.updateTodo(updated)
        }
    }

    // New function to handle editing the task
    fun editTodo(todo: Todo, newTask: String) {
        viewModelScope.launch {
            val updatedTodo = todo.copy(task = newTask)
            dao.updateTodo(updatedTodo)
        }
    }
}
