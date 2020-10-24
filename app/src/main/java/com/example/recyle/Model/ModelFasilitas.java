package com.example.recyle.Model;

public class ModelFasilitas {
    String pId, pJudul_fasilitas, pIsi_Fasilitas, pImage, pTime, uid, uEmail, uDp, uName;

    public ModelFasilitas() {
    }

    public ModelFasilitas(String pId, String pJudul_fasilitas, String pIsi_Fasilitas,
                          String pImage, String pTime, String uid, String uEmail, String uDp, String uName) {
        this.pId = pId;
        this.pJudul_fasilitas = pJudul_fasilitas;
        this.pIsi_Fasilitas = pIsi_Fasilitas;
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

    public String getpJudul_fasilitas() {
        return pJudul_fasilitas;
    }

    public void setpJudul_fasilitas(String pJudul_fasilitas) {
        this.pJudul_fasilitas = pJudul_fasilitas;
    }

    public String getpIsi_Fasilitas() {
        return pIsi_Fasilitas;
    }

    public void setpIsi_Fasilitas(String pIsi_Fasilitas) {
        this.pIsi_Fasilitas = pIsi_Fasilitas;
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