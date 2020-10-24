package com.example.recyle.Adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recyle.Model.ModelPesananTiket;
import com.example.recyle.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPesanan extends RecyclerView.Adapter<AdapterPesanan.MyHolder> {
    Context context;
    List<ModelPesananTiket> modelPesananTiketList;
    int pageWidht = 1200;

    public AdapterPesanan(Context context, List<ModelPesananTiket> pesananTiketList) {
        this.context = context;
        this.modelPesananTiketList = pesananTiketList;
    }

    @NonNull
    @Override
    public AdapterPesanan.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = null;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pendapatan, viewGroup, false);

    return new AdapterPesanan.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int i) {
        String uid = modelPesananTiketList.get(i).getUid();
        String uEmail = modelPesananTiketList.get(i).getuEmail();
        String uName = modelPesananTiketList.get(i).getuEmail();
        String uDp = modelPesananTiketList.get(i).getuDp();
        String pId = modelPesananTiketList.get(i).getpId_Pesanan();
        String pNama_Pemesan = modelPesananTiketList.get(i).getpNama_Pemesan();
        final String pNotelpon = modelPesananTiketList.get(i).getpNotelpon();
        String pEmail = modelPesananTiketList.get(i).getpEmail();
        String pRombongan_dari = modelPesananTiketList.get(i).getpRombongan_dari();
        String pTanggal = modelPesananTiketList.get(i).getpTanggal();
        String pAnak_anak = modelPesananTiketList.get(i).getpAnak_anak();
        String pDewasa = modelPesananTiketList.get(i).getpDewasa();
        String pTotal_bayar = modelPesananTiketList.get(i).getpTotal_bayar();
        String pTotal_bayar_anak = modelPesananTiketList.get(i).getpTotal_bayar_anak();
        String pTotal_bayar_dewasa = modelPesananTiketList.get(i).getpTotal_bayar_dewasa();
        final String pTotal_pengunjung = modelPesananTiketList.get(i).getpTotal_pengunjung();
        String pTimeStamp = modelPesananTiketList.get(i).getpTime();
        String status = modelPesananTiketList.get(i).getStatus();
        //convert timstamp todd/mm/yy hh:mm am/pm
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //set data
        holder.pnama.setText(pNama_Pemesan);
        holder.pnohp.setText(pNotelpon);
        holder.prombong.setText(pRombongan_dari);
        holder.pstatus.setText(status);

        holder.pTimeTV.setText(pTime);
        holder.total_pengunjungTV.setText(pTotal_pengunjung);
        holder.total_pendapatan.setText(pTotal_bayar);


        holder.ctkpdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String tanggalnya = holder.pTimeTV.getText().toString();
                String pengunjungnya = holder.total_pengunjungTV.getText().toString();
                String totalnya = holder.total_pendapatan.getText().toString();
                String nmpembeli = holder.pnama.getText().toString();
                String no = holder.pnohp.getText().toString();
                String st = holder.pstatus.getText().toString();
                String rm = holder.prombong.getText().toString();

                PdfDocument myPdfDocument = new PdfDocument();
                Paint myPaint = new Paint();
                Paint titlePaint = new Paint();

                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder
                        (1200, 2010, 1).create();
                PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                Canvas canvas = myPage1.getCanvas();

                myPaint.setColor(Color.rgb(0, 113, 188));
                myPaint.setTextSize(30f);
                myPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("Call Center: 0822335588", 1160, 40, myPaint);
                canvas.drawText("Email: Deddysupa@gmail.com", 1160, 80, myPaint);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(70);
                canvas.drawText("E-Zoo Laporan Pembelian", pageWidht/2,210,myPaint);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(70);
                canvas.drawText("Catatan/Struk Pembelian", pageWidht/2, 500,titlePaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Nama Pembeli : " +nmpembeli, 20, 590, myPaint);
                canvas.drawText("Nomor Hp : " +no, 20, 640, myPaint);
                canvas.drawText("Status : " + st, 20, 690, myPaint);
                canvas.drawText("Rombongan Dari : " + rm, 20, 740, myPaint);


                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(2);
                canvas.drawRect(20, 780, pageWidht-20, 860, myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setStyle(Paint.Style.FILL);
                canvas.drawText("no", 40,830, myPaint);
                canvas.drawText("Tanggal", 200, 830, myPaint);
                canvas.drawText("Pengunjung", 700, 830, myPaint);
                canvas.drawText("Total", 1050, 830, myPaint);


                canvas.drawLine(180, 790, 180, 840, myPaint);
                canvas.drawLine(680, 790, 680, 840, myPaint);
                canvas.drawLine(880, 790, 880, 840, myPaint);
                canvas.drawLine(1030, 790, 1030, 840, myPaint);


                canvas.drawText("1", 40, 950, myPaint);
                canvas.drawText(tanggalnya, 200, 950, myPaint);
                canvas.drawText(pengunjungnya, 700, 950, myPaint);
                myPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(totalnya, pageWidht-40, 950, myPaint);


                myPdfDocument.finishPage(myPage1);
                String mFilename = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(System.currentTimeMillis());

                File file = new File(Environment.getExternalStorageDirectory() +"/"+ mFilename + ".pdf");

                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myPdfDocument.close();
                Toast.makeText(context, "file Pdf tersimpan di dokumen", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return modelPesananTiketList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView pTimeTV, total_pengunjungTV, total_pendapatan, pnama, pnohp, prombong, pstatus;
        Button ctkpdf;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init Views
            pTimeTV = itemView.findViewById(R.id.tanggal_pendapatan);
            total_pengunjungTV = itemView.findViewById(R.id.jml_pengunjung_hari_ini);
            total_pendapatan = itemView.findViewById(R.id.ttl);
            pnama = itemView.findViewById(R.id.deddysupa);
            pnohp = itemView.findViewById(R.id.nomohp);
            prombong = itemView.findViewById(R.id.yogya);
            pstatus = itemView.findViewById(R.id.sta);

            ctkpdf = itemView.findViewById(R.id.btn_ctk);

        }
    }

}