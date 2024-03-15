package edu.ucsd.cse110.successorator.ui.cardlist;

import android.annotation.SuppressLint;
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
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreatePendingMenuFragment;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreatePendingTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateRecurringMenuFragment;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateRecurringTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateTomorrowTaskDialogFragment;

public class TaskListFragment extends Fragment {
    private FragmentTaskListBinding mainView;
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

        this.adapter = new TaskListAdapter(
                requireContext(),
                List.of(),
                task -> {
                    if (activityModel.getCurrTaskView() == TaskViews.TODAY_VIEW ||
                        activityModel.getCurrTaskView() == TaskViews.TOMORROW_VIEW) {
                        activityModel.completeTask(TaskEntity.fromTask(task));
                    }
                },
                task -> {
                    if (activityModel.getCurrTaskView() == TaskViews.PENDING_VIEW) {
                        var dialogFragment = CreatePendingMenuFragment.newInstance(task);
                        dialogFragment.show(getParentFragmentManager(), "CreatePendingMenuFragment");
                    } else if (activityModel.getCurrTaskView() == TaskViews.RECURRING_VIEW) {
                        var dialogFragment = CreateRecurringMenuFragment.newInstance(task);
                        dialogFragment.show(getParentFragmentManager(), "CreateRecurringMenuFragment");
                    }
                }
        );

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
        this.mainView = FragmentTaskListBinding.inflate(inflater, container, false);
        mainView.taskList.setAdapter(adapter);
//        mainView.taskList.setEmptyView(mainView.emptyText);

        Spinner dateSpinner = mainView.getRoot().findViewById(R.id.date);

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

                if (selectedItem.split("-")[0].equals("Pending")) {
                    mainView.plusButton.setOnClickListener(v -> {
                        var dialogFragment = CreatePendingTaskDialogFragment.newInstance();
                        dialogFragment.show(getParentFragmentManager(), "CreatePendingTaskDialogFragment");
                    });
                }
                if (selectedItem.split("-")[0].equals("Tomorrow ")) {
                    mainView.plusButton.setOnClickListener(v -> {
                        var dialogFragment = CreateTomorrowTaskDialogFragment.newInstance();
                        dialogFragment.show(getParentFragmentManager(), "CreateTomorrowTaskDialogFragment");
                    });
                }
                if (selectedItem.split("-")[0].equals("Recurring")){
                    mainView.plusButton.setOnClickListener(v -> {
                        var dialogFragment = CreateRecurringTaskDialogFragment.newInstance();
                        dialogFragment.show(getParentFragmentManager(), "CreateRecurringTaskDialogFragment");
                    });
                }
                if (selectedItem.split("-")[0].equals("Today ")){
                    mainView.plusButton.setOnClickListener(v -> {
                        var dialogFragment = CreateTaskDialogFragment.newInstance();
                        dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
                    });
                }

                // Perform actions based on the selected item
                switch (selectedItem.split(" - ")[0]) { // Using split to get the first part ("Today", "Tomorrow", "Pending", "Recurring")
                    case "Today":
                        // Perform action for Today
                        activityModel.switchView(TaskViews.TODAY_VIEW);
                        mainView.taskList.setEmptyView(mainView.emptyText);
                        break;
                    case "Tomorrow":
                        // Perform action for Tomorrow
                        activityModel.switchView(TaskViews.TOMORROW_VIEW);
                        mainView.emptyText.setVisibility(View.INVISIBLE);
                        break;
                    case "Pending":
                        // Perform action for Pending
                        activityModel.switchView(TaskViews.PENDING_VIEW);
                        mainView.emptyText.setVisibility(View.INVISIBLE);
                        break;
                    case "Recurring":
                        // Perform action for Recurring
                        activityModel.switchView(TaskViews.RECURRING_VIEW);
                        mainView.emptyText.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Do something when nothing is selected
            }
        });

        Spinner focusModeSpinner = mainView.getRoot().findViewById(R.id.mode);

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
                        activityModel.switchContextView(ContextViews.ALL);
                        break;
                    case "Home":
                        activityModel.switchContextView(ContextViews.HOME);
                        break;
                    case "Work":
                        activityModel.switchContextView(ContextViews.WORK);
                        break;
                    case "School":
                        activityModel.switchContextView(ContextViews.SCHOOL);
                        break;
                    case "Errand":
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

        mainView.dateButton.setOnClickListener(v -> {
            int currentPosition = dateSpinner.getSelectedItemPosition();
            activityModel.setNewTime(activityModel.getCurrentTime().plusDays(1));
            activityModel.deleteCompletedTasks(true);
            activityModel.updateDisplayTask(activityModel.getCurrentTime());

            options.set(0, "Today - " + activityModel.getCurrentTime().format(formatter));
            options.set(1, "Tomorrow - " + activityModel.getCurrentTime().plusDays(1).format(formatter));
            adapter.notifyDataSetChanged();
            dateSpinnerAdapter.notifyDataSetChanged();
            dateSpinner.setSelection(currentPosition);
        });

        return mainView.getRoot();
    }
}
