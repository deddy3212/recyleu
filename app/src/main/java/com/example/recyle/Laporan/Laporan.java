package com.example.recyle.Laporan;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Adapter.AdapterPesanan;
import com.example.recyle.Model.ModelPesananTiket;
import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Laporan extends AppCompatActivity {

    RecyclerView recyclerView;

    List<ModelPesananTiket> modelPesananTikets;
    AdapterPesanan adapterPesanan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmet_home);
        recyclerView = findViewById(R.id.postsRecyclerview);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(getApplicationContext());

        //show newest post first, for this load  from last
        LayoutManager.setStackFromEnd(true);
        LayoutManager.setReverseLayout(true);

        //set layout to recyclerviews
        recyclerView.setLayoutManager(LayoutManager);

        //init post list
        modelPesananTikets = new ArrayList<>();
        loadPosts();
        return;

    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Pesanan Tiket");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelPesananTikets.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPesananTiket modelPesananTiket = ds.getValue(ModelPesananTiket.class);

                    modelPesananTikets.add(modelPesananTiket);


                    //adapter
                    adapterPesanan = new AdapterPesanan(getApplication(), modelPesananTikets);
                    recyclerView.setAdapter(adapterPesanan);
                    //set adapter to recyclerview

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplication(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
