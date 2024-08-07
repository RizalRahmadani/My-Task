package rzl.learning.mytask.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rzl.learning.mytask.R;
import rzl.learning.mytask.data.User;
import rzl.learning.mytask.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

   private ActivityRegisterBinding binding;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        setupPasswordVisibilityToggle();
        setupConfirmPasswordVisibilityToggle();


        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = mDatabase.getReference("users");

                String name = binding.edtName.getText().toString();
                String email = binding.edtEmail.getText().toString();
                String password = binding.edtPassword.getText().toString();
                String confirmPassword = binding.edtConfirmPassword.getText().toString();

                if (name.isEmpty()||email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()){

                    if (name.isEmpty()){
                        binding.edtName.setError("Masukkan Nama Anda");
                    }
                    if (email.isEmpty()){
                        binding.edtEmail.setError("Masukkan Email Anda");
                    }
                    if (password.isEmpty()){
                        binding.edtName.setError("Masukkan Password Anda");
                    }
                    if (confirmPassword.isEmpty()){
                        binding.edtName.setError("Masukkan Ulang Password Anda");
                    }
                    Toast.makeText(getApplicationContext(),"Lengkapi data anda", Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern)){
                    binding.edtEmail.setError("Email anda tidak valid");
                    Toast.makeText(RegisterActivity.this, "Masukkan Email Anda", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 8) {
                    binding.edtPassword.setError("Masukkan password minimal 8 karakter");
                    Toast.makeText(RegisterActivity.this, "Masukkan password minimal 8 karakter", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    binding.edtConfirmPassword.setError("Password Tidak Sama, Mohon Ulangi");
                    Toast.makeText(RegisterActivity.this, "Password Tidak Sama, Mohon Ulangi", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DatabaseReference mDatabaseRef = mDatabase.getReference().child("users").child(auth.getCurrentUser().getUid());
                            User users = new User(auth.getCurrentUser().getUid(),name, email,"");
                            mDatabaseRef.setValue(users).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Terjadi kesalahan, coba ulangi!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Terjadi kesalahan, coba ulangi!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void setupPasswordVisibilityToggle() {
        binding.edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtPassword.getRight() - binding.edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void togglePasswordVisibility()  {
        if (isPasswordVisible){
            binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_hide_password,0);
        }else{
            binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.edtPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_show_password,0);
        }

        binding.edtPassword.setSelection(binding.edtPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void setupConfirmPasswordVisibilityToggle() {
        binding.edtConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.edtConfirmPassword.getRight() - binding.edtConfirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        toggleConfirmPasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });
    }



    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            binding.edtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.edtConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_hide_password, 0);
        } else {
            binding.edtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.edtConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_show_password, 0);
        }
        binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.getText().length());
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
    }
}