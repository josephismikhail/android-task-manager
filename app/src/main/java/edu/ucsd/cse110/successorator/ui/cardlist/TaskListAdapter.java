package edu.ucsd.cse110.successorator.ui.cardlist;

import static edu.ucsd.cse110.successorator.lib.domain.Context.ERRAND;
import static edu.ucsd.cse110.successorator.lib.domain.Context.HOME;
import static edu.ucsd.cse110.successorator.lib.domain.Context.SCHOOL;
import static edu.ucsd.cse110.successorator.lib.domain.Context.WORK;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TaskListAdapter extends ArrayAdapter<Task> {
    private final Consumer<Task> onTaskClicked;
    private final Consumer<Task> onTaskLongPress;

    public TaskListAdapter(
            Context context,
            List<Task> tasks,
            Consumer<Task> onTaskClicked,
            Consumer<Task> onTaskLongPress) {
        super(context, 0, new ArrayList<>(tasks));
        this.onTaskClicked = onTaskClicked;
        this.onTaskLongPress = onTaskLongPress;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if (task == null) {System.out.println("task is null in TLA");}
        assert task != null;

        ListItemTaskBinding binding;
        if (convertView != null) {
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        binding.tag.setText(task.getContext().toString().substring(0, 1));
        var layoutInflater = LayoutInflater.from(getContext());
//        @NonNull FragmentTaskListBinding binding2 = FragmentTaskListBinding.inflate(layoutInflater, parent, false);

        if(task.getContext() == HOME) {
            binding.tag.setBackgroundColor(Color.parseColor("#F4EEBB"));
//            binding2.mode.setBackgroundColor(Color.parseColor("#F4EEBB"));
        } else if (task.getContext() == WORK) {

        }else if (task.getContext() == SCHOOL) {

        }else if (task.getContext() == ERRAND) {

        }

        // M -> V
        // Make the view match the model.
        binding.taskText.setText(task.getTask());
        if (task.isCompleted()) {
            binding.taskText.setPaintFlags(binding.taskText.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            binding.taskText.setPaintFlags(binding.taskText.getPaintFlags()
                    & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // V -> M
        // Bind clicks to update the model.
        binding.getRoot().setOnClickListener(v -> {
            onTaskClicked.accept(task);
        });

        binding.getRoot().setOnLongClickListener(v -> {
            onTaskLongPress.accept(task);
            return true;
        });


        return binding.getRoot();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var task = getItem(position);
        assert task != null;

        var id = task.getId();
        assert id != null;

        return id;
    }
}
