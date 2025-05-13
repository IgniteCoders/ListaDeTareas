package com.example.listadetareas.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listadetareas.R
import com.example.listadetareas.data.Category
import com.example.listadetareas.data.CategoryDAO
import com.example.listadetareas.data.Task
import com.example.listadetareas.data.TaskDAO
import com.example.listadetareas.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskBinding

    lateinit var category: Category
    lateinit var task: Task

    lateinit var taskDAO: TaskDAO
    lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)

        val categoryId = intent.getLongExtra("CATEGORY_ID", -1)
        category = categoryDAO.findById(categoryId)!!

        val id = intent.getLongExtra("TASK_ID", -1)
        if (id == -1L) {
            task = Task(-1L, "", false, category)
        } else {
            task = taskDAO.findById(id)!!
        }

        binding.titleEditText.setText(task.title)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            task.title = title
            if (task.id == -1L) {
                taskDAO.insert(task)
            } else {
                taskDAO.update(task)
            }
            finish()
        }
    }
}