package dev.vstelt.notes;

public class Note implements Comparable<Note> {
    public String id;
    public String title;
    public String content;
    public long modified;

    public Note(String id, long modified, String title, String content) {
        this.id = id;
        this.modified = modified;
        this.title = title;
        this.content = content;
    }

    @Override
    public int compareTo(Note note) {
        return (int) (modified-note.modified);
    }
}
