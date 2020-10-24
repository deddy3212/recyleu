package com.example.recyle.MenuTerkait;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Adapter.AdapterKonservasi;
import com.example.recyle.Adapter.AdapterRekreasi;
import com.example.recyle.Model.ModelKonservasi;
import com.example.recyle.Model.ModelRekreasi;
import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Konservasinya extends AppCompatActivity {

    RecyclerView recyclerView;

    List<ModelKonservasi> konservasiList;
    AdapterKonservasi adapterKonservasi;

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
        konservasiList = new ArrayList<>();
        loadPosts();
        return;
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Konservasi");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                konservasiList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelKonservasi modelKonservasi = ds.getValue(ModelKonservasi.class);

                    konservasiList.add(modelKonservasi);


                    //adapter
                    adapterKonservasi = new AdapterKonservasi(getApplication(), konservasiList);
                    recyclerView.setAdapter(adapterKonservasi);
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

