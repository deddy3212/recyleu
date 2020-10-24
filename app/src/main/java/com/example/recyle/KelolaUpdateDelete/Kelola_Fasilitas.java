package com.example.recyle.KelolaUpdateDelete;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Adapter2.Adapter_fasilitas2;
import com.example.recyle.Model.ModelFasilitas;
import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Kelola_Fasilitas extends AppCompatActivity {
    RecyclerView recyclerView;

    List<ModelFasilitas> fasilitasList;
    Adapter_fasilitas2 adapterFasilitas2;

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
        fasilitasList = new ArrayList<>();
        loadPosts();
        return;
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Fasilitas");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fasilitasList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelFasilitas modelFasilitas = ds.getValue(ModelFasilitas.class);

                    fasilitasList.add(modelFasilitas);

                    //adapter
                    adapterFasilitas2 = new Adapter_fasilitas2(getApplication(), fasilitasList);
                    recyclerView.setAdapter(adapterFasilitas2);
                    //set adapter to recyclerview

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplication(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
