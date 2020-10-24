package com.example.recyle.AdapterMenuUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recyle.Model.ModelPesananTiket;
import com.example.recyle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.WriterException;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LihatMenunggu extends ArrayAdapter<ModelPesananTiket> {
    private Activity context;
    private List<ModelPesananTiket> modelPesananTikets;

    FirebaseAuth mAuth;
    String email,uid;
    String pId_Pesanan;
    ImageView qrImage;

    Bitmap bitmap;

    public LihatMenunggu(Activity context, List<ModelPesananTiket> modelPesananTiketList) {
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
        TextView nama_pemesan = listViewItem.findViewById(R.id.tv_namapemesan);
        TextView no_tlp = listViewItem.findViewById(R.id.tv_notlp);
        TextView Email = listViewItem.findViewById(R.id.tv_email);
        TextView rombongan = listViewItem.findViewById(R.id.tv_rombongan);
        TextView dewasa = listViewItem.findViewById(R.id.tv_dewasa);
        TextView anak_anak = listViewItem.findViewById(R.id.tv_anak);
        TextView total_pengunjung = listViewItem.findViewById(R.id.tv_pengunjung);
        TextView total_dewasa = listViewItem.findViewById(R.id.tv_total_dewasa);
        TextView total_anak = listViewItem.findViewById(R.id.tv_total_anak);
        TextView total_bayar = listViewItem.findViewById(R.id.tv_total_bayar);
        TextView sta = listViewItem.findViewById(R.id.tv_status);
        ImageView qr = listViewItem.findViewById(R.id.tv_Qr);

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


        if (status.equals("Sudah Bayar")){
            ln.removeAllViewsInLayout();
        }
        if (status.equals("Kadaluwarsa")){
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




