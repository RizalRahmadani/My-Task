package rzl.learning.mytask.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rzl.learning.mytask.R;
import rzl.learning.mytask.data.Task;
import rzl.learning.mytask.databinding.ActivityEditTaskBinding;

public class EditTaskActivity extends AppCompatActivity {

    private ActivityEditTaskBinding binding;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String taskId;

    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtCalendar.setOnClickListener(view -> {
            new DatePickerDialog(EditTaskActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("tasks");

        taskId = getIntent().getStringExtra("taskId");
        String taskName = getIntent().getStringExtra("taskName");
        String deadline = getIntent().getStringExtra("deadline");
        String category = getIntent().getStringExtra("category");
        String description = getIntent().getStringExtra("description");

        binding.edtTaskName.setText(taskName);
        binding.edtCalendar.setText(deadline);
        binding.edtDescription.setText(description);

        int radioButtonId = -1;
        switch (category.toLowerCase()) {
            case "work":
                radioButtonId = R.id.radioButtonWork;
                break;
            case "education":
                radioButtonId = R.id.radioButtonEducation;
                break;
            case "life":
                radioButtonId = R.id.radioButtonLife;
                break;
        }
        if (radioButtonId != -1) {
            RadioButton radioButton = findViewById(radioButtonId);
            radioButton.setChecked(true);
        }

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void saveTask() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String userId = currentUser.getUid();



        String updatedTaskName = binding.edtTaskName.getText().toString().trim();
        String updatedDeadline = binding.edtCalendar.getText().toString().trim();
        String updatedDescription = binding.edtDescription.getText().toString().trim();

        int selectedCategoryId = binding.radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedCategoryId);
        String updatedCategory = selectedRadioButton.getText().toString().trim();

        Task updatedTask = new Task(taskId, updatedTaskName, updatedCategory, updatedDeadline, updatedDescription);

        databaseReference.child(userId).child(taskId).setValue(updatedTask)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "Data berhasil di ubah", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(),"Data gagal diubah, terjadi kesalahan", Toast.LENGTH_SHORT).show();
                });
    }

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    private void updateLabel() {
        String myFormat = "dd MMM, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.edtCalendar.setText(sdf.format(myCalendar.getTime()));
    }
}