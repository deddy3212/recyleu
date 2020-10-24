package com.example.recyle.Konfirmasi;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recyle.Adapter2.AdapterKonfir;
import com.example.recyle.Model.ModelPesananTiket;
import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

public class Konfirmasilah extends AppCompatActivity {

    private MaterialSearchView mMaterialSearchView;
    RecyclerView recyclerView;

    List<ModelPesananTiket> modelPesananTikets;
    AdapterKonfir adapterKonfir;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirmasi_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        mMaterialSearchView = findViewById(R.id.search_view);
        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Pesanan Tiket");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        modelPesananTikets.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelPesananTiket modelPesananTiket = ds.getValue(ModelPesananTiket.class);

                            if (modelPesananTiket.getpId_Pesanan().toLowerCase().contains(newText.toLowerCase()) ||
                                    modelPesananTiket.getpNama_Pemesan().toLowerCase().contains(newText.toLowerCase())) {
                                modelPesananTikets.add(modelPesananTiket);

                            }

                            //adapter
                            adapterKonfir = new AdapterKonfir(getApplicationContext(), modelPesananTikets);

                            //set adapter to recyclerview

                        }
                        recyclerView.setAdapter(adapterKonfir);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplication(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });
        recyclerView = findViewById(R.id.postsRecyclerview_konfirmasi);
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
                    ModelPesananTiket mdl = ds.getValue(ModelPesananTiket.class);

                    modelPesananTikets.add(mdl);

                    //adapter
                    adapterKonfir = new AdapterKonfir(getApplication(), modelPesananTikets);
                    recyclerView.setAdapter(adapterKonfir);
                    //set adapter to recyclerview
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplication(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.caridata,menu);

        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        mMaterialSearchView.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }
}