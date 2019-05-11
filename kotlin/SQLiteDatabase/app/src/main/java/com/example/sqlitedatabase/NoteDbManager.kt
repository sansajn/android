package com.example.sqlitedatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class NoteDbManager {
	constructor(context: Context) {
		var dbHelper = DatabaseHelper(context)
		db = dbHelper.writableDatabase
	}

	fun insert(values: ContentValues): Long {
		val ID = db!!.insert(dbTable, "", values)
		return ID
	}

	fun queryAll(): Cursor {
		return db!!.rawQuery("SELECT * FROM $dbTable", null)
	}

	fun delete(selection: String, selectionArgs: Array<String>): Int {
		val count = db!!.delete(dbTable, selection, selectionArgs)
		return count
	}

	fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
		val count = db!!.update(dbTable, values, selection, selectionArgs)
		return count
	}

	inner class DatabaseHelper : SQLiteOpenHelper {

		constructor(context: Context) : super(context, dbName, null, dbVersion) {
			this.context = context
		}

		override fun onCreate(db: SQLiteDatabase?) {
			db!!.execSQL(CREATE_TABLE_SQL)
			Toast.makeText(context, "database created", Toast.LENGTH_LONG).show()
		}

		override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
			db!!.execSQL("DROP TABLE IF EXISTS $dbTable")
		}

		var context: Context? = null
	}

	private val dbName = "JSANotes"
	private val dbTable = "Notes"
	private val colId = "Id"
	private val colTitle = "Title"
	private val colContent = "Content"
	private val dbVersion = 1

	private val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS $dbTable ($colId INTEGER PRIMARY KEY, $colTitle TEXT, $colContent TEXT);"
	private var db: SQLiteDatabase? = null
}
