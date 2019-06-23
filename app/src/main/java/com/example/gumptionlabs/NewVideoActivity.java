package com.example.gumptionlabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewVideoActivity extends AppCompatActivity {
    private EditText et_name;
    private EditText et_length;
    private EditText et_url;
    private EditText et_pwd;
    private NumberPicker nrPicker_seqNo;
    String docId;
    SimpleDateFormat s;
    Date Timestamp = new Date();
    String Length,Video_Name,Video_URL,Password;
    int seq_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_video);
        setTitle("Add Video Details");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                docId =null;
            } else {
                docId = extras.getString("docId");
            }
        } else {
            docId = (String) savedInstanceState.getSerializable("docId");
        }
        et_name=findViewById(R.id.new_video_name);
        et_length=findViewById(R.id.new_video_length);
        et_url=findViewById(R.id.new_video_url);
        et_pwd=findViewById(R.id.new_video_password);
        nrPicker_seqNo=findViewById(R.id.new_video_seqNo_nrPicker);
        nrPicker_seqNo.setMinValue(1);
        nrPicker_seqNo.setMaxValue(50);
        s= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_video_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.save_video:
                saveVideo();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveVideo()
    {
        Video_Name = et_name.getText().toString();
        Length = et_length.getText().toString();
        Password = et_pwd.getText().toString();
        Video_URL= et_url.getText().toString();
        seq_no = nrPicker_seqNo.getValue();
        String ts = s.format(new Date());
        try {
            Timestamp = s.parse(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(Video_Name.trim().isEmpty() || Length.trim().isEmpty()
                || Password.trim().isEmpty() || Video_URL.trim().isEmpty()
                || docId.trim().isEmpty())
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }



        Map<String, Object> video = new HashMap<>();
        video.put("Length",Length);
        video.put("Password", Password);
        video.put("Timestamp", Timestamp);
        video.put("Video_Name", Video_Name);
        video.put("Video_URL", Video_URL);
        video.put("seq_no", seq_no);

        CollectionReference vidRef = FirebaseFirestore.getInstance()
                .collection("videos")
                .document(docId)
                .collection("videos");

        vidRef.add(video).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NewVideoActivity.this, "Video Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewVideoActivity.this, "Unexpected Error Occurred", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        /* DocumentReference vidCountRef = FirebaseFirestore.getInstance()
                .collection("videos")
                .document("docId");
        vidCountRef.update("video_count", FieldValue.increment(1));
        Toast.makeText(NewVideoActivity.this, "Course Video Count Updated", Toast.LENGTH_SHORT).show();

        CollectionReference vidRef = FirebaseFirestore.getInstance()
                .collection("videos")
                .document(docId)
                .collection("videos");
        vidRef.add(new Video(Length,Password,Timestamp,Video_Name,Video_URL,seq_no));
        Toast.makeText(NewVideoActivity.this, "Video Added", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(NewVideoActivity.this,MasterActivity.class));*/
    }
}