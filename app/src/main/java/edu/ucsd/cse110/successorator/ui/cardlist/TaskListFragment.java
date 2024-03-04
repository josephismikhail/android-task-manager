package edu.ucsd.cse110.successorator.ui.cardlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
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

        Spinner dateSpinner = view.getRoot().findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        // Set the time to 2 AM
        // use local date time to get the current date and time, instead of calendar
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
//        Date updatedTime = calendar.getTime();

        // Format the date for display
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
//        String updatedTimeString = sdf.format(updatedTime);

        // set up the options of the dropdown menu
        List<String> options = new ArrayList<>();
        options.add("Today - " + sdf.format(calendar.getTime()));
        Calendar tmrCalendar = (Calendar) calendar.clone();
        tmrCalendar.add(Calendar.DAY_OF_YEAR, 1);
        options.add("Tomorrow - " + sdf.format(tmrCalendar.getTime()));
        options.add("Pending");
        options.add("Recurring");

        // set up the adapter for the dropdown menu
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);

//        // Update the text of the TextView with the formatted date
//        dateSpinner.setText(updatedTimeString);

        view.dateButton.setOnClickListener(v -> {
            // Increment the date by one day
            calendar.add(Calendar.DATE, 1);

            // Get the updated date and time
            options.set(0, "Today - " + sdf.format(calendar.getTime()));
            Calendar tmrCalendar2 = (Calendar) calendar.clone();
            tmrCalendar2.add(Calendar.DAY_OF_YEAR, 1);
            options.set(1, "Tomorrow - " + sdf.format(tmrCalendar2.getTime()));
//            String updatedTimeString2 = sdf.format(newTime);
            adapter.notifyDataSetChanged();
            dateSpinner.setSelection(0);

            // Update the text of the TextView with the formatted date
//            dateSpinner.setText(updatedTimeString2);
            activityModel.deleteCompletedTasks(true);
        });

        return view.getRoot();
    }
}
