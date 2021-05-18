package htlgrieskirchen.net.tawimmer.todo_newtry.ui.showtodolist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity;
import htlgrieskirchen.net.tawimmer.todo_newtry.Label;
import htlgrieskirchen.net.tawimmer.todo_newtry.Note;
import htlgrieskirchen.net.tawimmer.todo_newtry.R;
import htlgrieskirchen.net.tawimmer.todo_newtry.RecyclerViewAdapter;
import htlgrieskirchen.net.tawimmer.todo_newtry.TodoList;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.cloud;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.currentListIndex;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.drawerMenuActivity;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.getHideDone;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.root;

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
        if((todoList = (TodoList) bundle.getSerializable("ShowToDoList"))!=null){

        }else{
            todoList = (TodoList) bundle.getSerializable("ShowToDoListAfterDeletion");
            Note note = todoList.removeNoteFromList(todoList.removeNoteFromList(todoList.getNotes().get(todoList.getNotes().size()-1)));

            showSnackbar(note);
            updateUI();
        }

        root = inflater.inflate(R.layout.fragment_showtodolist, container, false);

        drawerMenuActivity.getSupportActionBar().setTitle(todoList.getTitle());
        TextView textView = root.findViewById(R.id.textView_showToDoList_title);
        textView.setText(todoList.getTitle());
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(getActivity(),todoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        DrawerMenuActivity.setHideDone(getHideDone());
        activity = getActivity();


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

        } else {
            recyclerView.setAdapter(null);
        }
    }

    @SuppressLint("WrongConstant")
    private void showSnackbar(Note note){
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(android.R.id.content), "Do you want to reverse your changes?", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar1 = Snackbar.make(getActivity().findViewById(android.R.id.content), "Message is restored!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
                snackbar.show();
                if(note.isChecked()){
                    if(DrawerMenuActivity.getCloudSaving() && DrawerMenuActivity.internetConnectionHandler.isNetworkAvailable(getActivity())){
                        note.setChecked(true);
                        cloud.addNote(todoList,note);
                    }
                    todoList.addToList(note);
                    todoList.checkNote(todoList.getNotes().indexOf(note));
                }else{
                    if(DrawerMenuActivity.getCloudSaving() && DrawerMenuActivity.internetConnectionHandler.isNetworkAvailable(getActivity())){
                        cloud.addNote(todoList,note);
                    }
                    todoList.addToList(note);
                }
                updateUI();
            }
        });

        snackbar.show();
    }
}