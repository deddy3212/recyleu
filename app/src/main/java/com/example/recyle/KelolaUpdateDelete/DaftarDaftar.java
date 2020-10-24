package com.example.recyle.KelolaUpdateDelete;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Adapter.AdapterFasilitas;
import com.example.recyle.Adapter.AdapterPost;
import com.example.recyle.Adapter2.AdapterPost2;
import com.example.recyle.Model.ModelFasilitas;
import com.example.recyle.Model.ModelPost;
import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DaftarDaftar extends AppCompatActivity {

    RecyclerView recyclerView;

    List<ModelPost> postList;
    AdapterPost2 adapterPost2;

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
        postList = new ArrayList<>();
        loadPosts();
        return;
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);

                    //adapter
                    adapterPost2 = new AdapterPost2(getApplication(), postList);
                    recyclerView.setAdapter(adapterPost2);
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
