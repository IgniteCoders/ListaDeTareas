package com.example.listadetareas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class CategoryDAO(private val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }

    // Insertar
    fun insert(category: Category) {
        open()

        try {
            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(Category.COLUMN_NAME_TITLE, category.title)

            // Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(Category.TABLE_NAME, null, values)

            Log.i("DATABASE", "Inserted a category with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Actualizar
    fun update(category: Category) {
        open()

        try {
            // Create a new map of values, where column names are the keys
            val values = ContentValues()
            values.put(Category.COLUMN_NAME_TITLE, category.title)

            // Which row to update, based on the id
            val selection = "${Category.COLUMN_NAME_ID} = ${category.id}"

            val count = db.update(Category.TABLE_NAME, values, selection, null)

            Log.i("DATABASE", "Updated category with id: ${category.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Borrar
    fun delete(category: Category) {
        open()

        try {
            // Define 'where' part of query.
            val selection = "${Category.COLUMN_NAME_ID} = ${category.id}"

            // Issue SQL statement.
            val deletedRows = db.delete(Category.TABLE_NAME, selection, null)

            Log.i("DATABASE", "Deleted category with id: ${category.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Obtener un registro por ID
    fun findById(id: Long): Category? {
        open()

        var category: Category? = null
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(
                Category.COLUMN_NAME_ID,
                Category.COLUMN_NAME_TITLE
            )

            // Filter results WHERE "id" = category.id
            val selection = "${Category.COLUMN_NAME_ID} = $id"

            val cursor = db.query(
                Category.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE))

                category = Category(id, title)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return category
    }

    // Obtener todos los registros
    fun findAll(): List<Category> {
        open()

        var categoryList: MutableList<Category> = mutableListOf()
        try {
            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(
                Category.COLUMN_NAME_ID,
                Category.COLUMN_NAME_TITLE
            )

            // Filter results WHERE "id" = category.id
            val selection = null

            val cursor = db.query(
                Category.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE))

                val category = Category(id, title)
                categoryList.add(category)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return categoryList
    }
}