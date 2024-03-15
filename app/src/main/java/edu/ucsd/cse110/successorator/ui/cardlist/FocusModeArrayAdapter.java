package edu.ucsd.cse110.successorator.ui.cardlist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.databinding.FocusModeDialogBinding;
import edu.ucsd.cse110.successorator.databinding.ListItemTaskBinding;

public class FocusModeArrayAdapter extends ArrayAdapter<String> {
    private final Consumer<String> onFocusClicked;

    public FocusModeArrayAdapter(Context context, String[] items, Consumer<String> onFocusClicked) {
        super(context, 0, items);
        this.onFocusClicked = onFocusClicked;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Implement your custom view for the selected item (if needed)
        // This method is called for the view shown when the spinner is closed
        String focus = getItem(position);
        assert focus != null;

        FocusModeDialogBinding binding;
        if (convertView != null) {
            binding = FocusModeDialogBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = FocusModeDialogBinding.inflate(layoutInflater, parent, false);
        }

        // M -> V
        // Make the view match the model.
        binding.modeString.setText(focus);

        // V -> M
        // Bind clicks to update the model.
        binding.getRoot().setOnClickListener(v -> {
            onFocusClicked.accept(focus);
        });


        return binding.getRoot();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Implement your custom view for each dropdown item
        // This method is called for the views shown in the dropdown
        return super.getDropDownView(position, convertView, parent);
    }
}
