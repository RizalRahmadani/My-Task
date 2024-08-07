package rzl.learning.mytask.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable; // Tambahkan import untuk @Nullable
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import rzl.learning.mytask.adapter.TaskAdapter;
import rzl.learning.mytask.data.Task;
import rzl.learning.mytask.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String name = "";

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Masih dalam pengembangan", Toast.LENGTH_SHORT).show();
            }
        });

        showName();
        setupRecyclerView(view);
        loadTask();
    }

    private void showName() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        name = dataSnapshot.child("name").getValue(String.class);
                        if (name != null && !name.isEmpty()) {
                            name = name.toLowerCase();
                            name = name.substring(0, 1).toUpperCase() + name.substring(1);
                        }
                        binding.tvHello.setText("Hello,\n" + name);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void setupRecyclerView(View view) {
        binding.rvList.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(getContext(),taskList);
        binding.rvList.setAdapter(taskAdapter);
    }

    private void loadTask() {
        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        reference = FirebaseDatabase.getInstance().getReference("tasks").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(Task.class);
                    taskList.add(task);
                }
                if (taskList.isEmpty()){
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.imgEmpty.setVisibility(View.VISIBLE);
                }else{
                    binding.tvEmpty.setVisibility(View.GONE);
                    binding.imgEmpty.setVisibility(View.GONE);
                }

                taskAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
