package com.example.catetin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class RekamanKeuanganDbManager {
    private val dbName = "Catetin"
    private val dbTable = "RekamanKeuangan"
    private val colId = "Id"
    private val dbVersion = 1
    private val colNominal = "Nominal"
    private val colKategori = "Kategori"
    private val colKategoriPosition = "KategoriPosition"
    private val colAkun = "Akun"
    private val colNotes = "Notes"
    private val colJenis = "Jenis"
    private val colTanggal = "Tanggal"

    private var db: SQLiteDatabase? = null

    constructor(context: Context) {
        var dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }

    fun insert(values: ContentValues): Long {
        val ID = db!!.insert(dbTable, "", values)
        return ID
    }

    fun queryAll(akun: Int): Cursor {
        return db!!.rawQuery("select * from $dbTable where Akun = ?", arrayOf(akun.toString()))
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val count = db!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionargs: Array<String>): Int {
        val count = db!!.update(dbTable, values, selection, selectionargs)
        return count
    }

    fun sumPemasukan() : Cursor {
        return db!!.rawQuery("SELECT SUM($colNominal) AS Pemasukan FROM $dbTable WHERE $colJenis = '1'", null)
    }

    fun sumPengeluaran() : Cursor {
        return db!!.rawQuery("SELECT SUM($colNominal) AS Pengeluaran FROM $dbTable WHERE $colJenis = '0'", null)
    }

    fun sumPemasukan(akun: Int) : Cursor {
        return db!!.rawQuery("SELECT SUM($colNominal) AS Pemasukan FROM $dbTable WHERE $colJenis = '1' AND $colAkun = ?", arrayOf(akun.toString()))
    }

    fun sumPengeluaran(akun: Int) : Cursor {
        return db!!.rawQuery("SELECT SUM($colNominal) AS Pengeluaran FROM $dbTable WHERE $colJenis = '0' AND $colAkun = ?", arrayOf(akun.toString()))
    }
}