package htlgrieskirchen.net.tawimmer.todo_newtry;


import java.io.Serializable;
import java.util.ArrayList;

public class TodoList implements Serializable {

    private String title;
    private ArrayList<Note> notes;
    private ArrayList<Note> checkedNotes;
    private ArrayList<Label> labels;

    public TodoList(String title, ArrayList<Note> notes, ArrayList<Label> labels) {
        this.title = title;
        this.notes = notes;
        this.labels = labels;
        checkedNotes = new ArrayList<>();
    }

    public void addToList(Note note) {
        notes.add(note);
    }

    public void checkNote(int index) {
        Note note = notes.remove(index);
        checkedNotes.add(0,note);
        note.setChecked(true);
    }

    public void uncheckNote(int index) {
        Note note = checkedNotes.remove(index);
        notes.add(0,note);
        note.setChecked(false);
    }

    public Note removeNoteFromList(Note note) {
                   notes.remove(note);
                   checkedNotes.remove(note);
          return note;
        }


    public String getTitle() {
        return title;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public ArrayList<Note> getCheckedNotes() {
        return checkedNotes;
    }

    public ArrayList<Label> getLabel() {
        return labels;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLabel(ArrayList<Label> labels) {
        this.labels = labels;
    }
}
