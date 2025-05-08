package com.example.listadetareas.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetareas.R
import com.example.listadetareas.adapters.CategoryAdapter
import com.example.listadetareas.data.Category
import com.example.listadetareas.data.CategoryDAO
import com.example.listadetareas.databinding.ActivityMainBinding
import com.example.listadetareas.databinding.DialogCreateCategoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var adapter: CategoryAdapter
    var categoryList: List<Category> = emptyList()

    lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryDAO = CategoryDAO(this)

        categoryList = categoryDAO.findAll()

        adapter = CategoryAdapter(categoryList, { position ->
            // He pulsado una categoría
            val category = categoryList[position]

            val intent = Intent(this, TaskListActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            startActivity(intent)
        }, { position ->
            // Edit
            val category = categoryList[position]
            showCategoryDialog(category)
        }, { position ->
            // Delete
            showDeleteConfirmation(position)
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addCategoryButton.setOnClickListener {
            showCategoryDialog(Category(-1L, ""))
        }
    }

    fun showCategoryDialog(category: Category) {
        val dialogBinding = DialogCreateCategoryBinding.inflate(layoutInflater)

        dialogBinding.titleEditText.setText(category.title)

        var dialogTitle = ""
        var dialogIcon = 0
        if (category.id != -1L) {
            dialogTitle = "Edit category"
            dialogIcon = R.drawable.ic_edit
        } else {
            dialogTitle = "Create category"
            dialogIcon = R.drawable.ic_add
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(dialogTitle)
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok, { dialog, which ->
                category.title = dialogBinding.titleEditText.text.toString()
                if (category.id != -1L) {
                    categoryDAO.update(category)
                } else {
                    categoryDAO.insert(category)
                }
                loadData()
             })
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(dialogIcon)
            .show()
    }

    fun showDeleteConfirmation(position: Int) {
        val category = categoryList[position]

        MaterialAlertDialogBuilder(this)
            .setTitle("Borrar categoría")
            .setMessage("¿Está usted seguro de querer borrar esta categoría? Borrar la categoría, eliminará todas sus tareas!")
            .setPositiveButton(android.R.string.ok, { dialog, which ->
                categoryDAO.delete(category)
                loadData()
            })
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_delete)
            .show()
    }

    fun loadData() {
        categoryList = categoryDAO.findAll()
        adapter.updateItems(categoryList)
    }
}