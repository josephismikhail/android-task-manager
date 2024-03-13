package edu.ucsd.cse110.successorator.ui.cardlist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Objects;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.TagType;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class CreateTaskDialogFragment extends DialogFragment {
    private FragmentDialogCreateTaskBinding view;
    private MainViewModel activityModel;


    CreateTaskDialogFragment() {}

    public static CreateTaskDialogFragment newInstance() {
        var fragment = new CreateTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateTaskBinding.inflate(getLayoutInflater());
        // Click save button to save
        FragmentDialogCreateTaskBinding binding = this.view;
        ImageButton saveButton = binding.saveButton;
        // Set a click listener for the Save ImageButton
        saveButton.setOnClickListener(this::onSaveButtonClick);
        return new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please provide the new task text.")
                .setView(view.getRoot())
                .create();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

    }

    private void onSaveButtonClick(View v) {
        var taskText = view.taskEditText.getText().toString();
        var task = new Task(null, taskText, false, -1,
                null, RecurType.ONCE,
                activityModel.getCurrentTime().atZone(ZoneId.systemDefault()).toEpochSecond(), TagType.HOME,true);
        if (taskText.trim().isEmpty()) {
            // do nothing
        } else if (view.onceButton.isChecked()){
            if(view.homeButton.isChecked()) {
                activityModel.newTask(task);
            }else if (view.workButton.isChecked()) {
                activityModel.newTask(task.withTagType(TagType.WORK));
            }else if (view.schoolButton.isChecked()) {
                activityModel.newTask(task.withTagType(TagType.SCHOOL));
            }else if (view.errandsButton.isChecked()) {
                activityModel.newTask(task.withTagType(TagType.ERRAND));
            }
            activityModel.newTask(task.withTagType(TagType.PENDING));
        } else if (view.dailyButton.isChecked()) {
            if(view.homeButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.DAILY));
            }else if (view.workButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.DAILY).withTagType(TagType.WORK));
            }else if (view.schoolButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.DAILY).withTagType(TagType.SCHOOL));
            }else if (view.errandsButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.DAILY).withTagType(TagType.ERRAND));
            }
            activityModel.newTask(task.withRecurType(RecurType.DAILY).withTagType(TagType.PENDING));
        } else if (view.weeklyButton.isChecked()) {
            if(view.homeButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.WEEKLY));
            }else if (view.workButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.WEEKLY).withTagType(TagType.WORK));
            }else if (view.schoolButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.WEEKLY).withTagType(TagType.SCHOOL));
            }else if (view.errandsButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.WEEKLY).withTagType(TagType.ERRAND));
            }
            activityModel.newTask(task.withRecurType(RecurType.WEEKLY).withTagType(TagType.PENDING));
        } else if (view.monthlyButton.isChecked()) {
            if(view.homeButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.MONTHLY));
            }else if (view.workButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.MONTHLY).withTagType(TagType.WORK));
            }else if (view.schoolButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.MONTHLY).withTagType(TagType.SCHOOL));
            }else if (view.errandsButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.MONTHLY).withTagType(TagType.ERRAND));
            }
            activityModel.newTask(task.withRecurType(RecurType.MONTHLY).withTagType(TagType.PENDING));
        } else if (view.yearlyButton.isChecked()) {
            if(view.homeButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.YEARLY));
            }else if (view.workButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.YEARLY).withTagType(TagType.WORK));
            }else if (view.schoolButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.YEARLY).withTagType(TagType.SCHOOL));
            }else if (view.errandsButton.isChecked()) {
                activityModel.newTask(task.withRecurType(RecurType.YEARLY).withTagType(TagType.ERRAND));
            }
            activityModel.newTask(task.withRecurType(RecurType.YEARLY).withTagType(TagType.PENDING));
        }
        Objects.requireNonNull(getDialog()).dismiss();
    }

//    private void onNegativeButtonClick(DialogInterface dialog, int which) {
//        dialog.cancel();
//    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_create_task, container, false); // Replace with your actual layout file
        FragmentDialogCreateTaskBinding binding = this.view;
        if (binding.weeklyButton == null || binding.monthlyButton == null || binding.yearlyButton == null) {
            Log.e("YourFragment", "RadioButton weeklyButton not found in the layout");
            return view;
        }
        LocalDateTime currentTime = activityModel.getCurrentTime();
        LocalDateTime cutoffTime = activityModel.getCurrentTime().toLocalDate().atTime(2, 0,0);

        if (currentTime.isBefore(cutoffTime)) {
            cutoffTime = cutoffTime.minusDays(1);
        } else {
            cutoffTime = currentTime;
        }
        activityModel.setNewTime(cutoffTime);

        var onceTaskText = "Once";
        binding.onceButton.setText(onceTaskText);

        var dailyRecurText = "Daily";
        binding.dailyButton.setText(dailyRecurText);

        // Weekly button text
        var weeklyRecurText = "Weekly on " +
                activityModel.getCurrentTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        binding.weeklyButton.setText(weeklyRecurText);

        // Monthly button text
        int nthWeekday = activityModel.getCurrentTime().get(ChronoField.ALIGNED_WEEK_OF_MONTH);
        String ordinalIndicator;
        switch (nthWeekday) {
            case 1:
                ordinalIndicator = "st";
                break;
            case 2:
                ordinalIndicator = "nd";
                break;
            case 3:
                ordinalIndicator = "rd";
                break;
            default:
                ordinalIndicator = "th";
                break;
        }
        var monthlyRecurText = "Monthly on " + nthWeekday + ordinalIndicator + " " +
                activityModel.getCurrentTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        binding.monthlyButton.setText(monthlyRecurText);

        // Yearly button text
        var yearlyRecurText = "Yearly on " + DateTimeFormatter.ofPattern("MM/dd").format(activityModel.getCurrentTime());
        binding.yearlyButton.setText(yearlyRecurText);
        return view;
    }

}
