package edu.ucsd.cse110.successorator.ui.cardlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateTaskDialogFragment;

public class TaskListFragment extends Fragment {
    private FragmentTaskListBinding view;
    private MainViewModel activityModel;
    private TaskListAdapter adapter;

    public TaskListFragment() {

    }

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        this.adapter = new TaskListAdapter(requireContext(), List.of(), task -> {
            // Delegate the task completion logic to the ViewModel
            activityModel.completeTask(TaskEntity.fromTask(task));
        });

        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks));
            adapter.notifyDataSetChanged();
        });
    }

    /*
    Links:
    - https://stackoverflow.com/questions/36914596/java-8-localdatetime-today-at-specific-hour
    - https://stackoverflow.com/questions/23944370/how-to-get-milliseconds-from-localdatetime-in-java-8
    - https://stackoverflow.com/questions/28177370/how-to-format-localdate-to-string
    - https://stackoverflow.com/questions/29201103/how-to-compare-localdate-instances-java-8
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);
        view.taskList.setAdapter(adapter);
        view.taskList.setEmptyView(view.emptyText);

        view.plusButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });

        TextView dateTextView = view.getRoot().findViewById(R.id.date);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime cutoffTime = LocalDate.now().atTime(2,0,0);

        if (currentTime.isBefore(cutoffTime)) {
            cutoffTime = cutoffTime.minusDays(1);
        }
        else {
            cutoffTime = currentTime;
            activityModel.deleteCompletedTasksBefore(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        activityModel.setNewTime(cutoffTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MM/dd");
        dateTextView.setText(activityModel.getCurrentTime().format(formatter));

        // Forward date button
        view.dateButton.setOnClickListener(v -> {
            activityModel.setNewTime(activityModel.getCurrentTime().plusDays(1));
            dateTextView.setText(activityModel.getCurrentTime().format(formatter));
            activityModel.deleteCompletedTasks(true);
            activityModel.recurTask(activityModel.getCurrentTime());
        });


        return view.getRoot();
    }
}
