package com.example.recyle.MenuUser;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.AdapterMenuUser.LihatKadaluwarsa;
import com.example.recyle.AdapterMenuUser.LihatMenunggu;
import com.example.recyle.Model.ModelPesananTiket;
import com.example.recyle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Kadaluwarsa extends AppCompatActivity {

    FirebaseAuth mAuth;
    String email,uid;

    private FirebaseDatabase getDatabase;

    DatabaseReference reference;
    List<ModelPesananTiket> modelPesananTikets;
    ListView lvc1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_bayar);
        lvc1 =  findViewById(R.id.lv_tiket);

        getDatabase = FirebaseDatabase.getInstance();

        reference = getDatabase.getReference("Pesanan Tiket");
        mAuth = FirebaseAuth.getInstance();
        checkuserStatus();

        modelPesananTikets = new ArrayList<>();
        testampil(this, lvc1);

        lvc1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long uid) {
                return false;
            }
        });

        lvc1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long uid) {
                ModelPesananTiket ad = modelPesananTikets.get(position);
                Bundle bun= new Bundle();
                bun.putString("uid",ad.getUid());
                bun.putString("status", ad.getStatus());
            }
        });
        checkuserStatus();
        testampil(this, lvc1);
    }


    public void testampil (final Activity context, final ListView listView){
        final String st = FirebaseAuth.getInstance().getUid();

        Query query = FirebaseDatabase.getInstance().getReference("Pesanan Tiket")
                .orderByChild("uid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelPesananTikets.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ModelPesananTiket sepatu = postSnapshot.getValue(ModelPesananTiket.class);
                        modelPesananTikets.add(sepatu);

                    }
                    LihatKadaluwarsa sepatuAdapter = new LihatKadaluwarsa(context, modelPesananTikets);
                    listView.setAdapter((ListAdapter) sepatuAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

