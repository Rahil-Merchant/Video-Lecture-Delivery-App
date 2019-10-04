package com.example.gumptionlabs;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

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
    private ImageView et_imgView;
    private Button et_textView;
    String docId;
    SimpleDateFormat s;
    Date Timestamp = new Date();
    String Length,Video_Name,Video_URL,Password;
    int seq_no, check_img_upload = 0;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private StorageReference mStorageRef;
    private StorageTask mUploadTask;


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
        et_imgView = findViewById(R.id.imageView);
        et_textView = findViewById(R.id.button);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        et_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

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

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(et_imgView);
            check_img_upload = 1;
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
                || docId.trim().isEmpty() || check_img_upload == 0)
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
        //video.put("Image", imageUri);

        //StorageReference strRef = FirebaseStorage.getInstance().getReference("Uploads");
        //strRef.getReference().putFile(imageUri);
        long ctr = System.currentTimeMillis();
        StorageReference fileReference = mStorageRef.child(ctr
                + "." + getFileExtension(imageUri));

        video.put("Image",ctr);

        mUploadTask = fileReference.putFile(imageUri);

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

    /*private void uploadFile() {
        if (imageUri != null)
        {
            mStorageRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return mStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        //Log.e(TAG, "then: " + downloadUri.toString());


                        ImageUpload upload = new ImageUpload(et_name.getText().toString().trim(),
                                downloadUri.toString());

                        mDatabaseRef.push().setValue(upload);
                    } else
                    {
                        Toast.makeText(NewVideoActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }*/
}