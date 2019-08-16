package com.example.gumptionlabs;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewPurchasedActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String uid;
    private CollectionReference courseRef;
    private MyCourseAdapter adapter;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        setTitle("View Purchase History");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                uid =null;
            } else {
                uid = extras.getString("userId");
            }
        } else {
            uid = (String) savedInstanceState.getSerializable("userId");
        }

        courseRef=db.collection("users").document(uid).collection("courses_purchased");
        navView = findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        Query query = courseRef.orderBy("amount", Query.Direction.ASCENDING);

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
                //String id = documentSnapshot.getId(); //document id
                // String path = documentSnapshot.getReference().getPath(); //path to doc
                //Intent i = new Intent(MyCoursesActivity.this, MyVideoActivity.class);
                //i.putExtra("mycourseId",id);
                // i.putExtra("mycourseName",documentSnapshot.getString("name"));
                //startActivity(i);
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
