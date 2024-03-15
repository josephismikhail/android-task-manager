package edu.ucsd.cse110.successorator.ui.cardlist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.Objects;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.RecurringTaskMenuBinding;
import edu.ucsd.cse110.successorator.lib.domain.RecurType;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskViews;

public class CreateRecurringMenuFragment extends DialogFragment {
    private RecurringTaskMenuBinding view;
    private MainViewModel activityModel;
    private Task task;

    CreateRecurringMenuFragment(Task task) { this.task = task; }

    public static CreateRecurringMenuFragment newInstance(Task task) {
        var fragment = new CreateRecurringMenuFragment(task);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = RecurringTaskMenuBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .setPositiveButton("Save", this::onPositiveButtonClick)
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

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        if (view.deleteRecurring.isChecked()) {
            var newTask = task.withRecurType(RecurType.ONCE);
            activityModel.deleteTask(task.getId());
            activityModel.newTask(newTask);
        }
        Objects.requireNonNull(getDialog()).dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) { dialog.cancel(); }
}
