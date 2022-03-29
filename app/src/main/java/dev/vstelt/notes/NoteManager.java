package dev.vstelt.notes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NoteManager extends RecyclerView.Adapter<NoteManager.ViewHolder> {

    Context ctx;
    ArrayList<Note> notes;

    public NoteManager(Context ctx) {
        super();
        notes = new ArrayList<>();
        this.ctx = ctx;
        readAllNotes();
    }

    public void readAllNotes() {
        for (File f: ctx.getFilesDir().listFiles()) {
            Log.d("NoteManager", "Reading File: " + f.getName());
            try {
                List<String> lines = Files.readAllLines(f.toPath());
                String title = lines.get(0);

                StringBuffer content = new StringBuffer();
                for (int i=1; i<lines.size(); i++) {
                    content.append(lines.get(i));
                    content.append('\n');
                }
                notes.add(new Note(f.getName(), f.lastModified(), title, content.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(notes);
        Collections.reverse(notes);
    }

    public String nextId() {
        String id = UUID.randomUUID().toString();

        for (Note n: notes) {
            if (n.id.equals(id)) {
                return nextId();
            }
        }

        return id;
    }

    private long writeNote(Note n) {
        File f = new File(ctx.getFilesDir(), n.id);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(n.title);
            fw.write('\n');
            fw.write(n.content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f.lastModified();
    }

    public void syncNote(String id, String title, String content) {
        Note old = null;

        for (Note n: notes) {
            if (n.id.equals(id)) {
                old = n;
                break;
            }
        }

        if (title.equals("") && content.equals("") && old!=null) {
            notes.remove(id);
            new File(ctx.getFilesDir(), id).delete();
        } else {
            if (old == null) {
                old = new Note(id, 0L, title, content);
                notes.add(old);
            } else {
                old.title = title;
                old.content = content;
            }
            long time = writeNote(old);
            old.modified = time;
        }

        Collections.sort(notes);
        Collections.reverse(notes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteManager.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new NoteManager.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NoteManager.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Note n = notes.toArray(new Note[]{})[position];

        viewHolder.getTitle().setText(n.title);
        viewHolder.getDescription().setText(n.content);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), NoteActivity.class);
                i.putExtra("ID", n.id);
                i.putExtra("TITLE", n.title);
                i.putExtra("CONTENT", n.content);

                view.getContext().startActivity(i);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            title = view.findViewById(R.id.noteTitle);
            description = view.findViewById(R.id.noteDescription);
        }

        public TextView getTitle() {
            return title;
        }
        public TextView getDescription() { return description; }
    }
}
