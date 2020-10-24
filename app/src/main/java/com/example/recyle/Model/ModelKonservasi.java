package com.example.recyle.Model;

public class ModelKonservasi {
    String pId, pJudul_Konservasi, pIsi_Konservasi, pImage, pTime, uid, uEmail, uDp, uName;

    public ModelKonservasi() {
    }

    public ModelKonservasi(String pId, String pJudul_Konservasi, String pIsi_Konservasi, String pImage,
                           String pTime, String uid, String uEmail, String uDp, String uName) {
        this.pId = pId;
        this.pJudul_Konservasi = pJudul_Konservasi;
        this.pIsi_Konservasi = pIsi_Konservasi;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.uName = uName;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpJudul_Konservasi() {
        return pJudul_Konservasi;
    }

    public void setpJudul_Konservasi(String pJudul_Konservasi) {
        this.pJudul_Konservasi = pJudul_Konservasi;
    }

    public String getpIsi_Konservasi() {
        return pIsi_Konservasi;
    }

    public void setpIsi_Konservasi(String pIsi_Konservasi) {
        this.pIsi_Konservasi = pIsi_Konservasi;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}