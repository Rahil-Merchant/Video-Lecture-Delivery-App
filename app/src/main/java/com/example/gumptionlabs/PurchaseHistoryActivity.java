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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    private CollectionReference mycourseRef;
    private PurchaseHistoryAdapter adapter;
    String uid;
    // private int mMenuId;
    BottomNavigationView navView;
    GoogleSignInClient mGoogleAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        setTitle("Purchase History");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleAuth = GoogleSignIn.getClient(this,gso);
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

        adapter = new PurchaseHistoryAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.MyCourse_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /*adapter.setOnClickListener(new MyCourseAdapter.OnItemClickListener() {
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
        });*/
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
                    startActivity(new Intent(getApplicationContext(),StoreActivity.class));
                    return true;
                }
                case R.id.navigation_my_courses: {
                    startActivity(new Intent(getApplicationContext(),MyCoursesActivity.class));
                    return true;
                }
                case R.id.navigation_purchase_history: {
                    return true;
                }
                case R.id.navigation_free_courses: {
                    startActivity(new Intent(getApplicationContext(),FreeCoursesActivity.class));
                    return true;
                }

                case R.id.navigation_logout: {
                    FirebaseAuth.getInstance().signOut();
                    mGoogleAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    return true;
                }
            }
            return false;
        }
    };
}
