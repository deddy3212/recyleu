package com.example.recyle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Model.ModelFasilitas;
import com.example.recyle.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class AdapterFasilitas extends RecyclerView.Adapter<AdapterFasilitas.MyHolder> {

    TextView tvbaca;
    Context context;
    List<ModelFasilitas> modelFasilitas;

    public AdapterFasilitas(Context context, List<ModelFasilitas> modelFasilitas) {
        this.context = context;
        this.modelFasilitas = modelFasilitas;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_post.xml
        ViewGroup viewGroup = null;

        View view = LayoutInflater.from(context).inflate(R.layout.row_fasilitas, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //get data
        String uid = modelFasilitas.get(i).getpId();
        String uEmail = modelFasilitas.get(i).getuEmail();
        String uName = modelFasilitas.get(i).getuEmail();
        String uDp = modelFasilitas.get(i).getuDp();
        String pId = modelFasilitas.get(i).getpId();
        String getpJudulFasilitas = modelFasilitas.get(i).getpJudul_fasilitas();
        String pIsiFasilitas = modelFasilitas.get(i).getpIsi_Fasilitas();
        String pImage = modelFasilitas.get(i).getpImage();
        String pTimeStamp = modelFasilitas.get(i).getpTime();



        //convert timstamp todd/mm/yy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //ser data
        myHolder.uNameTV.setText(uName);
        myHolder.pTimeTV.setText(pTime);
        myHolder.pJudulTV.setText(getpJudulFasilitas);
        myHolder.pIsiPostinganTV.setText(pIsiFasilitas);



        //set user dp
        //set post image
        if (pImage.equals("noImage")){
            //hide
            myHolder.pImaglv.setVisibility(View.GONE);

        }else {

        }try {
            Picasso.get().load(pImage).into(myHolder.pImaglv);
        }
        catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {


        return modelFasilitas.size();
    }




    //views holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views from row_post.xml
        ImageView uPicturelv, pImaglv;
        ImageButton more_BTN;
        TextView uNameTV, pTimeTV, pJudulTV, pIsiPostinganTV;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init Views
            uPicturelv = itemView.findViewById(R.id.uPictureTV);
            pImaglv = itemView.findViewById(R.id.pImageTV);
            uNameTV = itemView.findViewById(R.id.uNameTV);
            pTimeTV = itemView.findViewById(R.id.pTimeTV);
            pJudulTV = itemView.findViewById(R.id.pJudulTV);
            pIsiPostinganTV = itemView.findViewById(R.id.pIsiPostinganTV);

        }
    }
}
