package com.example.gumptionlabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AddVideoActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    private CollectionReference myvidRef;
    private AddVideoAdapter adapter;
    String docId,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        setTitle("Edit Videos");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                docId =null;
            } else {
                docId = extras.getString("mycourseId");
            }
        } else {
            docId = (String) savedInstanceState.getSerializable("mycourseId");
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
        }

        myvidRef=db.collection("videos").document(docId).collection("videos");
        setUpRecyclerView();

        findViewById(R.id.btn_add_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AddVideoActivity.this, NewVideoActivity.class);
                i.putExtra("docId",docId);
                startActivity(i);
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = myvidRef.orderBy("seq_no", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Video> options= new FirestoreRecyclerOptions.Builder<Video>()
                .setQuery(query,Video.class)
                .build();

        adapter = new AddVideoAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.MyVideo_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                //drag drop
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                Toast.makeText(AddVideoActivity.this, "Video Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}