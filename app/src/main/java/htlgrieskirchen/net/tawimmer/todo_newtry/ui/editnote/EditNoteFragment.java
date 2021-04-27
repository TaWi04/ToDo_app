package htlgrieskirchen.net.tawimmer.todo_newtry.ui.editnote;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import htlgrieskirchen.net.tawimmer.todo_newtry.CalendarForToDo;
import htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity;
import htlgrieskirchen.net.tawimmer.todo_newtry.Label;
import htlgrieskirchen.net.tawimmer.todo_newtry.MultiSelectionSpinner;
import htlgrieskirchen.net.tawimmer.todo_newtry.Note;
import htlgrieskirchen.net.tawimmer.todo_newtry.R;
import htlgrieskirchen.net.tawimmer.todo_newtry.TodoList;
import htlgrieskirchen.net.tawimmer.todo_newtry.ui.addnote.AddNoteViewModel;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.currentListIndex;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.drawerMenuActivity;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.hideKeyboard;


public class EditNoteFragment extends Fragment {

    private AddNoteViewModel editNoteViewModel;
    private Note currentNote;
    static EditText txtDate, txtTime;
    private EditText txtTitle, txtDetails;
    private ProgressBar progressBar;
    static View root;
    private MultiSelectionSpinner spinnerForLabels;
    private TodoList todoList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        currentNote = (Note) bundle.getSerializable("Note");
        editNoteViewModel =
                new ViewModelProvider(this).get(AddNoteViewModel.class);
        root = inflater.inflate(R.layout.fragment_editnote, container, false);
        drawerMenuActivity.getSupportActionBar().setTitle(currentNote.getTitle());

        txtTitle = (EditText) root.findViewById(R.id.editTextTitle);
        txtTitle.setText(currentNote.getTitle());
        txtDetails = (EditText) root.findViewById(R.id.editTextToDo);
        if (currentNote.getNoteString() != null) {
            txtDetails.setText(currentNote.getNoteString());
        }
        todoList = allTodoLists.get(currentListIndex);
        txtDate = (EditText) root.findViewById(R.id.editTextDate);
        if (currentNote.getDate() != null) {
            txtDate.setText(currentNote.getFormatedDateString("dd.MM.yyyy", "hh:mm"));
        }

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePicker();
            }
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_check);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNote();
            }
        });
        FloatingActionButton floatingActionButtonDelete = (FloatingActionButton) root.findViewById(R.id.fab_delete);
        floatingActionButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
        txtTime = (EditText) root.findViewById(R.id.editTextTime);
        if (currentNote.getTime() != null) {
            txtTime.setText(currentNote.getTime().toString());
        }
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePicker();
            }
        });
        if (currentNote.getLabels() != null) {
            spinnerForLabels.setSelection((ArrayList<Label>) currentNote.getLabels());
        }
        progressBar = root.findViewById(R.id.progressBar);
        return root;
    }

    public void deleteNote() {
        todoList.removeNoteFromList(currentNote);
        todoList.addToList(currentNote); //on last index
        new YourAsyncTask().execute();
        hideKeyboard(getActivity(), txtDetails);
        hideKeyboard(getActivity(), txtTitle);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ShowToDoListAfterDeletion", todoList);
        Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editNoteFragment_to_nav_showTodoList, bundle);
    }

    public void editNote() {


        txtTime = (EditText) root.findViewById(R.id.editTextTime);

        txtDate = (EditText) root.findViewById(R.id.editTextDate);

        txtTitle = (EditText) root.findViewById(R.id.editTextTitle);
        if (txtTitle.length() <= 0) {
            txtTitle.setError("Must at least have 1 characters!");
            return;
        }
        txtDetails = (EditText) root.findViewById(R.id.editTextToDo);
        ToggleButton toggleButton = (ToggleButton) root.findViewById(R.id.toggleButton_important);

        updateNote(txtTime, txtDate, txtTitle, txtDetails, toggleButton);
        currentNote.isChecked();

        progressBar.setVisibility(View.VISIBLE);
        new YourAsyncTask().execute();

        hideKeyboard(getActivity(), txtDetails);
        hideKeyboard(getActivity(), txtTitle);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ShowToDoList", todoList);
        Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editNoteFragment_to_nav_showTodoList, bundle);
    }

    private void updateNote(EditText txtTime, EditText txtDate, EditText txtTitle, EditText txtDetails, ToggleButton toggleButton) {
        String title, details;
        LocalDate date;
        LocalTime time;
        boolean important;

        if (!(txtTime.getText().toString().length() < 1)) {
            time = LocalTime.parse(txtTime.getText().toString());
            currentNote.setTime(time);
        }
        if (!(txtDate.getText().toString().length() < 1)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            date = LocalDate.parse(txtDate.getText().toString(), dtf);
            currentNote.setDate(date);
        }
        title = txtTitle.getText().toString();
        currentNote.setTitle(title);
        details = txtDetails.getText().toString();
        if (details.length() > 0) {
            currentNote.setNoteString(details);
        }
        important = toggleButton.isChecked();
        currentNote.setImportant(important);
    }

    private class YourAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {


            synchronized (this) {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void createTimePicker() {
        CalendarForToDo cftd = new CalendarForToDo();
        cftd.createTimePickerDialog(requireActivity(), txtTime);
    }

    public void createDatePicker() {
        CalendarForToDo cftd = new CalendarForToDo();
        cftd.createDatePickerDialog(requireActivity(), txtDate);
    }
}