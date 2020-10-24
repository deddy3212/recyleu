package com.example.recyle.AdapterMenuUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.recyle.Model.ModelPesananTiket;
import com.example.recyle.R;
import com.example.recyle.PesanTiket.Transaksi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.Long.parseLong;

public class LihatTiket extends ArrayAdapter<ModelPesananTiket> {
    private Activity context;
    private List<ModelPesananTiket> modelPesananTikets;

    FirebaseAuth mAuth;
    String email,uid;
    String pId_Pesanan;
    ImageView qrImage;

    Bitmap bitmap;

    public LihatTiket(Activity context, List<ModelPesananTiket> modelPesananTiketList) {
        super(context, R.layout.list_tiket, modelPesananTiketList);
        this.context = context;
        this.modelPesananTikets = modelPesananTiketList;

        mAuth = FirebaseAuth.getInstance();
        checkuserStatus();

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_tiket, null, true);
        final TextView nama_pemesan = listViewItem.findViewById(R.id.tv_namapemesan);
        final TextView no_tlp = listViewItem.findViewById(R.id.tv_notlp);
        final TextView Email = listViewItem.findViewById(R.id.tv_email);
        final TextView rombongan = listViewItem.findViewById(R.id.tv_rombongan);
        final TextView dewasa = listViewItem.findViewById(R.id.tv_dewasa);
        final TextView anak_anak = listViewItem.findViewById(R.id.tv_anak);
        final TextView total_pengunjung = listViewItem.findViewById(R.id.tv_pengunjung);
        final TextView total_dewasa = listViewItem.findViewById(R.id.tv_total_dewasa);
        final TextView total_anak = listViewItem.findViewById(R.id.tv_total_anak);
        final TextView total_bayar = listViewItem.findViewById(R.id.tv_total_bayar);
        final TextView sta = listViewItem.findViewById(R.id.tv_status);
        final ImageView qr = listViewItem.findViewById(R.id.tv_Qr);



        LinearLayout ln = listViewItem.findViewById(R.id.lini_lihatTiket);

        ModelPesananTiket modelPesananTiket = modelPesananTikets.get(position);
        //get
        nama_pemesan.setText(modelPesananTiket.getpNama_Pemesan());
        no_tlp.setText(modelPesananTiket.getpNotelpon());
        Email.setText(modelPesananTiket.getpEmail());
        rombongan.setText(modelPesananTiket.getpRombongan_dari());
        dewasa.setText(modelPesananTiket.getpDewasa());
        anak_anak.setText(modelPesananTiket.getpAnak_anak());
        total_pengunjung.setText(modelPesananTiket.getpTotal_pengunjung());
        total_dewasa.setText(modelPesananTiket.getpTotal_bayar_dewasa());
        total_anak.setText(modelPesananTiket.getpTotal_bayar_anak());
        total_bayar.setText(modelPesananTiket.getpTotal_bayar());
        sta.setText(modelPesananTiket.getStatus());

        pId_Pesanan = modelPesananTiket.getpId_Pesanan().toString();
        qrImage = qr;

        String status = sta.getText().toString();
        if (status.equals("Belum Bayar")){
            ln.removeAllViewsInLayout();
        }

        if (status.equals("Sudah Bayar") ){

            qr.setVisibility(View.VISIBLE);
            Qrnya();
        }
        else if (status.equals("Kadaluwarsa")){
            ln.removeAllViewsInLayout();
        }

        else {
            qr.setVisibility(View.INVISIBLE);
            qr.getLayoutParams().height = 0;
            qr.getLayoutParams().width = 0;

        }
        return listViewItem;
    }


    private void Qrnya() {
        QRGEncoder qrgEncoder = new QRGEncoder(pId_Pesanan, null, QRGContents.Type.TEXT, 700);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    }





    private void checkuserStatus() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            //user is signed
            //set email of logged in user
            email = user.getEmail();
            uid = user.getUid();
        }

    }
}
