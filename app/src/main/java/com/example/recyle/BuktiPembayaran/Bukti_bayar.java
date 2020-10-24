package com.example.recyle.BuktiPembayaran;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Adapter.AdapterBuktiPembayaran;
import com.example.recyle.Adapter.AdapterFasilitas;
import com.example.recyle.Adapter2.AdapterKonfir;
import com.example.recyle.Model.ModelBuktiPembayaran;
import com.example.recyle.Model.ModelFasilitas;
import com.example.recyle.Model.ModelPesananTiket;
import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class Bukti_bayar extends AppCompatActivity {

    RecyclerView recyclerView;
    private MaterialSearchView mMaterialSearchView1;

    List<ModelBuktiPembayaran> buktiPembayaranList;
    AdapterBuktiPembayaran adapterBuktiPembayaran;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bukti);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        mMaterialSearchView1 = findViewById(R.id.search_view1);
        mMaterialSearchView1.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
        mMaterialSearchView1.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bukti Pembayaran");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        buktiPembayaranList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelBuktiPembayaran modelBuktiPembayaran = ds.getValue(ModelBuktiPembayaran.class);

                            if (modelBuktiPembayaran.getpAtas_nama().toLowerCase().contains(newText.toLowerCase()) ||
                                    modelBuktiPembayaran.getpId().toLowerCase().contains(newText.toLowerCase())) {
                                buktiPembayaranList.add(modelBuktiPembayaran);
                            }
                            //adapter
                            adapterBuktiPembayaran = new AdapterBuktiPembayaran(getApplicationContext(), buktiPembayaranList);
                            //set adapter to recyclerview

                        }
                        recyclerView.setAdapter(adapterBuktiPembayaran);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplication(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });
        recyclerView = findViewById(R.id.postsRecyclerview_bukti);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getApplicationContext());
        //show newest post first, for this load  from last
        LayoutManager.setStackFromEnd(true);
        LayoutManager.setReverseLayout(true);
        //set layout to recyclerviews
        recyclerView.setLayoutManager(LayoutManager);
        //init post list
        buktiPembayaranList = new ArrayList<>();
        loadPosts();
        return;
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bukti Pembayaran");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                buktiPembayaranList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelBuktiPembayaran modelBuktiPembayaran = ds.getValue(ModelBuktiPembayaran.class);

                    buktiPembayaranList.add(modelBuktiPembayaran);

                    //adapter
                    adapterBuktiPembayaran = new AdapterBuktiPembayaran(getApplication(), buktiPembayaranList);
                    recyclerView.setAdapter(adapterBuktiPembayaran);
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
        mMaterialSearchView1.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }
}
