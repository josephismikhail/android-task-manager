package edu.ucsd.cse110.successorator.ui.cardlist.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Objects;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateRecurringTaskBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class CreateRecurringTaskDialogFragment extends DialogFragment {
    private FragmentDialogCreateRecurringTaskBinding view;
    private MainViewModel activityModel;
    private LocalDate selectedDate;

    CreateRecurringTaskDialogFragment() {}

    public static CreateRecurringTaskDialogFragment newInstance() {
        var fragment = new CreateRecurringTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateRecurringTaskBinding.inflate(getLayoutInflater());
        selectedDate = LocalDate.now();
        updateDateButtonText();
        // Click save button to save
        FragmentDialogCreateRecurringTaskBinding binding = this.view;
        ImageButton saveButton = binding.saveButton;
        // Set a click listener for the Save ImageButton
        saveButton.setOnClickListener(this::onSaveButtonClick);
        // Click date button to show date picker
        Button dateSelectButton = view.dateSelect;
        dateSelectButton.setOnClickListener(this::showDatePickerDialog);
        return new AlertDialog.Builder(getActivity())
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

    private void showDatePickerDialog(View v) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue() - 1; // DatePickerDialog uses 0-indexed months!
        int day = now.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), (view, yearSelected, monthOfYear, dayOfMonth) -> {
            selectedDate = LocalDate.of(yearSelected, monthOfYear + 1, dayOfMonth);
            updateDateButtonText();
            updateRecurringOptionLabels();
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateDateButtonText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = selectedDate.format(formatter);
        view.dateSelect.setText(formattedDate); // Update the button text
    }

    private void updateRecurringOptionLabels() {
        if (selectedDate == null) return;

        // Update button texts based on the selected date
        DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        String weeklyText = "Weekly on " + selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String monthlyText = "Monthly on " + selectedDate.format(DateTimeFormatter.ofPattern("MMM dd"));
        String yearlyText = "Yearly on " + selectedDate.format(DateTimeFormatter.ofPattern("MMM dd"));

        view.weeklyButton.setText(weeklyText);
        view.monthlyButton.setText(monthlyText);
        view.yearlyButton.setText(yearlyText);
    }

    private void onSaveButtonClick(View v) {
        var taskText = view.taskEditText.getText().toString();
        if (taskText.trim().isEmpty() || selectedDate == null) { return; }

        var context = getTaskContext();

        long epochSecond = selectedDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        var task = new Task(null, taskText, false, -1,
                null, context, RecurType.DAILY,
                epochSecond, true);

        if (view.dailyButton.isChecked()) {
            activityModel.newTask(task.withRecurType(RecurType.DAILY));
        } else if (view.weeklyButton.isChecked()) {
            activityModel.newTask(task.withRecurType(RecurType.WEEKLY));
        } else if (view.monthlyButton.isChecked()) {
            activityModel.newTask(task.withRecurType(RecurType.MONTHLY));
        } else if (view.yearlyButton.isChecked()) {
            activityModel.newTask(task.withRecurType(RecurType.YEARLY));
        }
        Objects.requireNonNull(getDialog()).dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_create_task, container, false); // Replace with your actual layout file
        FragmentDialogCreateRecurringTaskBinding binding = this.view;
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

//        var onceTaskText = "Once";
//        binding.onceButton.setText(onceTaskText);

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

    private TaskContext getTaskContext() {
        if (view.homeButton.isChecked()) {
            return TaskContext.HOME;
        }
        else if (view.schoolButton.isChecked()) {
            return TaskContext.SCHOOL;
        }
        else if (view.workButton.isChecked()) {
            return TaskContext.WORK;
        }
        else {
            return TaskContext.ERRAND;
        }
    }

}
