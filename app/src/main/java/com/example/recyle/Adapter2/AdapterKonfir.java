package com.example.recyle.Adapter2;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Admin.Admin_tambah_postingan;
import com.example.recyle.Konfirmasi.KonfirmasiBayaran;
import com.example.recyle.Model.ModelPesananTiket;
import com.example.recyle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AdapterKonfir extends RecyclerView.Adapter<AdapterKonfir.MyHolder> {

    TextView tvbaca;
    Context context;
    List<ModelPesananTiket> pesananTiketList;


    public AdapterKonfir(Context context, List<ModelPesananTiket> pesananTiketList) {
        this.context = context;
        this.pesananTiketList = pesananTiketList;
    }

    @NonNull
    @Override
    public AdapterKonfir.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_post.xml
        ViewGroup viewGroup = null;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_konfirmasi, viewGroup, false);
        return new AdapterKonfir.MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final AdapterKonfir.MyHolder myHolder, int i) {
        //get data
        final String uid = pesananTiketList.get(i).getUid();
        String uEmail = pesananTiketList.get(i).getuEmail();
        String uName = pesananTiketList.get(i).getuEmail();
        String uDp = pesananTiketList.get(i).getuDp();
        final String pId_Pesanan = pesananTiketList.get(i).getpId_Pesanan();
        String pNama_Pemesan = pesananTiketList.get(i).getpNama_Pemesan();
        String status = pesananTiketList.get(i).getStatus();
        String pTimeStamp = pesananTiketList.get(i).getpTime();


        //convert timstamp todd/mm/yy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //ser data
        myHolder.idnya.setText(pId_Pesanan);
        myHolder.pnama.setText(pNama_Pemesan);
        myHolder.pTimeTV.setText(pTime);
        myHolder.pstatus.setText(status);
        if (status.equals("Sudah Bayar")){
            myHolder.pstatus.setTextColor(Color.GREEN);
        } else {
            myHolder.pstatus.setTextColor(Color.RED);
        }
        //set user dp
        //set post image

        //handle button klik
        myHolder.more_BTN.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                showMoreOption(myHolder.more_BTN, pId_Pesanan);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMoreOption(ImageButton more_btn,  final String pId_Pesanan) {
        //membuat pop up
        PopupMenu popupMenu = new PopupMenu(context, more_btn, Gravity.END);
            //add item listener
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit");

        //add click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id==0){
                    //delete di sini
                    deleteBegin(pId_Pesanan);
                }
                else if (id==1){
                    //edit di sini
                    Intent intent = new Intent(context, KonfirmasiBayaran.class);
                    intent.putExtra("key", "editPost");
                    intent.putExtra("editPostId", pId_Pesanan);
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
                getReference("Pesanan Tiket").orderByChild("pTime").equalTo(pTime);
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

    private void warnastatus (){
    }



    @Override
    public int getItemCount() {


        return pesananTiketList.size();
    }

    //views holder class
    public static class MyHolder extends RecyclerView.ViewHolder{

        //views from row_post.xml
        ImageButton more_BTN;
        TextView idnya, pTimeTV, pnama, pstatus;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init Views
            idnya = itemView.findViewById(R.id.u_id_pesanan);
            pTimeTV = itemView.findViewById(R.id.pTimeTV);
            pnama = itemView.findViewById(R.id.uName);
            pstatus = itemView.findViewById(R.id.pstatusnya);
            more_BTN = itemView.findViewById(R.id.pMoreTV);
        }
    }
}
