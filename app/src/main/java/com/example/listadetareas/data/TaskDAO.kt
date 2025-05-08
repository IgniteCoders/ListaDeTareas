package com.example.listadetareas.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.listadetareas.utils.DatabaseManager

class TaskDAO(private val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }

    // Insertar
    fun insert(task: Task) {
        open()

        try {
            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(Task.COLUMN_NAME_TITLE, task.title)
            values.put(Task.COLUMN_NAME_DONE, task.done)
            values.put(Task.COLUMN_NAME_CATEGORY, task.category.id)

            // Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(Task.TABLE_NAME, null, values)

            Log.i("DATABASE", "Inserted a task with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Actualizar
    fun update(task: Task) {
        open()

        try {
            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(Task.COLUMN_NAME_TITLE, task.title)
            values.put(Task.COLUMN_NAME_DONE, task.done)
            values.put(Task.COLUMN_NAME_CATEGORY, task.category.id)

            // Which row to update, based on the id
            val selection = "${Task.COLUMN_NAME_ID} = ${task.id}"

            val count = db.update(Task.TABLE_NAME, values, selection, null)

            Log.i("DATABASE", "Updated task with id: ${task.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Borrar
    fun delete(task: Task) {
        open()

        try {
            // Define 'where' part of query.
            val selection = "${Task.COLUMN_NAME_ID} = ${task.id}"

            // Issue SQL statement.
            val deletedRows = db.delete(Task.TABLE_NAME, selection, null)

            Log.i("DATABASE", "Deleted task with id: ${task.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Obtener un registro por ID
    fun findById(id: Long): Task? {
        open()

        var task: Task? = null
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(
                Task.COLUMN_NAME_ID,
                Task.COLUMN_NAME_TITLE,
                Task.COLUMN_NAME_DONE,
                Task.COLUMN_NAME_CATEGORY
            )

            // Filter results WHERE "id" = task.id
            val selection = "${Task.COLUMN_NAME_ID} = $id"

            val cursor = db.query(
                Task.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) != 0
                val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))

                val category = CategoryDAO(context).findById(categoryId)!!

                task = Task(id, title, done, category)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return task
    }

    // Obtener todos los registros
    fun findAll(): List<Task> {
        open()

        var taskList: MutableList<Task> = mutableListOf()
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(
                Task.COLUMN_NAME_ID,
                Task.COLUMN_NAME_TITLE,
                Task.COLUMN_NAME_DONE,
                Task.COLUMN_NAME_CATEGORY
            )

            // Filter results WHERE "id" = task.id
            val selection = null

            val cursor = db.query(
                Task.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) != 0
                val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))

                val category = CategoryDAO(context).findById(categoryId)!!

                val task = Task(id, title, done, category)
                taskList.add(task)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return taskList
    }

    // Obtener todos los registros por categoría
    fun findAllByCategory(category: Category): List<Task> {
        open()

        var taskList: MutableList<Task> = mutableListOf()
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(
                Task.COLUMN_NAME_ID,
                Task.COLUMN_NAME_TITLE,
                Task.COLUMN_NAME_DONE,
                Task.COLUMN_NAME_CATEGORY
            )

            // Filter results WHERE "id" = task.id
            val selection = "${Task.COLUMN_NAME_CATEGORY} = ${category.id}"

            val cursor = db.query(
                Task.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) != 0
                val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))

                val category = CategoryDAO(context).findById(categoryId)!!

                val task = Task(id, title, done, category)
                taskList.add(task)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return taskList
    }
}