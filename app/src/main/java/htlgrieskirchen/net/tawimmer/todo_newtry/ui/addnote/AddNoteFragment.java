package htlgrieskirchen.net.tawimmer.todo_newtry.ui.addnote;

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

import htlgrieskirchen.net.tawimmer.todo_newtry.CalendarForToDo;
import htlgrieskirchen.net.tawimmer.todo_newtry.Note;
import htlgrieskirchen.net.tawimmer.todo_newtry.R;
import htlgrieskirchen.net.tawimmer.todo_newtry.TodoList;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.currentListIndex;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.hideKeyboard;

public class AddNoteFragment extends Fragment {

    private AddNoteViewModel addNoteViewModel;
    static EditText txtDate, txtTime, txtTitle, txtDetails;
    private ProgressBar progressBar;
    static View root;
    private TodoList todoList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addNoteViewModel =
                new ViewModelProvider(this).get(AddNoteViewModel.class);
        root = inflater.inflate(R.layout.fragment_addnotes, container, false);
        txtDate = (EditText) root.findViewById(R.id.editTextDate);
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
                addNote();
            }
        });

        txtTime = (EditText) root.findViewById(R.id.editTextTime);
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePicker();
            }
        });

        todoList = allTodoLists.get(currentListIndex);
        progressBar = root.findViewById(R.id.progressBar);
        return root;
    }

    public void addNote() {

        txtTime = (EditText) root.findViewById(R.id.editTextTime);

        txtDate = (EditText) root.findViewById(R.id.editTextDate);

        txtTitle = (EditText) root.findViewById(R.id.editTextTitle);
        if (txtTitle.length() <= 0) {
            txtTitle.setError("Must at least have 1 characters!");
            return;
        }
        txtDetails = (EditText) root.findViewById(R.id.editTextToDo);
        ToggleButton toggleButton = (ToggleButton) root.findViewById(R.id.toggleButton_important);
        todoList.addToList(createNote(txtTime, txtDate, txtTitle, txtDetails, toggleButton));
        progressBar.setVisibility(View.VISIBLE);

        new YourAsyncTask().execute();
        hideKeyboard(getActivity(), txtDetails);
        hideKeyboard(getActivity(), txtTitle);
        //if (current != null) current.clearFocus();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ShowToDoList", todoList);
        Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_addNote_to_showTodoListFragment, bundle);

    }

    private Note createNote(EditText txtTime, EditText txtDate, EditText txtTitle, EditText txtDetails, ToggleButton toggleButton) {
        String title, details;
        LocalDate date = null;
        LocalTime time = null;
        boolean important;

        if (!(txtTime.getText().toString().length() < 1)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            time = LocalTime.parse(txtTime.getText().toString(), dtf);
        }
        if (!(txtDate.getText().toString().length() < 1)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            date = LocalDate.parse(txtDate.getText().toString(), dtf);
        }


        title = txtTitle.getText().toString();
        if (txtDetails.getText().toString().length() < 1) {
            details = "";
        } else {
            details = txtDetails.getText().toString();
        }
        important = toggleButton.isChecked();


        if (time == null) {
            if (date == null) {
                return new Note(title, details, important);
            } else {
                return new Note(title, details, date, important);
            }
        } else {
            return new Note(title, details, date, time, important);
        }

    }

    private class YourAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {


            synchronized (this) {
                try {
                    wait(1500);
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