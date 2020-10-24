package com.example.recyle.Adapter;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Konfirmasi.KonfirmasiBayaran;
import com.example.recyle.Model.ModelBuktiPembayaran;
import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterBuktiPembayaran extends RecyclerView.Adapter<AdapterBuktiPembayaran.MyHolder> {
    Context context;
    List<ModelBuktiPembayaran> modelBuktiPembayaran;

    public AdapterBuktiPembayaran(Context context, List<ModelBuktiPembayaran> modelBuktiPembayaranList) {
        this.context = context;
        this.modelBuktiPembayaran = modelBuktiPembayaranList;
    }

    @NonNull
    @Override
    public AdapterBuktiPembayaran.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_post.xml
        ViewGroup viewGroup = null;
        View view = LayoutInflater.from(context).inflate(R.layout.row_bukti_pembayaran, viewGroup, false);

        return new AdapterBuktiPembayaran.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {
        String uid = modelBuktiPembayaran.get(i).getUid();
        String uEmail = modelBuktiPembayaran.get(i).getuEmail();
        String uName = modelBuktiPembayaran.get(i).getuEmail();
        String uDp = modelBuktiPembayaran.get(i).getuDp();
        final String pId = modelBuktiPembayaran.get(i).getpId();
        String pAtas_nama = modelBuktiPembayaran.get(i).getpAtas_nama();
        final String pImage = modelBuktiPembayaran.get(i).getpImage();
        String pTimeStamp = modelBuktiPembayaran.get(i).getpTime();


        //convert timstamp todd/mm/yy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //set data
        myHolder.uNameTV.setText(pAtas_nama);
        myHolder.pTimeTV.setText(pTime);
        myHolder.idnya.setText(pId);


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

        //handle button klik
        myHolder.more.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                showMoreOption((ImageButton) myHolder.more, pId);
            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMoreOption(ImageButton more_btn, final String pId) {
        //membuat pop up
        PopupMenu popupMenu = new PopupMenu(context, more_btn, Gravity.END);
        //add item listener
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");

        //add click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id==0){
                    //delete di sini
                    deleteBegin(pId);
                }
                else if (id==1){
                    //edit di sini
                    Intent intent = new Intent(context, KonfirmasiBayaran.class);
                    intent.putExtra("key", "editPost");
                    intent.putExtra("editPostId", pId);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }

                return false;
            }
        });
        //show menu
        popupMenu.show();

    }

    private void deleteBegin(String pId) {
        deletetidakdengangambar(pId);

    }

    private void deletetidakdengangambar(final String pTime) {

        final ProgressDialog pd = new  ProgressDialog(context);
        pd.setMessage("Hapus.....");

        Query fquery = FirebaseDatabase.getInstance().
                getReference("Bukti Pembayaran").orderByChild("pTime").equalTo(pTime);
        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ds.getRef().removeValue();

                }
                Toast.makeText(context, "Hapus berhasil", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {


        return modelBuktiPembayaran.size();
    }


    //views holder class
    class MyHolder extends RecyclerView.ViewHolder {

        TextView uNameTV, pTimeTV, idnya, emailTV;
             ImageView pImaglv, more;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init Views
            uNameTV = itemView.findViewById(R.id.namapemesan);
            idnya = itemView.findViewById(R.id.id);
            emailTV = itemView.findViewById(R.id.emailnya);
            pTimeTV = itemView.findViewById(R.id.pJudulTV);
            pImaglv = itemView.findViewById(R.id.pImageTV);
            more = itemView.findViewById(R.id.pMore);


        }
    }
}
