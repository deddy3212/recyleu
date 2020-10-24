package com.example.recyle.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Model.ModelPost;
import com.example.recyle.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.zip.DataFormatException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder> {

    TextView tvbaca;
    Context context;
    List<ModelPost> postList;
    public AdapterPost(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_post.xml
        ViewGroup viewGroup = null;
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        //get data
        final String uid = postList.get(i).getUid();
        String uEmail = postList.get(i).getuEmail();
        String uName = postList.get(i).getuEmail();
        String uDp = postList.get(i).getuDp();
        final String pId = postList.get(i).getpId();
        String pJudul = postList.get(i).getpJudul();
        String pIsiPost = postList.get(i).getpIsiPost();
        final String pImage = postList.get(i).getpImage();
        String pTimeStamp = postList.get(i).getpTime();

        //convert timstamp todd/mm/yy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //ser data
        myHolder.uNameTV.setText(uName);
        myHolder.pTimeTV.setText(pTime);
        myHolder.pJudulTV.setText(pJudul);
        myHolder.pIsiPostinganTV.setText(pIsiPost);
        //set user dp
        //set post image
        if (pImage.equals("noImage")){
            //hide
            myHolder.pImaglv.setVisibility(View.GONE);
        }else {
            //show
            myHolder.pImaglv.setVisibility(View.VISIBLE);
        }try {
            Picasso.get().load(pImage).into(myHolder.pImaglv);
        }
        catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    //views holder class
    public static class MyHolder extends RecyclerView.ViewHolder{

        //views from row_post.xml
        ImageView  uPicturelv, pImaglv;
        TextView uNameTV, pTimeTV, pJudulTV, pIsiPostinganTV, pLikeTV;


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
