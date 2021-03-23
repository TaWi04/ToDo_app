package htlgrieskirchen.net.tawimmer.todo_newtry;


import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.root;

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

    public void removeNoteFromList(Note note) {
        notes.remove(note);
        checkedNotes.remove(note);
        Snackbar snackbar = Snackbar
                .make(root, "Do you want to reverse your changes?", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar1 = Snackbar.make(root, "Message is restored!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
                snackbar.show();
            }
        });
    }

    public void editNote(Note current, Note changed){
        int index = notes.indexOf(current);
        if(index == -1){
            index = checkedNotes.indexOf(current);
            checkedNotes.remove(index);
            checkedNotes.add(index,changed);
        }else{
            notes.remove(index);
            notes.add(index,changed);
        }
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
