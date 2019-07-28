package com.example.gumptionlabs;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
   // private int mMenuId;
    BottomNavigationView navView;

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
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_my_courses).setChecked(true);
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

           // mMenuId = item.getItemId();
            for (int i = 0; i < navView.getMenu().size(); i++) {
                MenuItem menuItem = navView.getMenu().getItem(i);
                boolean isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);
            }

            switch (item.getItemId()) {
                case R.id.navigation_store:{
                    startActivity(new Intent(MyCoursesActivity.this,StoreActivity.class));
                    return true;
                }
                case R.id.navigation_my_courses: {
                    return true;
                }
                case R.id.navigation_info: {
                    String url = "http://www.gumptionlabs.com";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                case R.id.navigation_settings: {
                    startActivity(new Intent(MyCoursesActivity.this,homeActivity.class));
                    return true;
                }
            }
            return false;
        }
    };
}
