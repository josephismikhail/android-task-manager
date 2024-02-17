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

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateTaskDialogFragment;

// library for showing date
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
            task.changeStatus();

            var minSortOrder = activityModel.getMinSortOrder();
            var maxSortOrder = activityModel.getMaxSortOrder();
            var maxIncompleteSortOrder = activityModel.getIncompleteMaxSortOrder();

            if (task.isCompleted()) { // move task to bottom of incomplete
                if (maxSortOrder == maxIncompleteSortOrder) {
                    task = task.withSortOrder(maxSortOrder + 1);
                } else {
                    activityModel.shiftSortOrder(maxIncompleteSortOrder + 1, maxSortOrder, 1);
                    task = task.withSortOrder(maxIncompleteSortOrder + 1);
                }
            } else { // moves task to top
                activityModel.shiftSortOrder(minSortOrder, maxSortOrder, 1);
                task = task.withSortOrder(minSortOrder);
            }

            activityModel.save(task);
        });

        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks));
            adapter.notifyDataSetChanged();
        });
    }

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
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        // Set the time to 2 AM
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Check if the current time is before 2 AM, then add one day
        if (currentTime.before(calendar.getTime())) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);

        }
        else {
            var modelOwner = requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            this.activityModel = modelProvider.get(MainViewModel.class);

            activityModel.deleteCompletedTasksBefore(calendar.getTimeInMillis());
        }

        // Get the updated date and time
        Date updatedTime = calendar.getTime();

        // Format the date for display
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        String updatedTimeString = sdf.format(updatedTime);

        // Update the text of the TextView with the formatted date
        dateTextView.setText(updatedTimeString);

        view.dateButton.setOnClickListener(v -> {
            // Increment the date by one day
            calendar.add(Calendar.DATE, 1);

            // Get the updated date and time
            Date newTime = calendar.getTime();
            String updatedTimeString2 = sdf.format(newTime);

            // Update the text of the TextView with the formatted date
            dateTextView.setText(updatedTimeString2);
            activityModel.deleteCompletedTasks(true);
        });

        return view.getRoot();
    }
}