package htlgrieskirchen.net.tawimmer.todo_newtry.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity;
import htlgrieskirchen.net.tawimmer.todo_newtry.R;
import htlgrieskirchen.net.tawimmer.todo_newtry.RecyclerViewListAdapter;
import htlgrieskirchen.net.tawimmer.todo_newtry.TodoList;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.cloud;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.drawerMenuActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public static TodoList todoList;
    private ProgressBar progressBar;
    static View root;
    public static RecyclerView recyclerView;
    public static RecyclerViewListAdapter adapter;
    private static Activity activity;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        drawerMenuActivity.getSupportActionBar().setTitle("Home");
        Bundle bundle = getArguments();
        if (bundle != null) {
            if ((todoList = (TodoList) bundle.getSerializable("home")) != null) {

            } else {
                todoList = (TodoList) bundle.getSerializable("homeAfterDeletion");
                TodoList todoList = allTodoLists.remove(allTodoLists.size() - 1);
                showSnackbar(todoList);
                updateUI();
            }
        }

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewAllLists);
        ArrayList arr = ((DrawerMenuActivity) this.getActivity()).allTodoLists;
        ArrayList arrayList = arr;
        adapter = new RecyclerViewListAdapter(getActivity(), (arrayList));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        activity = getActivity();

        FloatingActionButton floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab_check);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.action_nav_home_to_nav_addList);
            }
        });

        progressBar = root.findViewById(R.id.progressBar);
        updateUI();

        return root;
    }


    public void updateUI() {
        if ((((DrawerMenuActivity) this.getActivity()).allTodoLists) != null) {

            drawerMenuActivity.getSupportActionBar().setTitle("Home");
            adapter = new RecyclerViewListAdapter(activity, ((ArrayList) (((DrawerMenuActivity) getActivity()).allTodoLists)));
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(null);
        }
    }

    @SuppressLint("WrongConstant")
    private void showSnackbar(TodoList todoList) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(android.R.id.content), "Do you want to reverse your changes?", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar1 = Snackbar.make(getActivity().findViewById(android.R.id.content), "Message is restored!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
                snackbar.show();
                if(DrawerMenuActivity.getCloudSaving() && DrawerMenuActivity.internetConnectionHandler.isNetworkAvailable(getActivity())){
                    cloud.addList(todoList);
                }
                allTodoLists.add(todoList);
                updateUI();
            }
        });

        snackbar.show();
    }

}