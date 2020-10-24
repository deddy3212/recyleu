package com.example.recyle.Model;

public class ModelInteraksi {
    String pId, pJudul_Interaksi, pIsi_Interaksi, pImage, pTime, uid, uEmail, uDp, uName;

    public ModelInteraksi() {
    }

    public ModelInteraksi(String pId, String pJudul_Interaksi, String pIsi_Interaksi,
                          String pImage, String pTime, String uid, String uEmail, String uDp, String uName) {
        this.pId = pId;
        this.pJudul_Interaksi = pJudul_Interaksi;
        this.pIsi_Interaksi = pIsi_Interaksi;
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

    public String getpJudul_Interaksi() {
        return pJudul_Interaksi;
    }

    public void setpJudul_Interaksi(String pJudul_Interaksi) {
        this.pJudul_Interaksi = pJudul_Interaksi;
    }

    public String getpIsi_Interaksi() {
        return pIsi_Interaksi;
    }

    public void setpIsi_Interaksi(String pIsi_Interaksi) {
        this.pIsi_Interaksi = pIsi_Interaksi;
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