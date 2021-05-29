package com.example.catetin

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_akun.*

class AkunActivity : AppCompatActivity() {
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(true)

        try {
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("MainActId", 0)
            if (id != 0) {
                edtNama.setText(bundle.getString("MainActNama"))
            }
        } catch (ex: Exception) {}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_input_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.btAdd -> {
                var dbManager = AkunDbManager(this)
                var values = ContentValues()
                values.put("Nama", edtNama.text.toString())

                if (id == 0) {
                    val mID = dbManager.insert(values)
                    if (mID > 0) {
                        Toast.makeText(this, "Berhasil menambah record!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal menambah record!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    var selectionArs = arrayOf(id.toString())
                    val mID = dbManager.update(values, "Id=?", selectionArs)
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

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }

        return 0
    }
}