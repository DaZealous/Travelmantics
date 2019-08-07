package www.andela.com.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import adapter.MyAdapter;
import model.ResortHelper;

public class UserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    List<ResortHelper> list;
    ValueEventListener listener;
    DatabaseReference resortRef;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    String userId;
    ProgressBar userProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        recyclerView = findViewById(R.id.user_recycler_view);
        userProgress = findViewById(R.id.user_progress_bar);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        list = new ArrayList<>();

        myAdapter = new MyAdapter(this, list);

        resortRef = mDatabase.getReference().child("Resort").child(userId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if(dataSnapshot.getChildrenCount() <= 0) {
                    userProgress.setVisibility(View.GONE);
                    Toast.makeText(UserActivity.this, "No Deals Yet", Toast.LENGTH_SHORT).show();
                }else{
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ResortHelper resortHelper = snapshot.getValue(ResortHelper.class);
                    list.add(resortHelper);
                }
                }
                Collections.reverse(list);
                userProgress.setVisibility(View.GONE);
                myAdapter.notifyDataSetChanged();
                if(list.size() == 0){
                    Toast.makeText(UserActivity.this, "No Deals Yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.user_find_deals){
            try {
                startActivity(new Intent(UserActivity.this, AdminActivity.class));
                return true;
            }catch(Exception e){
                Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(item.getItemId() == R.id.user_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(UserActivity.this, MainActivity.class));
                }
            });
        }
        else{
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(resortRef != null)
            resortRef.addValueEventListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(resortRef != null){
            resortRef.removeEventListener(listener);
        }
    }

}
