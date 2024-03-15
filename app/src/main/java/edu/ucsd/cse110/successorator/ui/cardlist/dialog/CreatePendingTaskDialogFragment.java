package edu.ucsd.cse110.successorator.ui.cardlist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreatePendingTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class CreatePendingTaskDialogFragment extends DialogFragment {
    private FragmentDialogCreatePendingTaskBinding view;
    private MainViewModel activityModel;

    CreatePendingTaskDialogFragment() {}

    public static CreatePendingTaskDialogFragment newInstance() {
        var fragment = new CreatePendingTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreatePendingTaskBinding.inflate(getLayoutInflater());
        // Click save button to save
        FragmentDialogCreatePendingTaskBinding binding = this.view;
        ImageButton saveButton = binding.saveButton;
        // Set a click listener for the Save ImageButton
        saveButton.setOnClickListener(this::onSaveButtonClick);
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

    private void onSaveButtonClick(View v) {
        var taskText = view.taskEditText.getText().toString();
        var context = getTaskContext();
        var task = new Task(null, taskText, false, -1,
                null, context, RecurType.PENDING,
                activityModel.getCurrentTime().atZone(ZoneId.systemDefault()).toEpochSecond(), true);
        if (!taskText.trim().isEmpty()) {
            activityModel.newTask(task);
        }
        Objects.requireNonNull(getDialog()).dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_create_task, container, false); // Replace with your actual layout file
        FragmentDialogCreatePendingTaskBinding binding = this.view;

        LocalDateTime currentTime = activityModel.getCurrentTime();
        LocalDateTime cutoffTime = activityModel.getCurrentTime().toLocalDate().atTime(2, 0,0);

        if (currentTime.isBefore(cutoffTime)) {
            cutoffTime = cutoffTime.minusDays(1);
        } else {
            cutoffTime = currentTime;
        }
        activityModel.setNewTime(cutoffTime);

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
