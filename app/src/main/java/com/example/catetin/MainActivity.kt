package com.example.catetin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.addNote
import kotlinx.android.synthetic.main.activity_main.balanceNum
import kotlinx.android.synthetic.main.activity_main.lvNotes
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_rekaman_keuangan_home.*

class MainActivity : AppCompatActivity() {
    private var listNotes = ArrayList<Akun>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
        }

        loadQueryAll()
        loadTotalBalance()

        addNote.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, AkunActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
        loadQueryAll()
        loadTotalBalance()
    }

    fun loadQueryAll() {
        var dbManager = AkunDbManager(this)
        val cursor = dbManager.queryAll()
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val nama = cursor.getString(cursor.getColumnIndex("Nama"))

                listNotes.add(Akun(id, nama))
            } while (cursor.moveToNext())
        }

        var notesAdapter = NotesAdapter(this, listNotes)
        lvNotes.adapter = notesAdapter
    }

    fun loadTotalBalance() {
        val totalBalance = loadPemasukan() - loadPengeluaran()
        if (totalBalance >= 0) {
            balanceNum.text = "$${totalBalance.toString()}"
        } else {
            balanceNum.text = "-$${Math.abs(totalBalance).toString()}"
        }
    }

    fun loadAccountBalance(akun: Int) : Int {
        return loadPemasukan(akun) - loadPengeluaran(akun)
    }

    fun loadPemasukan() : Int {
        var dbManager = RekamanKeuanganDbManager(this)
        val cursor = dbManager.sumPemasukan()
        var pemasukan = 0
        if (cursor.moveToFirst()) {
            do {
                pemasukan = cursor.getInt(cursor.getColumnIndex("Pemasukan"))
            } while (cursor.moveToNext())
        }

        return pemasukan
    }

    fun loadPemasukan(akun: Int) : Int {
        var dbManager = RekamanKeuanganDbManager(this)
        val cursor = dbManager.sumPemasukan(akun)
        var pemasukan = 0
        if (cursor.moveToFirst()) {
            do {
                pemasukan = cursor.getInt(cursor.getColumnIndex("Pemasukan"))
            } while (cursor.moveToNext())
        }

        return pemasukan
    }

    fun loadPengeluaran() : Int {
        var dbManager = RekamanKeuanganDbManager(this)
        val cursor = dbManager.sumPengeluaran()
        var pengeluaran = 0
        if (cursor.moveToFirst()) {
            do {
                pengeluaran = cursor.getInt(cursor.getColumnIndex("Pengeluaran"))
            } while (cursor.moveToNext())
        }

        return pengeluaran
    }

    fun loadPengeluaran(akun: Int) : Int {
        var dbManager = RekamanKeuanganDbManager(this)
        val cursor = dbManager.sumPengeluaran(akun)
        var pengeluaran = 0
        if (cursor.moveToFirst()) {
            do {
                pengeluaran = cursor.getInt(cursor.getColumnIndex("Pengeluaran"))
            } while (cursor.moveToNext())
        }

        return pengeluaran
    }

    inner class NotesAdapter : BaseAdapter {
        private var notesList = ArrayList<Akun>()
        private var context: Context? = null
        constructor(context: Context, notesList: ArrayList<Akun>) : super() {
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.akun, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mNote = notesList[position]
            vh.tvNama.text = mNote.nama

            val accountBalance = loadAccountBalance(mNote.id!!)
            if (accountBalance >= 0) {
                vh.tvNominal.text = "$${accountBalance.toString()}"
            } else {
                vh.tvNominal.text = "-$${Math.abs(accountBalance).toString()}"
            }

            val bottomSheetFragment = AkunBottomSheetFragment.newInstance(mNote.id!!, mNote.nama)
            vh.listItem.setOnClickListener {
                bottomSheetFragment.show(supportFragmentManager, "AkunBottomSheetDialog")
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }
    }

    private class ViewHolder(view: View?) {
        val tvNama: TextView
        val tvNominal: TextView
        val listItem: LinearLayout
        init {
            this.tvNama = view?.findViewById(R.id.tvNama) as TextView
            this.tvNominal = view.findViewById(R.id.tvNominal) as TextView
            this.listItem = view.findViewById(R.id.list_item) as LinearLayout
        }
    }
}