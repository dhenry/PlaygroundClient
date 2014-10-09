package com.hackathon.playground.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hackathon.playground.app.R;
import com.hackathon.playground.app.loader.AttendeeUpdateTask;
import com.hackathon.playground.app.model.PlayDate;

import java.util.List;

/**
 * Author: Dave
 */
public class AttendeesFragment extends ListFragment {

    private PlayDate playDate;
    private View attendeeList;
    private Button addAttendeeButton;
    private ArrayAdapter<String> adapter;
    private String newAttendee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendees_list, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            playDate = bundle.getParcelable(PlayDateDetailsActivity.SELECTED_PLAY_DATE);
            setAttendees(playDate.getAttendees());
        }
        attendeeList = view;

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        addAttendeeButton = (Button)attendeeList.findViewById(R.id.add_attendee_button);

        addAttendeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AttendeesFragment.this.getActivity());
                builder.setTitle("Please enter attendee name");

                // Set up the input
                final EditText input = new EditText(AttendeesFragment.this.getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newAttendee = input.getText().toString();
                        new AttendeeUpdateTask(AttendeesFragment.this).execute(playDate.getId(), "addattendee", newAttendee);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void setAttendees(List<String> attendees) {
        adapter.clear();
        adapter.addAll(attendees);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
    }

    public void refreshView(PlayDate result) {
        Toast.makeText(getActivity(), newAttendee + " is now attending the playdate", Toast.LENGTH_LONG).show();
        setAttendees(result.getAttendees());
    }
}

