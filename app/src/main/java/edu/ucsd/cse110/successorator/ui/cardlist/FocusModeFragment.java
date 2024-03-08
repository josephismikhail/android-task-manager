package edu.ucsd.cse110.successorator.ui.cardlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;


import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;

public class FocusModeFragment extends Fragment {

    private TextView selectedModeTextView;
    private FragmentTaskListBinding view;

    public FocusModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Initialize UI components
        selectedModeTextView = view.date;

        // Retrieve mode name from the arguments (if passed)
        if (getArguments() != null) {
            String modeName = getArguments().getString("mode");
            updateSelectedMode(modeName);
        }

        return view;
    }

    // Method to update the selected mode in the UI
    private void updateSelectedMode(String modeName) {
        if (selectedModeTextView != null) {
            selectedModeTextView.setText("Selected Mode: " + modeName);
        }
    }
}

