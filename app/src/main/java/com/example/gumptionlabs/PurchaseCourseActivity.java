package com.example.gumptionlabs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PurchaseCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String name, amt, vidCount, desc, docId, payment_mode, uid, email, purchase_timestamp;
    String[] payment_modes = { "Credit Card", "Debit Card", "Netbanking", "PayTM", "Other"};
    TextView nameTv, amtTv, vidCountTv, descTv;
    SimpleDateFormat s;
    int amount = 0;
    FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_course);

            setTitle("Purchase Course");
            if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                amt = null;
                name=null;
                vidCount=null;
                desc=null;
                docId = null;
            } else {
                amt = extras.getString("courseAmount");
                name = extras.getString("courseName");
                vidCount = extras.getString("courseVidCount");
                desc=extras.getString("courseDesc");
                docId=extras.getString("courseId");
            }
        } else {
            amt = (String) savedInstanceState.getSerializable("courseAmount");
            name = (String) savedInstanceState.getSerializable("courseName");
            vidCount = (String) savedInstanceState.getSerializable("courseVidCount");
            desc = (String) savedInstanceState.getSerializable("courseDesc");
            docId = (String) savedInstanceState.getSerializable("courseId");

        }
            amount=Integer.parseInt(amt);
            nameTv =findViewById(R.id.purchase_name);
            nameTv.setText(name);
            amtTv=findViewById(R.id.purchase_amt);
            amtTv.setText(amt);
            vidCountTv=findViewById(R.id.purchase_vidCount);
            vidCountTv.setText(vidCount);
            descTv=findViewById(R.id.purchase_desc);
            descTv.setText(desc);
            Spinner spinMode = findViewById(R.id.purchase_spinner);
            spinMode.setOnItemSelectedListener(this);
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,payment_modes);  // built in resource
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  //built in resource
            spinMode.setAdapter(aa);

            user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                email = user.getEmail();
                uid = user.getUid();
            }

            s= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            findViewById(R.id.purchase_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    purchase_timestamp= s.format(new Date());
                    //setting isPaid flag
                    Map<String, Object> data = new HashMap<>();
                    data.put("isPaid", true);
                    db.collection("users").document(uid)
                            .set(data, SetOptions.merge());

                    writeCourseDoc();
                }
            });

        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        payment_mode = payment_modes[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Please select a payment mode", Toast.LENGTH_SHORT).show();
    }

    private void writeCourseDoc()
    {
        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        DocumentReference uidRef = ref.collection("videos").document(docId).collection("purchased_by").document(uid);
        purchaseDatabaseWrite pDb = new purchaseDatabaseWrite(amount, email, payment_mode, purchase_timestamp);
        uidRef.set(pDb).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                writeUserDoc();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PurchaseCourseActivity.this, "Unexpected Error, Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void writeUserDoc()
    {
        FirebaseFirestore ref = FirebaseFirestore.getInstance();
        DocumentReference uidRef = ref.collection("users").document(uid).collection("courses_purchased").document(docId);
        purchaseUserDatabaseWrite pDb = new purchaseUserDatabaseWrite(name, amount, payment_mode, purchase_timestamp);
        uidRef.set(pDb).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PurchaseCourseActivity.this, "Data Successfully Added", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(PurchaseCourseActivity.this, homeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PurchaseCourseActivity.this, "Unexpected Error, Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
