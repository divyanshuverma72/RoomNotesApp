package com.example.roomnotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.roomnotesapp.databinding.ActivityAddNoteBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NOTE_ID = "note_id";
    public static final String NOTE_TITLE = "note_title";
    public static final String NOTE_DESC = "note_desc";
    public static final String NOTE_DATE = "note_date";
    public static final String NOTE_BUTTON = "note_button";

    private ActivityAddNoteBinding activityAddNoteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddNoteBinding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(activityAddNoteBinding.getRoot());

        activityAddNoteBinding.addButton.setOnClickListener(this);
        activityAddNoteBinding.updateButton.setOnClickListener(this);
        activityAddNoteBinding.deleteButton.setOnClickListener(this);

        if (!getIntent().hasExtra(NOTE_ID)) {
            setTitle("Add note");
            activityAddNoteBinding.addButton.setVisibility(View.VISIBLE);
            activityAddNoteBinding.deleteButton.setVisibility(View.GONE);
            activityAddNoteBinding.updateButton.setVisibility(View.GONE);
        } else {
            setTitle("Update note");
            activityAddNoteBinding.title.setText(getIntent().getStringExtra(NOTE_TITLE));
            activityAddNoteBinding.description.setText(getIntent().getStringExtra(NOTE_DESC));

            activityAddNoteBinding.addButton.setVisibility(View.GONE);
            activityAddNoteBinding.deleteButton.setVisibility(View.VISIBLE);
            activityAddNoteBinding.updateButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        final String titleName = activityAddNoteBinding.title.getText().toString();
        final String description = activityAddNoteBinding.description.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy hh.mm aa", Locale.ENGLISH);
        Date date = new Date();
        final String modifiedDate = formatter.format(date);
        Intent data = new Intent();

        data.putExtra(NOTE_TITLE, titleName);
        data.putExtra(NOTE_DESC, description);
        data.putExtra(NOTE_DATE, modifiedDate);

        int id = getIntent().getIntExtra(NOTE_ID, -1);
        if (id != -1) {
            data.putExtra(NOTE_ID, id);
        }

        if (view.getId() == R.id.addButton) {
            data.putExtra(NOTE_BUTTON, "ADD");
        } else if (view.getId() == R.id.updateButton) {
            data.putExtra(NOTE_BUTTON, "UPDATE");
        } else if (view.getId() == R.id.deleteButton) {
            data.putExtra(NOTE_BUTTON, "DELETE");
        }

        setResult(RESULT_OK, data);
        finish();
    }
}