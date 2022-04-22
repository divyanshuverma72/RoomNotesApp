package com.example.roomnotesapp;

import static com.example.roomnotesapp.AddNoteActivity.NOTE_BUTTON;
import static com.example.roomnotesapp.AddNoteActivity.NOTE_DATE;
import static com.example.roomnotesapp.AddNoteActivity.NOTE_DESC;
import static com.example.roomnotesapp.AddNoteActivity.NOTE_ID;
import static com.example.roomnotesapp.AddNoteActivity.NOTE_TITLE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.roomnotesapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NoteClickListener {

    private NoteViewModel noteViewModel;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        NoteAdapter noteAdapter = new NoteAdapter(this);
        noteAdapter.setHasStableIds(true);
        noteAdapter.setNoteClickListener(this);

        activityMainBinding.recyclerView.setHasFixedSize(true);
        activityMainBinding.recyclerView.setAdapter(noteAdapter);
        activityMainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {
            Log.d("", "onChanged: ");
            if (notes.isEmpty()) {
                activityMainBinding.emptyMsg.setVisibility(View.VISIBLE);
            } else {
                activityMainBinding.emptyMsg.setVisibility(View.GONE);
            }
            noteAdapter.setNotes(notes);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addNotes) {
            Intent intent = new Intent(this, AddNoteActivity.class);
            callAddNoteActivityForResult.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> callAddNoteActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String title = result.getData().getStringExtra(NOTE_TITLE);
                    String desc = result.getData().getStringExtra(NOTE_DESC);
                    String date = result.getData().getStringExtra(NOTE_DATE);
                    String buttonName = result.getData().getStringExtra(NOTE_BUTTON);
                    Note note = new Note(title, desc, date);
                    if (result.getData().hasExtra(NOTE_ID)) {
                        int id = result.getData().getIntExtra(NOTE_ID, -1);
                        if (id == -1) {
                            Toast.makeText(this, "Note can not be updated.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        note.setId(id);
                    }
                    Log.d("TAG", ": button nhg "+buttonName);
                    switch (buttonName) {
                        case "ADD":
                            noteViewModel.insert(note);
                            break;
                        case "UPDATE":
                            noteViewModel.update(note);
                            break;
                        case "DELETE":
                            noteViewModel.delete(note);
                            break;
                    }
                }
            }
    );

    @Override
    public void onNoteClicked(int noteId, String title, String description, String date) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra(NOTE_ID, noteId);
        intent.putExtra(NOTE_TITLE, title);
        intent.putExtra(NOTE_DESC, description);
        intent.putExtra(NOTE_DATE, date);

        callAddNoteActivityForResult.launch(intent);
    }
}