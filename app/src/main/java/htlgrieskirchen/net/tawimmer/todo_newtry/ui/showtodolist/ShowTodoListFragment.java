package htlgrieskirchen.net.tawimmer.todo_newtry.ui.showtodolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import htlgrieskirchen.net.tawimmer.todo_newtry.Label;
import htlgrieskirchen.net.tawimmer.todo_newtry.Note;
import htlgrieskirchen.net.tawimmer.todo_newtry.R;
import htlgrieskirchen.net.tawimmer.todo_newtry.RecyclerViewAdapter;
import htlgrieskirchen.net.tawimmer.todo_newtry.TodoList;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.currentListIndex;

public class ShowTodoListFragment extends Fragment {

    private ShowTodoListModel showTodoListModel;
    public static TodoList todoList;
    // static EditText  txtDate, txtTime;
    // private int mYear, mMonth, mDay, mHour, mMinute;
    private ProgressBar progressBar;
    static View root;
    public static RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;
    private static Activity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        showTodoListModel =
                new ViewModelProvider(this).get(ShowTodoListModel.class);

        Bundle bundle = getArguments();
        todoList = (TodoList) bundle.getSerializable("ShowToDoList");
        root = inflater.inflate(R.layout.fragment_showtodolist, container, false);
        if(todoList == null){
            todoList = new TodoList("News", setList(), new ArrayList<>());//TODO add Lists CURRENTLY ONLY FOR TEST PURPOSE

        }
       TextView textView = root.findViewById(R.id.textView_showToDoList_title);
        textView.setText(todoList.getTitle());
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(getActivity(),todoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        activity = getActivity();
        //currentListIndex;
        //allTodoLists;


        FloatingActionButton floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_check);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.action_nav_showTodoList_to_nav_addNote);
            }
        });

        progressBar = root.findViewById(R.id.progressBar);
        updateUI();
        return root;
    }

    public ArrayList<Note> setList() {
        ArrayList<Note> arr = new ArrayList<>();
        arr.add(new Note("TEST", "test", true));
        arr.add(new Note("TEST2", "test2", true));
        return arr;
    }

    public static void updateUI() {


        if (todoList != null) {
            currentListIndex = allTodoLists.indexOf(todoList);
            adapter = new RecyclerViewAdapter(activity,todoList);
            recyclerView.setAdapter(adapter);

            /*fab_create.show()
            fab_create.setOnClickListener {
                Dialog_Create(requireActivity(),list).show()
            }*/

        } else {
            recyclerView.setAdapter(null);
        }
    }

    /*public void addNote(){

        //progressBar.onVisibilityAggregated(true);
        progressBar.setVisibility(View.VISIBLE);

        //startActivity(new Intent(this, addNotes.class));

        new YourAsyncTask().execute();
    }
    private class YourAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {



            synchronized (this){
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
*/
   /* public void createTimePicker(){
        CalendarForToDo cftd = new CalendarForToDo();
        cftd.createTimePickerDialog(requireActivity(),txtTime);

        //startActivity(new Intent(this, addNotes.class));
    }
    public void createDatePicker(){
        CalendarForToDo cftd = new CalendarForToDo();
        cftd.createDatePickerDialog(requireActivity(),txtDate);

        //startActivity(new Intent(this, addNotes.class));
    }*/



   /* private List<Label> setUpReportSelection() {
        spinnerForLabels = (MultiSelectionSpinner) root.findViewById(R.id.spinnerLabel);

        //ArrayList<Label> selectedLabels = new ArrayList<>();
        ArrayList<Label> arr = new ArrayList<>();
        for (Label l: DrawerMenuActivity.listOfLabels) {
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
        });


        return spinnerForLabels.getSelectedItems();
    }*/

}