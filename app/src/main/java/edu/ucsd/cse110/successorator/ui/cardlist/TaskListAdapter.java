package edu.ucsd.cse110.successorator.ui.cardlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.TagType;
import edu.ucsd.cse110.successorator.lib.domain.Task;


public class TaskListAdapter extends ArrayAdapter<Task> {
    private final Consumer<Task> onTaskClicked;

    public TaskListAdapter(
            Context context,
            List<Task> tasks,
            Consumer<Task> onTaskClicked) {
        super(context, 0, new ArrayList<>(tasks));
        this.onTaskClicked = onTaskClicked;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        assert task != null;

        ListItemTaskBinding binding;
        if (convertView != null) {
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        binding.tag.setText(task.getTagType().toString().indexOf(0,1));
        var layoutInflater = LayoutInflater.from(getContext());
//        @NonNull FragmentTaskListBinding binding2 = FragmentTaskListBinding.inflate(layoutInflater, parent, false);

        if(task.getTagType() == TagType.HOME) {
            binding.tag.setBackgroundColor(Color.parseColor("#F4EEBB"));
//            binding2.mode.setBackgroundColor(Color.parseColor("#F4EEBB"));
        } else if (task.getTagType() == TagType.WORK) {

        }else if (task.getTagType() == TagType.SCHOOL) {

        }else if (task.getTagType() == TagType.ERRAND) {

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
