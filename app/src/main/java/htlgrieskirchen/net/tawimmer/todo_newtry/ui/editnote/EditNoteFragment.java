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

         txtTitle = (EditText) root.findViewById(R.id.editTextTitle);
         txtTitle.setText(currentNote.getTitle());
         txtDetails = (EditText) root.findViewById(R.id.editTextToDo);
        if(currentNote.getNoteString() != null){
            txtDetails.setText(currentNote.getNoteString());
        }
        todoList = allTodoLists.get(currentListIndex);
       txtDate = (EditText) root.findViewById(R.id.editTextDate);
       if(currentNote.getDate() != null){
           txtDate.setText(currentNote.getFormatedDateString("dd.MM.yyyy","hh:mm"));
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
        txtTime = (EditText) root.findViewById(R.id.editTextTime);
        if(currentNote.getTime() != null){
            txtTime.setText(currentNote.getTime().toString());
        }
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePicker();
            }
        });
        setUpReportSelection();
        if(currentNote.getLabels() != null){
            spinnerForLabels.setSelection((ArrayList<Label>) currentNote.getLabels());
        }

        //spinnerForLabels = (MultiSelectionSpinner) root.findViewById(R.id.spinnerLabel);

       /* progress = new ProgressDialog(root.getContext());
        progress.setCancelable(false);
        progress.setTitle("Progress");*/
        progressBar = root.findViewById(R.id.progressBar);
        /*addNoteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                txtTime.setText(s);
                txtDate.setText(s);
                //textView.setText(s);
            }
        });*/
        return root;
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


        //todoList.editNote(currentNote,changed);
       // todoList.addToList();


        //progressBar.onVisibilityAggregated(true);
        progressBar.setVisibility(View.VISIBLE);

        //startActivity(new Intent(this, addNotes.class));

        new YourAsyncTask().execute();

        hideKeyboard(getActivity(),txtDetails);
        hideKeyboard(getActivity(),txtTitle);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ShowToDoList", todoList);
        Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editNoteFragment_to_nav_showTodoList,bundle);

    }

    private void updateNote(EditText txtTime, EditText txtDate, EditText txtTitle, EditText txtDetails, ToggleButton toggleButton) {
        String title, details;
        LocalDate date;
        LocalTime time;
        boolean important;

        if( !(txtTime.getText().toString().length() <1)){
            time = LocalTime.parse(txtTime.getText().toString());
            currentNote.setTime(time);
        }
        if( !(txtDate.getText().toString().length() <1)){
           DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("dd.MM.yyyy");
            //dtf.format((txtDate.getText().toString()));
            //String[] val = txtDate.getText().toString().split("")
            date = LocalDate.parse(txtDate.getText().toString(),dtf); //,dtf);//TODO HELLLPPP
            //date.format(dtf);

           // String dateString =  note.getFormatedDateString("dd.MM.yyyy","hh:mm");
            currentNote.setDate(date);

        }


        title = txtTitle.getText().toString();
        currentNote.setTitle(title);
        details = txtDetails.getText().toString();
        if(details.length()>0){
            currentNote.setNoteString(details);
        }
        important = toggleButton.isChecked();
        currentNote.setImportant(important);


        /*if (time == null) {

            if (date == null) {
               // return new Note(title, details, important);
            } else {
                return new Note(title, details, date, important);
            }
        }else{
            return new Note(title,details,date,time,important);
        }*/

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

            // code where data is processing
            //progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress.show();
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void createTimePicker() {
        CalendarForToDo cftd = new CalendarForToDo();
        cftd.createTimePickerDialog(requireActivity(), txtTime);

        //startActivity(new Intent(this, addNotes.class));
    }

    public void createDatePicker() {
        CalendarForToDo cftd = new CalendarForToDo();
        cftd.createDatePickerDialog(requireActivity(), txtDate);

        //startActivity(new Intent(this, addNotes.class));
    }


    private List<Label> setUpReportSelection() {
        spinnerForLabels = (MultiSelectionSpinner) root.findViewById(R.id.spinnerLabel);

        //ArrayList<Label> selectedLabels = new ArrayList<>();
        ArrayList<Label> arr = new ArrayList<>();
        for (Label l : DrawerMenuActivity.listOfLabels) {
            arr.add(l);
        }

        spinnerForLabels.setItems(arr);

        ArrayList<Label> selectedItems = spinnerForLabels.getSelectedItems();
        spinnerForLabels.setSelection(selectedItems);

        //AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
        //alert.setTitle(label.getName());

        //ArrayAdapter adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, arr);
        //spinnerForLabels.setAdapter(adapter);

        /*spinnerForLabels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Label label = (Label) adapterView.getItemAtPosition(i);

                AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
                alert.setTitle(label.getName());

                /*switch(spinnerItem.getType()){
                    case NONE:
                        break;
                    case SUM_GAME_PRICES:
                        alert.setMessage(SteamGameAppConstants.ALL_PRICES_SUM+sb.sumGamePrices()).show();
                        break;
                    case AVERAGE_GAME_PRICES:
                        alert.setMessage(SteamGameAppConstants.ALL_PRICES_AVERAGE+sb.averageGamePrice()).show();
                        break;
                    case UNIQUE_GAMES:
                        alert.setMessage(SteamGameAppConstants.UNIQUE_GAMES_COUNT+sb.getUniqueGames().size()).show();
                        break;
                    case MOST_EXPENSIVE_GAMES:
                        int topN = 3;
                        if(sb.getGames().size() == 0){
                            alert.setMessage("There is no Game!");
                        }
                        if(sb.getGames().size() == 2){
                            topN = 2;
                        }else if(sb.getGames().size() == 1){
                            topN = 1;
                        }
                        List<Game> topGames = sb.selectTopNGamesDependingOnPrice(topN);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(SteamGameAppConstants.MOST_EXPENSIVE_GAMES +"\n"+topGames.get(0));
                        if(topN == 2){
                            stringBuilder.append("\n"+topGames.get(1));
                        } else if(topN == 3){
                            stringBuilder.append("\n"+topGames.get(2));
                        }

                        alert.setMessage(stringBuilder.toString()).show();
                        break;
                    default:
                        break;

                }}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


        return spinnerForLabels.getSelectedItems();
    }

}