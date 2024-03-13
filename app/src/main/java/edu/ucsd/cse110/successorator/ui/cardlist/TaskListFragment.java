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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.data.db.TaskEntity;
import edu.ucsd.cse110.successorator.databinding.FocusModeDialogBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.ui.cardlist.dialog.CreateTaskDialogFragment;

public class TaskListFragment extends Fragment{
    private FragmentTaskListBinding view;
    private FocusModeDialogBinding focusMenu;
    private MainViewModel activityModel;
    private TaskListAdapter adapter;

    private FocusModeArrayAdapter adapter2;

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

        Spinner focusModeButton = view.mode;
        focusModeButton.setOnClickListener(this::onFocusButtonClick);
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

    private void onFocusButtonClick(View view) {

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


        Spinner focusModeButton = view.mode;
// Example data for the spinner
        String[] items = new String[]{"Home Mode", "Work Mode", "School Mode", "Errand Mode"};

        this.adapter2 = new FocusModeArrayAdapter(requireContext(), items, focus -> {
            focusModeButton.setOnClickListener(v -> {
                adapter2.getDropDownView(requireContext(), items, focus);
                adapter2.show(getParentFragmentManager(), "CreateTaskDialogFragment");
            });
            // 1. Create sorting method in MainViewModel like 'sorttask(String focus)', that returns a list of task that according to different mode
        });
        focusModeButton.setAdapter(adapter2);

        // 2. change the view outside, so it could create a new task view according to different mode user click



        //focusModeButton.setEmptyView(view.emptyText);
        // TextView title = view.date;
// Handling item selection
        // focusModeButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           // @Override
            //public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // change title
//                String selectedItem = items[position];
//                int itemId = selectedItem.getItemId();
//                Intent intent = new Intent(getActivity(), FocusModeActivity.class);
//                if (itemId == R.id.home_mode) {
//                    intent.putExtra("mode", "Home Mode");
//                } else if (itemId == R.id.work_mode) {
//                    intent.putExtra("mode", "Work Mode");
//                } else if (itemId == R.id.school_mode) {
//                    intent.putExtra("mode", "School Mode");
//                } else if (itemId == R.id.errand_mode) {
//                    intent.putExtra("mode", "Errand Mode");
//                }
//                startActivity(intent);
                // title.setText(selectedItem);
                // change task list accodring to name
//            }
//            //@Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Optional: Do something when no item is selected
//            }
//        });

//        NavigationView navigationView = view.FocusModeView;
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int itemId = item.getItemId();
//                Intent intent = new Intent(getActivity(), FocusModeActivity.class);
//                if (itemId == R.id.home_mode) {
//                    intent.putExtra("mode", "Home Mode");
//                } else if (itemId == R.id.work_mode) {
//                    intent.putExtra("mode", "Work Mode");
//                } else if (itemId == R.id.school_mode) {
//                    intent.putExtra("mode", "School Mode");
//                } else if (itemId == R.id.errand_mode) {
//                    intent.putExtra("mode", "Errand Mode");
//                }
//                startActivity(intent);
//                // Close the navigation drawer if needed
//                return true;
//            }
//        });


        TextView dateTextView = view.getRoot().findViewById(R.id.date);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime cutoffTime = currentTime.toLocalDate().atTime(2,0,0);

        if (currentTime.isBefore(cutoffTime)) {
            cutoffTime = cutoffTime.minusDays(1);
        }
        else {
            cutoffTime = currentTime;
//            activityModel.deleteCompletedTasksBefore(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond());//.toInstant().toEpochMilli());
            activityModel.deleteCompletedTasks(true);
            activityModel.updateDisplayTask(LocalDateTime.now());
        }

        activityModel.setNewTime(cutoffTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MM/dd");
        dateTextView.setText(activityModel.getCurrentTime().format(formatter));

        // Forward date button
        view.dateButton.setOnClickListener(v -> {
            activityModel.setNewTime(activityModel.getCurrentTime().plusDays(1));
            dateTextView.setText(activityModel.getCurrentTime().format(formatter));
            activityModel.deleteCompletedTasks(true);
            activityModel.updateDisplayTask(activityModel.getCurrentTime());
        });

        return view.getRoot();
    }

}
