package com.example.catetin

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note.*

class RekamanKeuanganActivity : AppCompatActivity() {
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }

        setSpinnerValues()

        try {
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("MainActId", 0)

            if (id != 0) {
                edtNominal.setText(bundle.getString("MainActNominal"))
                edtJenis.isChecked = bundle.getInt("MainActJenis") == 1
                edtKategori.setSelection(bundle.getInt("MainActKategoriPosition"))
                edtAkun.setSelection(getIndex(edtAkun, bundle.getString("MainActNamaAkun").toString()))
                edtTanggal.setText(bundle.getString("MainActTanggal"))
                edtNotes.setText(bundle.getString("MainActNotes"))

                changeActionBarTitle()
            }
        } catch (ex: Exception) {
        }

        edtJenis.setOnClickListener {
            changeActionBarTitle()
        }
    }

    private fun changeActionBarTitle() {
        if (edtJenis.isChecked) {
            supportActionBar?.title = "Pemasukan"
        } else {
            supportActionBar?.title = "Pengeluaran"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_input_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btAdd -> {
                var dbManager = RekamanKeuanganDbManager(this)
                val akunDbManager = AkunDbManager(this)

                var searchCursor = akunDbManager.searchId(edtAkun.selectedItem.toString())
                var idAkun = 0

                if (searchCursor.moveToFirst()) {
                    do {
                        idAkun = searchCursor.getInt(searchCursor.getColumnIndex("Id"))
                    } while (searchCursor.moveToNext())
                }

                var values = ContentValues()
                values.put("Nominal", edtNominal.text.toString())
                values.put("Jenis", edtJenis.isChecked)
                values.put("Kategori", edtKategori.selectedItem.toString())
                values.put("KategoriPosition", edtKategori.selectedItemPosition)
                values.put("Tanggal", edtTanggal.text.toString())
                values.put("Akun", idAkun)
                values.put("NamaAkun", edtAkun.selectedItem.toString())
                values.put("Notes", edtNotes.text.toString())

                if (id == 0) {
                    val mID = dbManager.insert(values)
                    if (mID > 0) {
                        Toast.makeText(this, "Berhasil menambah record!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal menambah record!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    var selectionArgs = arrayOf(id.toString())
                    val mID = dbManager.update(values, "Id=?", selectionArgs)
                    if (mID > 0) {
                        Toast.makeText(this, "Berhasil update record!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal update record!", Toast.LENGTH_LONG).show()
                    }
                }

                return true
            }
            else -> return false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setSpinnerValues() {
        var list = mutableListOf<String?>()
        var akunDbManager = AkunDbManager(this)
        val cursor = akunDbManager.queryAll()

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("Nama")))
            } while (cursor.moveToNext())
        }

        val adapter = ArrayAdapter(this, R.layout.spinner_item, list)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        edtAkun.adapter = adapter
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }

        return 0
    }
}