package com.example.recyle.Model;

public class ModelBuktiPembayaran {
    String pId, pAtas_nama, pImage, pTime, uid, uEmail, uDp;

    public ModelBuktiPembayaran() {
    }

    public ModelBuktiPembayaran(String pId, String pAtas_nama, String pImage,
                                String pTime, String uid, String uEmail, String uDp) {
        this.pId = pId;
        this.pAtas_nama = pAtas_nama;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uDp = uDp;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpAtas_nama() {
        return pAtas_nama;
    }

    public void setpAtas_nama(String pAtas_nama) {
        this.pAtas_nama = pAtas_nama;
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
}
