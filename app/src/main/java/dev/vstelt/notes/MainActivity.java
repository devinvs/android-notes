package dev.vstelt.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static NoteManager notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notes = new NoteManager(this);

        recyclerView = findViewById(R.id.noteList);

        recyclerView.setAdapter(notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClick(View view) {
        Intent i = new Intent(this, NoteActivity.class);
        String id = notes.nextId();
        i.putExtra("TITLE", "");
        i.putExtra("CONTENT", "");
        i.putExtra("ID", id);
        startActivity(i);
    }
}