package edu.ucsd.cse110.successorator.ui.cardlist;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.lib.domain.ContextViews;
import edu.ucsd.cse110.successorator.lib.domain.TaskViews;
import edu.ucsd.cse110.successorator.databinding.FocusModeDialogBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateTaskDialogFragment;

public class TaskListFragment extends Fragment{
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

        LocalDateTime currentLocalTime = LocalDateTime.now();
        LocalDateTime cutoffTime = currentLocalTime.toLocalDate().atTime(2,0,0);


        if (currentLocalTime.isBefore(cutoffTime)) {
            cutoffTime = cutoffTime.minusDays(1);
        }
        else {
            cutoffTime = currentLocalTime;
            activityModel.deleteCompletedTasks(true);
            activityModel.updateDisplayTask(LocalDateTime.now());
        }

        activityModel.setNewTime(cutoffTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MM/dd");

        Spinner dateSpinner = view.getRoot().findViewById(R.id.date);

        List<String> options = new ArrayList<>();
        options.add("Today - " + activityModel.getCurrentTime().format(formatter));
        options.add("Tomorrow - " + activityModel.getCurrentTime().plusDays(1).format(formatter));
        options.add("Pending");
        options.add("Recurring");

        // set up the adapter for the dropdown menu
        ArrayAdapter<String> dateSpinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, options);
        dateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateSpinnerAdapter);

        // switch to different views
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Perform actions based on the selected item
                switch (selectedItem.split(" - ")[0]) { // Using split to get the first part ("Today", "Tomorrow", "Pending", "Recurring")
                    case "Today":
                        // Perform action for Today
                        activityModel.switchView(TaskViews.TODAY_VIEW);
                        break;
                    case "Tomorrow":
                        // Perform action for Tomorrow
                        activityModel.switchView(TaskViews.TOMORROW_VIEW);
                        break;
                    case "Pending":
                        // Perform action for Pending
                        activityModel.switchView(TaskViews.PENDING_VIEW);
                        break;
                    case "Recurring":
                        // Perform action for Recurring
                        activityModel.switchView(TaskViews.RECURRING_VIEW);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Do something when nothing is selected
            }
        });

        Spinner focusModeSpinner = view.getRoot().findViewById(R.id.mode);

        List<String> focusModeOptions = new ArrayList<>();
        focusModeOptions.add("Cancel");
        focusModeOptions.add("Home");
        focusModeOptions.add("Work");
        focusModeOptions.add("School");
        focusModeOptions.add("Errand");

        ArrayAdapter<String> focusModeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, focusModeOptions);
        focusModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        focusModeSpinner.setAdapter(focusModeAdapter);

        // switch to different context views
        focusModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Hide spinner text
                ((TextView)view).setText(null);

                // Get the selected item
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Perform actions based on the selected item
                switch (selectedItem) {
                    case "Cancel":
                        focusModeSpinner.setBackgroundTintList(null);
                        activityModel.switchContextView(ContextViews.ALL);
                        break;
                    case "Home":
                        focusModeSpinner.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(244, 238, 18)));
                        activityModel.switchContextView(ContextViews.HOME);
                        break;
                    case "Work":
                        focusModeSpinner.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(188, 215, 237)));
                        activityModel.switchContextView(ContextViews.WORK);
                        break;
                    case "School":
                        focusModeSpinner.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(243, 213, 237)));
                        activityModel.switchContextView(ContextViews.SCHOOL);
                        break;
                    case "Errand":
                        focusModeSpinner.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(213, 251, 175)));
                        activityModel.switchContextView(ContextViews.ERRAND);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Do something when nothing is selected
            }
        });

        activityModel.setNewTime(cutoffTime);

        view.dateButton.setOnClickListener(v -> {
            activityModel.setNewTime(activityModel.getCurrentTime().plusDays(1));
            activityModel.deleteCompletedTasks(true);
            activityModel.updateDisplayTask(activityModel.getCurrentTime());

            options.set(0, "Today - " + activityModel.getCurrentTime().format(formatter));
            options.set(1, "Tomorrow - " + activityModel.getCurrentTime().plusDays(1).format(formatter));
            adapter.notifyDataSetChanged();
        });

        return view.getRoot();
    }
}
