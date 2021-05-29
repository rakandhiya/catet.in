package com.example.catetin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.akun_bottomsheet_fragment.*

class AkunBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        const val ARG_AKUN_ID = "id"
        const val ARG_AKUN_NAMA = "nama"

        fun newInstance(id: Int, name: String?): AkunBottomSheetFragment {
            val fragment = AkunBottomSheetFragment()
            val bundle = Bundle().apply {
                putInt(ARG_AKUN_ID, id)
                putString(ARG_AKUN_NAMA, name)
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
        return inflater.inflate(R.layout.akun_bottomsheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(ARG_AKUN_ID)
        val name = arguments?.getString(ARG_AKUN_NAMA)

        btDetail.setOnClickListener {
            var intent = Intent(context, RekamanKeuanganHomeActivity::class.java)
            intent.putExtra("MainActIdAkun", id)
            intent.putExtra("MainActNamaAkun", name)
            startActivity(intent)
            dismiss()
        }

        btEdit.setOnClickListener {
            var intent = Intent(context, AkunActivity::class.java)
            intent.putExtra("MainActId", id)
            intent.putExtra("MainActNama", name)
            startActivity(intent)
            dismiss()
        }

        btDelete.setOnClickListener {
            var dbManager = AkunDbManager(this.context!!)
            val selectionArgs = arrayOf(id.toString())
            dbManager.delete("Id=?", selectionArgs)
            (activity as MainActivity).loadQueryAll()
            Toast.makeText(context, "Berhasil menghapus record!", Toast.LENGTH_LONG).show()
            dismiss()
        }
    }
}