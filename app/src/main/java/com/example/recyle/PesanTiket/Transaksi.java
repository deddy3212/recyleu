package com.example.recyle.PesanTiket;

public class Transaksi {
    public String nama_pemesan,no_tlpon, email, rombongan_dari,tanggal, dewasa, anak_anak,
            total_pengunjung, total_bayar_dewasa,total_bayar_anak, total_bayar;
    public Transaksi() {
    }

    public Transaksi(String nama_pemesan, String no_tlpon, String email, String rombongan_dari, String tanggal, String dewasa,
                     String anak_anak, String total_pengunjung,
                     String total_bayar_dewasa, String total_bayar_anak, String total_bayar) {
        this.nama_pemesan = nama_pemesan;
        this.no_tlpon = no_tlpon;
        this.email = email;
        this.rombongan_dari = rombongan_dari;
        this.tanggal = tanggal;
        this.dewasa = dewasa;
        this.anak_anak = anak_anak;
        this.total_pengunjung = total_pengunjung;
        this.total_bayar_dewasa =total_bayar_dewasa;
        this.total_bayar_anak= total_bayar_anak;
        this.total_bayar= total_bayar;

    }

    public String getTotal_bayar_dewasa() {
        return total_bayar_dewasa;
    }

    public void setTotal_bayar_dewasa(String total_bayar_dewasa) {
        this.total_bayar_dewasa = total_bayar_dewasa;
    }

    public String getTotal_bayar_anak() {
        return total_bayar_anak;
    }

    public void setTotal_bayar_anak(String total_bayar_anak) {
        this.total_bayar_anak = total_bayar_anak;
    }

    public String getNama_pemesan() {
        return nama_pemesan;
    }



    public void setNama_pemesan(String nama_pemesan) {
        this.nama_pemesan = nama_pemesan;
    }

    public String getNo_tlpon() {
        return no_tlpon;
    }

    public void setNo_tlpon(String no_tlpon) {
        this.no_tlpon = no_tlpon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRombongan_dari() {
        return rombongan_dari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setRombongan_dari(String rombongan_dari) {
        this.rombongan_dari = rombongan_dari;
    }

    public String getDewasa() {
        return dewasa;
    }

    public void setDewasa(String dewasa) {
        this.dewasa = dewasa;
    }

    public String getAnak_anak() {
        return anak_anak;
    }

    public void setAnak_anak(String anak_anak) {
        this.anak_anak = anak_anak;
    }

    public String getTotal_pengunjung() {
        return total_pengunjung;
    }

    public void setTotal_pengunjung(String total_pengunjung) {
        this.total_pengunjung = total_pengunjung;
    }


    public String getTotal_bayar(){
        return total_bayar;
    }

    public  void setTotal_bayar (String total_bayar){
        this.total_bayar = total_bayar;
    }

}

