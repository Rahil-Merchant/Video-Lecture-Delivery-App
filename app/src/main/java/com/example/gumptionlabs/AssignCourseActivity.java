package com.example.gumptionlabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AssignCourseActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference courseRef = db.collection("videos");
    private CourseAdapter adapter;
    BottomNavigationView navView;
    SimpleDateFormat s;
    String name, courseId, uid, email, purchase_timestamp;
    String payment_mode = "Assigned by Admin";
    int amount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setTitle("Assign Courses");
        fab = findViewById(R.id.btn_add_course);
        fab.setVisibility(View.GONE);  //ignore warning, this works
        setUpRecyclerView();
        navView = findViewById(R.id.nav_view);
        navView.setVisibility(View.GONE);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                uid =null;
                email=null;
            } else {
                uid = extras.getString("userId");
                email = extras.getString("email");
            }
        } else {
            uid = (String) savedInstanceState.getSerializable("userId");
            email = (String) savedInstanceState.getSerializable("email");
        }
        s= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    }

    private void setUpRecyclerView() {
        Query query = courseRef.orderBy("amount", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Course> options = new FirestoreRecyclerOptions.Builder<Course>()
                .setQuery(query, Course.class)
                .build();

        adapter = new CourseAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                courseId = documentSnapshot.getId(); //document id
                name = documentSnapshot.getString("name");
                amount = documentSnapshot.getLong("amount").intValue();
                purchase_timestamp= s.format(new Date());
                Map<String, Object> data = new HashMap<>();
                data.put("isPaid", true);
                db.collection("users").document(uid)
                        .set(data, SetOptions.merge());

                writeCourseDoc();
            }
        });
    }

    private void writeCourseDoc()
    {
        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        DocumentReference uidRef = ref.collection("videos").document(courseId).collection("purchased_by").document(uid);
        purchaseDatabaseWrite pDb = new purchaseDatabaseWrite(amount, email, payment_mode, purchase_timestamp);
        uidRef.set(pDb).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                writeUserDoc();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AssignCourseActivity.this, "Unexpected Error, Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void writeUserDoc()
    {
        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        DocumentReference uidRef = ref.collection("users").document(uid).collection("courses_purchased").document(courseId);
        purchaseUserDatabaseWrite pDb = new purchaseUserDatabaseWrite(name, amount, payment_mode, purchase_timestamp);
        uidRef.set(pDb).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AssignCourseActivity.this, "Course Successfully Assigned", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AssignCourseActivity.this, "Unexpected Error, Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
