package rzl.learning.mytask.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import rzl.learning.mytask.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButtonWork, radioButtonEducation, radioButtonLife;

    private EditText edtCalendar;
    private Calendar myCalendar = Calendar.getInstance();

    private ActivityAddBinding addBinding;

    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addBinding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(addBinding.getRoot());


        radioGroup = findViewById(R.id.radioGroup);
        radioButtonWork = findViewById(R.id.radioButtonWork);
        radioButtonEducation = findViewById(R.id.radioButtonEducation);
        radioButtonLife = findViewById(R.id.radioButtonLife);

        mDatabase = FirebaseDatabase.getInstance();

        edtCalendar = findViewById(R.id.edt_calendar);
        edtCalendar.setOnClickListener(view -> {
            new DatePickerDialog(AddActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        addBinding.btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

        addBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addTask() {
        String taskName = addBinding.edtTaskName.getText().toString();
        String deadline = addBinding.edtCalendar.getText().toString();
        String description = addBinding.edtDescription.getText().toString();
        String category = "";

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButtonWork) {
            category = "Work";
        } else if (selectedId == R.id.radioButtonEducation) {
            category = "Education";
        } else if (selectedId == R.id.radioButtonLife) {
            category = "Life";
        }

        if (taskName.isEmpty() || deadline.isEmpty() || description.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            reference = mDatabase.getReference("tasks").child(userId);

            String taskId = reference.push().getKey();
            Task task = new Task(taskId, taskName, category, deadline, description);

            reference.child(taskId).setValue(task).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Toast.makeText(AddActivity.this, "Tugas berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddActivity.this, "Tugas gagal ditambahkan", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

        edtCalendar.setText(sdf.format(myCalendar.getTime()));
    }


}