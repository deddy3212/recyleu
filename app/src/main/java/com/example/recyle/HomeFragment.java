package com.example.recyle;


import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Adapter.AdapterPost;
import com.example.recyle.Model.ModelPost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPost adapterPost;
    String pId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragmet_home,container,false);

        //recycle view and its properties
        recyclerView = v.findViewById(R.id.postsRecyclerview);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());

       //show newest post first, for this load  from last
        LayoutManager.setStackFromEnd(true);
        LayoutManager.setReverseLayout(true);

        //set layout to recyclerviews
        recyclerView.setLayoutManager(LayoutManager);


        //init post list
        postList = new ArrayList<>();
        loadPosts();
        return v;

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
                    adapterPost = new AdapterPost(getActivity(), postList);
                   //set adapter to recyclerview
                }
                recyclerView.setAdapter(adapterPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searcPost(final String searchQuery){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);


                    if (modelPost.getpJudul().toLowerCase().contains(searchQuery.toLowerCase()) ||
                             modelPost.getpIsiPost().toLowerCase().contains(searchQuery.toLowerCase())){
                        postList.add(modelPost);

                    }

                    //adapter
                    adapterPost = new AdapterPost(getActivity(), postList);


                    //set adapter to recyclerview

                }
                recyclerView.setAdapter(adapterPost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }








}
