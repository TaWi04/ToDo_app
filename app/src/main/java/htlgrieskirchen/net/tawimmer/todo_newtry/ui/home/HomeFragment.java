package htlgrieskirchen.net.tawimmer.todo_newtry.ui.home;

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

import java.util.ArrayList;

import htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity;
import htlgrieskirchen.net.tawimmer.todo_newtry.R;
import htlgrieskirchen.net.tawimmer.todo_newtry.RecyclerViewListAdapter;
import htlgrieskirchen.net.tawimmer.todo_newtry.TodoList;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;

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
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        //root = inflater.inflate(R.layout.fragment_showtodolist, container, false);
        //((DrawerMenuActivity)this.getActivity()).allTodoLists;
        //todoList = new TodoList("News", setList(), new Label("Test"));//TODO add Lists CURRENTLY ONLY FOR TEST PURPOSE
        /*TextView textView = root.findViewById(R.id.textView_showToDoList_title);
        textView.setText(todoList.getTitle());*/
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerViewAllLists);
        ArrayList arr = ((DrawerMenuActivity)this.getActivity()).allTodoLists;
        ArrayList arrayList = arr;
        adapter = new RecyclerViewListAdapter(getActivity(),(arrayList));
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
        ;
        progressBar = root.findViewById(R.id.progressBar);
        updateUI();


        return root;
    }



    public void updateUI() {


        if ((((DrawerMenuActivity)this.getActivity()).allTodoLists) != null) {

            adapter = new RecyclerViewListAdapter(activity,((ArrayList)(((DrawerMenuActivity)getActivity()).allTodoLists)));
            recyclerView.setAdapter(adapter);

            /*fab_create.show()
            fab_create.setOnClickListener {
                Dialog_Create(requireActivity(),list).show()
            }*/

        } else {
            recyclerView.setAdapter(null);
        }
    }
}