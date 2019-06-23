package com.example.gumptionlabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewCourseActivity extends AppCompatActivity {
    private EditText et_name;
    private EditText et_desc;
    private EditText et_amt;
    private NumberPicker nrPicker_vidCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        setTitle("Add Course Details");
        et_name=findViewById(R.id.new_course_name);
        et_desc=findViewById(R.id.new_course_description);
        et_amt=findViewById(R.id.new_course_amount);
        nrPicker_vidCount=findViewById(R.id.new_course_vidCount_nrPicker);

        nrPicker_vidCount.setMinValue(1);
        nrPicker_vidCount.setMaxValue(50);
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
