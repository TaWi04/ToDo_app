package htlgrieskirchen.net.tawimmer.todo_newtry;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewListAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private ArrayList<TodoList> dataSet;
    ViewGroup parent;

    public RecyclerViewListAdapter(Activity activity, ArrayList<TodoList> dataSet) {
        this.dataSet = dataSet;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        return new ViewHolderToDoList(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_todolists, parent, false), parent);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TodoList todoList = getItem(position);
        
        ViewHolderToDoList viewHolderToDoList =  new ViewHolderToDoList(holder.itemView,parent);
            viewHolderToDoList.bind(todoList);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private TodoList getItem(int adapterPosition) {
        TodoList todoList = dataSet.get(adapterPosition);
        return todoList;
    }

    public class ViewHolderToDoList extends RecyclerView.ViewHolder {

        private TodoList todoList;
        private TextView title;
        private ImageButton changeList;
        private ViewGroup parent;

        public ViewHolderToDoList(View view, ViewGroup parent) {
            super(view);
            this.parent = parent;
            title = view.findViewById(R.id.view_list_title);
            changeList = view.findViewById(R.id.view_todolists_imageButton);
        }


        public void bind(TodoList todoList) {
            this.todoList = todoList;
            setChangeNoteOnClick();
            title.setText(todoList.getTitle());
            bindOnTitleClick();
        }

        private void bindOnTitleClick() { //TODO maybe onClick for symbol too?
            title.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ShowToDoList", todoList);
                ((DrawerMenuActivity)activity).currentListIndex = dataSet.indexOf(todoList);
                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.action_nav_home_to_showTodoListFragment,bundle);
                }
            );}


        private void setChangeNoteOnClick() {
            changeList.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("List", todoList);
                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.action_nav_home_to_editListFragment, bundle);
            });
        }
    }}

