package com.example.gumptionlabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.model.value.FieldValueOptions;

import java.util.HashMap;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    private CollectionReference userRef;
    private UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle("User List");
        userRef=db.collection("users");
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        Query query = userRef.orderBy("fname", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<UserList> options= new FirestoreRecyclerOptions.Builder<UserList>()
                .setQuery(query,UserList.class)
                .build();

        adapter = new UserAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.UserList_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Toast.makeText(UserListActivity.this, "Click on either of the below two buttons", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAssignClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                String email = documentSnapshot.getString("email");
                Intent i = new Intent(UserListActivity.this, AssignCourseActivity.class);
                i.putExtra("userId",id);
                i.putExtra("email",email);
                startActivity(i);
            }

            @Override
            public void onViewPurchasedClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Intent i = new Intent(UserListActivity.this, ViewPurchasedActivity.class);
                i.putExtra("userId",id);
                startActivity(i);
            }

           /* @Override
            public void onDisableClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Map<String, Object> data = new HashMap<>();
                data.put("isDisabled", true);
                db.collection("users").document(id)
                        .set(data, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserListActivity.this, "Account Locked", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
*/
           /* @Override
            public void onDeleteClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Map<String, Object> data = new HashMap<>();
                data.put("isDeleted", true);
                db.collection("users").document(id)
                        .set(data, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserListActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
            }*/

           /* @Override
            public void onEnableClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Map<String, Object> data = new HashMap<>();
                data.put("isDisabled", false);
                db.collection("users").document(id)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserListActivity.this, "Account Unlocked", Toast.LENGTH_SHORT).show();
                            }
                        });
            }*/
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
