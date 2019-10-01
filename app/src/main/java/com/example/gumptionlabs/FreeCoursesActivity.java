package com.example.gumptionlabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FreeCoursesActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference courseRef = db.collection("videos");
    private CourseAdapter adapter;
    BottomNavigationView navView;
    GoogleSignInClient mGoogleAuth;

    String id;
    SimpleDateFormat s;
    int amount = 0;
    FirebaseUser user;
    String name,uid, email, purchase_timestamp;
    String payment_mode = "Free Course";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setTitle("Free Courses");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleAuth = GoogleSignIn.getClient(this,gso);
        fab=findViewById(R.id.btn_add_course);
        fab.setVisibility(View.GONE);  //ignore warning, this works
        setUpRecylerView();
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_store).setChecked(true);

        s= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            email = user.getEmail();
            uid = user.getUid();
        }
    }

    private void setUpRecylerView() {
        Query query = courseRef.whereEqualTo("amount",0).orderBy("amount", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Course> options= new FirestoreRecyclerOptions.Builder<Course>()
                .setQuery(query,Course.class)
                .build();

        adapter = new CourseAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Course course = documentSnapshot.toObject(Course.class);
                id = documentSnapshot.getId(); //document id
                name=documentSnapshot.getString("name");
                purchase_timestamp= s.format(new Date());

                //write course doc
                FirebaseFirestore ref = FirebaseFirestore.getInstance();
                DocumentReference uidRef = ref.collection("videos").document(id).collection("purchased_by").document(uid);
                purchaseDatabaseWrite pDb = new purchaseDatabaseWrite(amount, email, payment_mode, purchase_timestamp);
                uidRef.set(pDb).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //write user doc
                        FirebaseFirestore ref = FirebaseFirestore.getInstance();
                        DocumentReference uidRef = ref.collection("users").document(uid).collection("courses_purchased").document(id);
                        purchaseUserDatabaseWrite pDb = new purchaseUserDatabaseWrite(name, amount, payment_mode, purchase_timestamp);
                        uidRef.set(pDb).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FreeCoursesActivity.this, "Course successfully added to your courses", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(FreeCoursesActivity.this, homeActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FreeCoursesActivity.this, "Unexpected Error, Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FreeCoursesActivity.this, "Unexpected Error, Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });




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
            for (int i = 0; i < navView.getMenu().size(); i++) {
                MenuItem menuItem = navView.getMenu().getItem(i);
                boolean isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);
            }
            switch (item.getItemId()) {
                case R.id.navigation_my_courses:{
                    startActivity(new Intent(FreeCoursesActivity.this,MyCoursesActivity.class));
                    return true;
                }
                case R.id.navigation_store: {
                    startActivity(new Intent(FreeCoursesActivity.this,StoreActivity.class));
                    return true;
                }
                case R.id.navigation_purchase_history: {
                    startActivity(new Intent(FreeCoursesActivity.this,PurchaseHistoryActivity.class));
                    return true;
                }
                case R.id.navigation_free_courses: {
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
