package com.example.catetin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper : SQLiteOpenHelper {
    var context: Context? = null

    private val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Akun (Id INTEGER PRIMARY KEY, Nama TEXT);"
    private val CREATE_TABLE_RK = "CREATE TABLE IF NOT EXISTS RekamanKeuangan " +
            "(Id INTEGER PRIMARY KEY, Nominal INTEGER, Jenis INTEGER, Akun INTEGER, NamaAkun TEXT, " +
            "Kategori TEXT, KategoriPosition INTEGER, Notes TEXT, Tanggal DATE);"

    constructor(context: Context) : super(context, "Catetin", null, 1) {
        this.context = context
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_TABLE_SQL)
        db.execSQL(CREATE_TABLE_RK)
        Toast.makeText(this.context, " database is created", Toast.LENGTH_LONG).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("Drop table IF EXISTS Akun")
        db.execSQL("Drop table IF EXISTS RekamanKeuangan")
    }
}