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
    private MyVideoAdapter adapter;
    String docId,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        setTitle("Edit Videos");

/*      findViewById(R.id.myVideo_url).setVisibility(View.VISIBLE);
        findViewById(R.id.myVideo_urlTv).setVisibility(View.VISIBLE);
        findViewById(R.id.myVideo_timestamp).setVisibility(View.VISIBLE);
        findViewById(R.id.myVideo_timestampTv).setVisibility(View.VISIBLE);
        findViewById(R.id.myVideo_pwd).setVisibility(View.VISIBLE);
        findViewById(R.id.myVideo_pwdTv).setVisibility(View.VISIBLE);*/

        findViewById(R.id.btn_add_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddVideoActivity.this, "Adding videos functionality yet to be implemented", Toast.LENGTH_SHORT).show();
            }
        });

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
    }

    private void setUpRecyclerView() {
        Query query = myvidRef.orderBy("seq_no", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MyVideo> options= new FirestoreRecyclerOptions.Builder<MyVideo>()
                .setQuery(query,MyVideo.class)
                .build();

        adapter = new MyVideoAdapter(options);
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
                Toast.makeText(AddVideoActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
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