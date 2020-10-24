package com.example.recyle.MenuTerkait;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Adapter.AdapterInteraksi;
import com.example.recyle.Adapter.AdapterRekreasi;
import com.example.recyle.Model.ModelInteraksi;
import com.example.recyle.Model.ModelRekreasi;
import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Rekreasinya extends AppCompatActivity {
    RecyclerView recyclerView;

    List<ModelRekreasi> rekreasiList;
    AdapterRekreasi adapterRekreasi;

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
        rekreasiList = new ArrayList<>();
        loadPosts();
        return;
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Rekreasi");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rekreasiList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelRekreasi modelRekreasi = ds.getValue(ModelRekreasi.class);

                    rekreasiList.add(modelRekreasi);


                    //adapter
                    adapterRekreasi = new AdapterRekreasi(getApplication(), rekreasiList);
                    recyclerView.setAdapter(adapterRekreasi);
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