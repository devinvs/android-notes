package dev.vstelt.notes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText contentText;
    private TextWatcher watcher;

    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        titleText = findViewById(R.id.editTitle);
        contentText = findViewById(R.id.editContent);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String title = intent.getStringExtra("TITLE");
        String content = intent.getStringExtra("CONTENT");

        noteId = id;
        titleText.setText(title);
        contentText.setText(content);

        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                onChange();
            }
        };

        titleText.addTextChangedListener(watcher);
        contentText.addTextChangedListener(watcher);

        contentText.requestFocus();
    }

    public void onChange() {
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();

        MainActivity.notes.syncNote(noteId, title, content);
    }

    public void onDelete(View view) {
        MainActivity.notes.deleteNote(noteId);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
