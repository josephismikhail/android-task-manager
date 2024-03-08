package edu.ucsd.cse110.successorator.ui.cardlist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class CreateTaskDialogFragment extends DialogFragment {
    private FragmentDialogCreateTaskBinding view;
    private MainViewModel activityModel;


    CreateTaskDialogFragment() {

    }

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
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
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
        var taskText = view.taskFrontEditText.getText().toString();
        if (taskText.trim().isEmpty()) {
            //not valid
        } else {
            var task = new Task(-1, taskText, false, -1, null);
            activityModel.prepend(task);
            getDialog().dismiss();
        }
    }
    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var taskText = view.taskFrontEditText.getText().toString();
        if (taskText.trim().isEmpty()) {
            dialog.dismiss();
        } else {
            var task = new Task(-1, taskText, false, -1, null);
            activityModel.prepend(task);

            dialog.dismiss();
        }
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_create_task, container, false); // Replace with your actual layout file
        FragmentDialogCreateTaskBinding binding = this.view;
        if (binding.weeklyButton == null || binding.monthlyButton == null || binding.yearlyButton == null) {
            Log.e("YourFragment", "RadioButton weeklyButton not found in the layout");
            return view;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime cutoffTime = LocalDate.now().atTime(2, 0,0);

        if (currentTime.isBefore(cutoffTime)) {
            cutoffTime = cutoffTime.minusDays(1);
        } else {
            cutoffTime = currentTime;
        }
        activityModel.setNewTime(cutoffTime);

        // set week day
        binding.weeklyButton.setText("weekly on " + activityModel.getCurrentTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));

        // set month date
        int dayOfMonth = (activityModel.getCurrentTime().getDayOfMonth());
        String ordinalIndicator;
        if (dayOfMonth >= 11 && dayOfMonth <= 13) {
            ordinalIndicator = "th";
        } else {
            switch (dayOfMonth % 10) {
                case 1: ordinalIndicator = "st";
                break;
                case 2: ordinalIndicator = "nd";
                break;
                case 3: ordinalIndicator = "rd";
                break;
                default: ordinalIndicator = "th";
                break;
            }
        }
        binding.monthlyButton.setText("monthly on " + String.valueOf(dayOfMonth) + ordinalIndicator + " " + activityModel.getCurrentTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));

        // set yearly
        String yearlyTime = DateTimeFormatter.ofPattern("MM/dd").format(activityModel.getCurrentTime());
        binding.yearlyButton.setText("yearly on " + yearlyTime);
        return view;

    }

}
