package htlgrieskirchen.net.tawimmer.todo_newtry.ui.addlist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity;
import htlgrieskirchen.net.tawimmer.todo_newtry.Label;
import htlgrieskirchen.net.tawimmer.todo_newtry.MultiSelectionSpinner;
import htlgrieskirchen.net.tawimmer.todo_newtry.R;
import htlgrieskirchen.net.tawimmer.todo_newtry.TodoList;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.hideKeyboard;

public class AddListFragment extends Fragment {

    private AddListViewModel addListViewModel;
    static EditText txtTitle;
    private ProgressBar progressBar;
    static View root;
    private MultiSelectionSpinner spinnerForLabels;
    private TodoList todoList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addListViewModel =
                new ViewModelProvider(this).get(AddListViewModel.class);
        root = inflater.inflate(R.layout.fragment_addlist, container, false);

        FloatingActionButton floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_check);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addList();
            }
        });

        setUpReportSelection();
        progressBar = root.findViewById(R.id.progressBar);
        return root;
    }

    public void addList() {

        txtTitle = (EditText) root.findViewById(R.id.editTextTitle);
        if (txtTitle.length() <= 0) {
            txtTitle.setError("Must at least have 1 characters!");
            return;
        }
        todoList = new TodoList(txtTitle.getText().toString(), new ArrayList<>(), spinnerForLabels.getSelectedItems());
        allTodoLists.add(todoList);
        progressBar.setVisibility(View.VISIBLE);

        new YourAsyncTask().execute();
        hideKeyboard(getActivity(), txtTitle);
        Bundle bundle = new Bundle();
        bundle.putSerializable("home", todoList);
        Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_addList_to_nav_home, bundle);
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

    private List<Label> setUpReportSelection() {
        spinnerForLabels = (MultiSelectionSpinner) root.findViewById(R.id.spinnerLabel);

        ArrayList<Label> arr = new ArrayList<>();
        for (Label l : DrawerMenuActivity.listOfLabels) {
            arr.add(l);
        }

        spinnerForLabels.setItems(arr);

        ArrayList<Label> selectedItems = spinnerForLabels.getSelectedItems();
        spinnerForLabels.setSelection(selectedItems);
        return spinnerForLabels.getSelectedItems();
    }

}