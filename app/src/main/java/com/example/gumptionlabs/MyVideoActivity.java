package com.example.gumptionlabs;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyVideoActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    private CollectionReference myvidRef;
    private MyVideoAdapter adapter;
    String docId,uid;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        setTitle("Videos");
        fab=findViewById(R.id.btn_add_video);
        fab.setVisibility(View.GONE);  //ignore warning, this works
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

        adapter.setOnClickListener(new MyVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                // MyCourse mycourse = documentSnapshot.toObject(MyCourse.class);
                String id = documentSnapshot.getId(); //document id
               //Intent i = new Intent(MyVideoActivity.this, PlayerActivity.class);
                Intent i = new Intent(MyVideoActivity.this, VideoPlayer.class); // my new intent looking hella neat
                String temp = documentSnapshot.getString("Video_URL");
                String toSend = temp.substring(18);
                //Toast.makeText(MyVideoActivity.this, "URL "+toSend, Toast.LENGTH_LONG).show();
                Log.d("TAG", " "+toSend);
                i.putExtra("myvideoURL",toSend);  //   New send with only the number part
                //i.putExtra("myvideoURL",documentSnapshot.getString("Video_URL"));  Older statement
                String path = documentSnapshot.getReference().getPath(); //path to doc
                i.putExtra("myvideoPath",path);
                i.putExtra("myvideoId",id);
                startActivity(i);
            }
        });
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
