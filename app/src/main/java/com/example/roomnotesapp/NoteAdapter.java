package com.example.roomnotesapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomnotesapp.databinding.NoteItemBinding;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();
    private NoteItemBinding noteItemBinding;
    private final Context context;
    private NoteClickListener noteClickListener;

    public NoteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        noteItemBinding = NoteItemBinding.inflate(LayoutInflater.from(context));
        return new NoteHolder(noteItemBinding.getRoot());
    }

    public void setNoteClickListener(NoteClickListener noteClickListener) {
        this.noteClickListener = noteClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.textViewTitle.setText(notes.get(position).getTitle());
        holder.textViewDesc.setText(notes.get(position).getDescription());
        holder.textViewDate.setText(notes.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "getItemCount: "+notes.isEmpty() + notes.size());
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewTitle;
        private final TextView textViewDesc;
        private final TextView textViewDate;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = NoteAdapter.this.noteItemBinding.titleTextView;
            textViewDesc = NoteAdapter.this.noteItemBinding.descTextView;
            textViewDate = NoteAdapter.this.noteItemBinding.date;

            NoteAdapter.this.noteItemBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            noteClickListener.onNoteClicked(notes.get(getAdapterPosition()).getId(),
                    notes.get(getAdapterPosition()).getTitle(), notes.get(getAdapterPosition()).getDescription(),
                    notes.get(getAdapterPosition()).getDescription());
        }
    }
}
