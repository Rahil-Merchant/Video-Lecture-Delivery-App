package com.example.gumptionlabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyCoursesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    private CollectionReference mycourseRef;
    private MyCourseAdapter adapter;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        setTitle("My Courses");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            uid = user.getUid();
        }
        mycourseRef=db.collection("users").document(uid).collection("courses_purchased");
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = mycourseRef.orderBy("amount", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MyCourse> options= new FirestoreRecyclerOptions.Builder<MyCourse>()
                .setQuery(query,MyCourse.class)
                .build();

        adapter = new MyCourseAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.MyCourse_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new MyCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
               // MyCourse mycourse = documentSnapshot.toObject(MyCourse.class);
                String id = documentSnapshot.getId(); //document id
               // String path = documentSnapshot.getReference().getPath(); //path to doc
                Intent i = new Intent(MyCoursesActivity.this, MyVideoActivity.class);
                i.putExtra("mycourseId",id);
               // i.putExtra("mycourseName",documentSnapshot.getString("name"));
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
