package com.example.gumptionlabs;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class NewCourseActivity extends AppCompatActivity {
    private EditText et_name;
    private EditText et_desc;
    private EditText et_amt;
    private NumberPicker nrPicker_vidCount;

    //private static final int PICK_IMAGE_REQUEST = 1;
    //private Button mButtonChooseImage;
    //private TextView mTextViewShowUploads;
   // private EditText mEditTextFileName;
   // private ImageView mImageView;
   // private ProgressBar mProgressBar;

    //private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        setTitle("Add Course Details");
        et_name=findViewById(R.id.new_course_name);
        et_desc=findViewById(R.id.new_course_description);
        et_amt=findViewById(R.id.new_course_amount);
        nrPicker_vidCount=findViewById(R.id.new_course_vidCount_nrPicker);
        //mButtonChooseImage = findViewById(R.id.button);
       // mEditTextFileName = findViewById(R.id.editText_video);
       // mImageView = findViewById(R.id.imageView);
       // mProgressBar = findViewById(R.id.progressBar);


        nrPicker_vidCount.setMinValue(1);
        nrPicker_vidCount.setMaxValue(50);

        /*mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_course_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.save_course:
                saveCourse();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }

    }*/

    private void saveCourse()
    {
        String name = et_name.getText().toString();
        String description = et_desc.getText().toString();
        int video_count = nrPicker_vidCount.getValue();
        String amt = et_amt.getText().toString();
        int amount = Integer.parseInt(amt);

        if(name.trim().isEmpty() || description.trim().isEmpty() || amt.trim().isEmpty())
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference courseRef = FirebaseFirestore.getInstance()
                .collection("videos");
        courseRef.add(new Course(name,description,video_count,amount));
        Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
