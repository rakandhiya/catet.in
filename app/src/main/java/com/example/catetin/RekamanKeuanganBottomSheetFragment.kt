package com.example.catetin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.rekamankeuangan_bottomsheet_fragment.*

class RekamanKeuanganBottomSheetFragment: BottomSheetDialogFragment() {
    companion object {
        const val ARG_RK_ID = "id"
        const val ARG_RK_JENIS = "jenis"
        const val ARG_RK_NOMINAL = "nominal"
        const val ARG_RK_KATEGORI = "kategori"
        const val ARG_RK_KATEGORI_POSITION = "kategoriPosition"
        const val ARG_RK_AKUN = "akun"
        const val ARG_RK_NAMA_AKUN = "namaAkun"
        const val ARG_RK_TANGGAL = "tanggal"
        const val ARG_RK_NOTES = "notes"

        fun newInstance(rekamanKeuangan: RekamanKeuangan): RekamanKeuanganBottomSheetFragment {
            val fragment = RekamanKeuanganBottomSheetFragment()
            val bundle = Bundle().apply {
                putInt(ARG_RK_ID, rekamanKeuangan.id!!)
                putInt(ARG_RK_JENIS, rekamanKeuangan.jenis!!)
                putInt(ARG_RK_NOMINAL, rekamanKeuangan.nominal!!)
                putString(ARG_RK_KATEGORI, rekamanKeuangan.kategori)
                putInt(ARG_RK_KATEGORI_POSITION, rekamanKeuangan.kategoriPosition!!)
                putInt(ARG_RK_AKUN, rekamanKeuangan.akun!!)
                putString(ARG_RK_NAMA_AKUN, rekamanKeuangan.namaAkun)
                putString(ARG_RK_TANGGAL, rekamanKeuangan.tanggal)
                putString(ARG_RK_NOTES, rekamanKeuangan.notes)
            }

            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rekamankeuangan_bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(ARG_RK_ID)

        btEdit.setOnClickListener {
            var intent = Intent(context, RekamanKeuanganActivity::class.java)
            intent.putExtra("MainActId", arguments?.getInt(ARG_RK_ID))
            intent.putExtra("MainActJenis", arguments?.getInt(ARG_RK_JENIS))
            intent.putExtra("MainActNominal", arguments?.getInt(ARG_RK_NOMINAL).toString())
            intent.putExtra("MainActKategoriPosition", arguments?.getInt(ARG_RK_KATEGORI_POSITION))
            intent.putExtra("MainActTanggal", arguments?.getString(ARG_RK_TANGGAL))
            intent.putExtra("MainActAkun", arguments?.getInt(ARG_RK_AKUN))
            intent.putExtra("MainActNamaAkun", arguments?.getString(ARG_RK_NAMA_AKUN))
            intent.putExtra("MainActNotes", arguments?.getString(ARG_RK_NOTES))
            startActivity(intent)
            dismiss()
        }

        btDelete.setOnClickListener {
            var dbManager = RekamanKeuanganDbManager(this.context!!)
            val selectionArgs = arrayOf(id.toString())
            dbManager.delete("Id=?", selectionArgs)
            (activity as RekamanKeuanganHomeActivity).loadQueryAll(arguments!!.getInt(ARG_RK_AKUN))
            Toast.makeText(context, "Berhasil menghapus record!", Toast.LENGTH_LONG).show()
            dismiss()
        }
    }
}