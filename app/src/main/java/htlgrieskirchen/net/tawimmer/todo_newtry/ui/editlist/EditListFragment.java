package htlgrieskirchen.net.tawimmer.todo_newtry.ui.editlist;

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
import htlgrieskirchen.net.tawimmer.todo_newtry.ui.addlist.AddListViewModel;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.cloud;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.drawerMenuActivity;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.hideKeyboard;

public class EditListFragment extends Fragment {

    private AddListViewModel editListViewModel;
    static EditText txtTitle;
    private ProgressBar progressBar;
    static View root;
    private MultiSelectionSpinner spinnerForLabels;
    private TodoList todoList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        todoList = (TodoList) bundle.getSerializable("List");
        editListViewModel =
                new ViewModelProvider(this).get(AddListViewModel.class);
        root = inflater.inflate(R.layout.fragment_editlist, container, false);
        drawerMenuActivity.getSupportActionBar().setTitle(todoList.getTitle());

        FloatingActionButton floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_check);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editList();
            }
        });

        FloatingActionButton floatingActionButtonDelete = (FloatingActionButton) root.findViewById(R.id.fab_delete);
        floatingActionButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteList();
            }
        });

        txtTitle = (EditText) root.findViewById(R.id.editTextTitle);
        txtTitle.setText(todoList.getTitle());
        spinnerForLabels = (MultiSelectionSpinner) root.findViewById(R.id.spinnerLabel);
        //spinnerForLabels.set; //TODO
        if (todoList.getLabel() != null) {
            if (todoList.getLabel().size() != 0) {
                ArrayList<Label> arr = new ArrayList<>();
                for (Label l : DrawerMenuActivity.listOfLabels) {
                    arr.add(l);
                }

                spinnerForLabels.setItems(arr);
                spinnerForLabels.setSelection(todoList.getLabel());
            } else {
                setUpReportSelection();
            }
        } else {
            setUpReportSelection();
        }

        progressBar = root.findViewById(R.id.progressBar);
        return root;
    }

    public void editList() {

        txtTitle = (EditText) root.findViewById(R.id.editTextTitle);
        if (txtTitle.length() <= 0) {
            txtTitle.setError("Must at least have 1 characters!");
            return;
        }
        spinnerForLabels = (MultiSelectionSpinner) root.findViewById(R.id.spinnerLabel);
        // spinnerForLabels.setSelection(todoList.getLabel());

        updateList(txtTitle, spinnerForLabels);
        if(DrawerMenuActivity.getCloudSaving() && DrawerMenuActivity.internetConnectionHandler.isNetworkAvailable(getActivity())){
            cloud.editList(todoList);
        }
        progressBar.setVisibility(View.VISIBLE);

        new YourAsyncTask().execute();
        hideKeyboard(getActivity(), txtTitle);
        Bundle bundle = new Bundle();
        bundle.putSerializable("home", todoList);
        Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editListFragment_to_nav_home, bundle);
    }

    public void deleteList() {

        allTodoLists.remove(todoList);
        if(DrawerMenuActivity.getCloudSaving() && DrawerMenuActivity.internetConnectionHandler.isNetworkAvailable(getActivity())){
            cloud.deleteList(todoList);
        }
        allTodoLists.add(todoList); //on last index
        new EditListFragment.YourAsyncTask().execute();
        hideKeyboard(getActivity(), txtTitle);
        Bundle bundle = new Bundle();
        bundle.putSerializable("homeAfterDeletion", todoList);
        Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editListFragment_to_nav_home, bundle);
    }

    private void updateList(EditText txtTitle, MultiSelectionSpinner spinner) {
        String title;

        todoList.setLabel(spinner.getSelectedItems());
        title = txtTitle.getText().toString();
        todoList.setTitle(title);
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
