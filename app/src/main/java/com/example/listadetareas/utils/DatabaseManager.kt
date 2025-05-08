package com.example.listadetareas.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.listadetareas.data.Category
import com.example.listadetareas.data.Task

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "ToDoList.db"

        private const val SQL_CREATE_CATEGORY =
            "CREATE TABLE ${Category.TABLE_NAME} (" +
                    "${Category.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${Category.COLUMN_NAME_TITLE} TEXT)"

        private const val SQL_DELETE_CATEGORY = "DROP TABLE IF EXISTS ${Category.TABLE_NAME}"

        private const val SQL_CREATE_TASK =
            "CREATE TABLE ${Task.TABLE_NAME} (" +
                    "${Task.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Task.COLUMN_NAME_TITLE} TEXT, " +
                    "${Task.COLUMN_NAME_DONE} INTEGER, " +
                    "${Task.COLUMN_NAME_CATEGORY} INTEGER, " +
                    "FOREIGN KEY (${Task.COLUMN_NAME_CATEGORY}) " +
                    "REFERENCES ${Category.TABLE_NAME}(${Category.COLUMN_NAME_ID}))"

        private const val SQL_DELETE_TASK = "DROP TABLE IF EXISTS ${Task.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_CATEGORY)
        db.execSQL(SQL_CREATE_TASK)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    private fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(SQL_DELETE_TASK)
        db.execSQL(SQL_DELETE_CATEGORY)
    }
}