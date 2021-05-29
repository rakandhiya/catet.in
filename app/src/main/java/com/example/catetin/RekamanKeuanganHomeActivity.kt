package com.example.catetin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.lvNotes
import kotlinx.android.synthetic.main.activity_rekaman_keuangan_home.*

class RekamanKeuanganHomeActivity : AppCompatActivity() {
    private var listNotes = ArrayList<RekamanKeuangan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekaman_keuangan_home)
        setSupportActionBar(toolbar)

        var bundle: Bundle? = intent.extras

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.title = bundle?.getString("MainActNamaAkun")

        loadQueryAll(bundle!!.getInt("MainActIdAkun"))
        loadBalance(bundle.getInt("MainActIdAkun"))

        addNote.setOnClickListener {
            var intent = Intent(this, RekamanKeuanganActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()

        var bundle: Bundle? = intent.extras
        loadQueryAll(bundle!!.getInt("MainActIdAkun"))
        loadBalance(bundle.getInt("MainActIdAkun"))
    }

    fun loadQueryAll(akun: Int) {
        var dbManager = RekamanKeuanganDbManager(this)
        val cursor = dbManager.queryAll(akun)
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val jenis = cursor.getInt(cursor.getColumnIndex("Jenis"))
                val nominal = cursor.getInt(cursor.getColumnIndex("Nominal"))
                val kategori = cursor.getString(cursor.getColumnIndex("Kategori"))
                val kategoriPosition = cursor.getInt(cursor.getColumnIndex("KategoriPosition"))
                val tanggal = cursor.getString(cursor.getColumnIndex("Tanggal"))
                val akun = cursor.getInt(cursor.getColumnIndex("Akun"))
                val namaAkun = cursor.getString(cursor.getColumnIndex("NamaAkun"))
                val notes = cursor.getString(cursor.getColumnIndex("Notes"))

                listNotes.add(RekamanKeuangan(id, nominal, jenis, kategori, kategoriPosition, tanggal, akun, namaAkun, notes))
            } while (cursor.moveToNext())
        }

        var notesAdapter = NotesAdapter(this, listNotes)
        lvNotes.adapter = notesAdapter
    }

    fun loadBalance(akun: Int) {
        val hasil = loadPemasukan(akun) - loadPengeluaran(akun)
        if (hasil >= 0) {
            balanceNum.text = "$${Math.abs(hasil)}"
        } else {
            balanceNum.text = "-$${Math.abs(hasil)}"
        }
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
        private var notesList = ArrayList<RekamanKeuangan>()
        private var context: Context? = null
        constructor(context: Context, notesList: ArrayList<RekamanKeuangan>) : super() {
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.note, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mNote = notesList[position]
            vh.tvKeterangan.text = mNote.kategori

            if (mNote.jenis!! == 1) {
                vh.tvNominal.setTextColor(Color.parseColor("#65BCBF"))
                vh.tvNominal.text = "$${mNote.nominal.toString()}"
            } else {
                vh.tvNominal.setTextColor(Color.parseColor("#F8777D"))
                vh.tvNominal.text = "-$${mNote.nominal.toString()}"
            }

            val bottomSheetFragment = RekamanKeuanganBottomSheetFragment.newInstance(mNote)
            vh.listItem.setOnClickListener {
                bottomSheetFragment.show(supportFragmentManager, "RekamanKeuanganBottomSheetDialog")
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
        val tvKeterangan: TextView
        val tvNominal: TextView
        val listItem: LinearLayout
        init {
            this.tvKeterangan = view?.findViewById(R.id.tvKeterangan) as TextView
            this.tvNominal = view.findViewById(R.id.tvNominal) as TextView
            this.listItem = view.findViewById(R.id.list_item) as LinearLayout
        }
    }
}