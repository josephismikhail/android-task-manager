package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();

        if (itemId == R.id.action_bar_menu_forward_date) {
            forwardDate();
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    Citing Source
    a) The link: https://www.geeksforgeeks.org/simpledateformat-parse-method-in-java-with-examples/
    b) The title of the link: SimpleDateFormat parse() Method in Java with Examples
    c) The date captured: 02.13.2024
    d) How the source was used: Was used as a source of information to figure out how to
    parse a string that is in a specific date format into a date object.

    Team Member: William Guo (github username: William2399(
     */
    private void forwardDate() {

        //Get app's current date in string form
        TextView dateTextView = view.getRoot().findViewById(R.id.date);
        String currentTime = dateTextView.getText().toString();
        int currentYear = LocalDate.now().getYear();

        // Convert string to Date object
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        Date currentDate = null;
        try {
            currentDate = sdf.parse(currentTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Create Calendar object whose time is that of the app's current date
        Calendar calendar = Calendar.getInstance();
        assert currentDate != null;
        calendar.setTime(currentDate);
        calendar.set(Calendar.YEAR, currentYear);

        // Increment the date by one day
        calendar.add(Calendar.DATE, 1);

        // Get the updated date and time
        Date updatedTime = calendar.getTime();
        String updatedTimeString = sdf.format(updatedTime);

        // Update the text of the TextView with the formatted date
        dateTextView.setText(updatedTimeString);
    }
}
