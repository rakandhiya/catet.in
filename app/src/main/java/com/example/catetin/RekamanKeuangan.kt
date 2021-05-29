package com.example.catetin

class RekamanKeuangan {
    var id: Int? = null
    var nominal: Int? = null
    var kategori: String? = null
    var kategoriPosition: Int? = null
    var tanggal: String? = null
    var akun: Int? = null
    var namaAkun: String? = null
    var notes: String? = null
    var jenis: Int? = null

    constructor(id: Int, nominal: Int, jenis: Int, kategori: String, kategoriPosition: Int, tanggal: String, akun: Int, namaAkun: String, notes: String) {
        this.id = id
        this.nominal = nominal
        this.kategori = kategori
        this.kategoriPosition = kategoriPosition
        this.tanggal = tanggal
        this.akun = akun
        this.namaAkun = namaAkun
        this.notes = notes
        this.jenis = jenis
    }
}