package com.example.recyle.Adapter2;

import android.annotation.TargetApi;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Admin.Admin_tambah_postingan;
import com.example.recyle.MenuTerkait.Fasilitasnya;
import com.example.recyle.Model.ModelFasilitas;
import com.example.recyle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Adapter_fasilitas2  extends RecyclerView.Adapter<Adapter_fasilitas2.MyHolder> {

    TextView tvbaca;
    Context context;
    List<ModelFasilitas> modelFasilitas;

    String myUid;

    public Adapter_fasilitas2(Context context, List<ModelFasilitas> modelFasilitas) {
        this.context = context;
        this.modelFasilitas = modelFasilitas;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_post.xml
        ViewGroup viewGroup = null;


        View view = LayoutInflater.from(context).inflate(R.layout.layout_delete, viewGroup, false);

        return new Adapter_fasilitas2.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_fasilitas2.MyHolder myHolder, int i) {

        //get data
        final String uid = modelFasilitas.get(i).getUid();
        String uEmail = modelFasilitas.get(i).getuEmail();
        String uName = modelFasilitas.get(i).getuEmail();
        String uDp = modelFasilitas.get(i).getuDp();
        final String pId = modelFasilitas.get(i).getpId();
        String pJudul = modelFasilitas.get(i).getpJudul_fasilitas();
        String pIsiPost = modelFasilitas.get(i).getpIsi_Fasilitas();
        final String pImage = modelFasilitas.get(i).getpImage();
        String pTimeStamp = modelFasilitas.get(i).getpTime();


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
        if (pImage.equals("noImage")) {
            //hide
            myHolder.pImaglv.setVisibility(View.GONE);

        } else {
            //show
            myHolder.pImaglv.setVisibility(View.VISIBLE);
        }
        try {
            Picasso.get().load(pImage).into(myHolder.pImaglv);
        } catch (Exception e) {

        }


        //handle button klik
        myHolder.more_BTN.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                showMoreOption(myHolder.more_BTN,uid, myUid, pId, pImage);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMoreOption(ImageButton more_btn, String uid, String myUid, final String pId, final String pImage) {
        //membuat pop up
        PopupMenu popupMenu = new PopupMenu(context, more_btn, Gravity.END);
        if (uid.equals(myUid)){
            //add item listener
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit");
        }

        //add click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id==0){
                    //delete di sini
                    deleteBegin(pId, pImage);
                }
                else if (id==1){
                    //edit di sini
                    Intent intent = new Intent(context, Fasilitasnya.class);
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

    private void deleteBegin(String pId, String pImage) {
        if (pImage.equals("noImage")){
            deletetidakdengangambar(pId);

        }
        else {
            deletedengangambar(pId, pImage);
        }
    }

    private void deletetidakdengangambar(final String pId) {

        final ProgressDialog pd = new  ProgressDialog(context);
        pd.setMessage("Hapus.....");

        Query fquery = FirebaseDatabase.getInstance().
                getReference("Posts").orderByChild("pId").equalTo(pId);
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

    private void deletedengangambar(final String pId, String pImage) {
        //progres bar
        final ProgressDialog pd = new  ProgressDialog(context);
        pd.setMessage("Hapus.....");

        /*cara
        1) menghapus gambar menggunakan url
        2) menghapus dari database menggunakan post id
         */

        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //hapus gambar, sekarang hapus database
                        Query fquery = FirebaseDatabase.getInstance().
                                getReference("Posts").orderByChild("pId").equalTo(pId);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //jika gagal
                        pd.dismiss();
                        Toast.makeText(context, "" +e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    @Override
    public int getItemCount() {


        return modelFasilitas.size();
    }




    //views holder class
    public static class MyHolder extends RecyclerView.ViewHolder{

        //views from row_post.xml
        ImageView uPicturelv, pImaglv;
        ImageButton more_BTN;
        TextView uNameTV, pTimeTV, pJudulTV, pIsiPostinganTV, pLikeTV,bacaa;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init Views
            uPicturelv = itemView.findViewById(R.id.uPictureTV);
            pImaglv = itemView.findViewById(R.id.pImageTV);
            uNameTV = itemView.findViewById(R.id.uNameTV);
            pTimeTV = itemView.findViewById(R.id.pTimeTV);
            pJudulTV = itemView.findViewById(R.id.pJudulTV);
            pIsiPostinganTV = itemView.findViewById(R.id.pIsiPostinganTV);
            pLikeTV = itemView.findViewById(R.id.pLikeTV);
            more_BTN = itemView.findViewById(R.id.pMoreTV);
        }
    }

}
