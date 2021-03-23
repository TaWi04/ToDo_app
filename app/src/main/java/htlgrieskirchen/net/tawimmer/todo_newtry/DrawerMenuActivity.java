package htlgrieskirchen.net.tawimmer.todo_newtry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ExpandableListView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import htlgrieskirchen.net.tawimmer.todo_newtry.ui.addnote.AddNoteFragment;
import htlgrieskirchen.net.tawimmer.todo_newtry.ui.showtodolist.ShowTodoListFragment;

public class DrawerMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout root;

    private AppBarConfiguration mAppBarConfiguration;
    public static List<Label> listOfLabels; //load Labels
    EditText txtDate, txtTime;
    public int colorForDatesOverDueDate;
    public int colorForDates;
    public static NavController navController;
    public static int currentListIndex;
    public static ArrayList<TodoList> allTodoLists;
    public  static Activity activity;

    //TODO is new
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);

        root = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //TODO is new
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        //NavigationView navigationView = findViewById(R.id.nav_view);

        RecyclerView recyclerView = findViewById(R.id.nav_view);
        expandableListView =  recyclerView.findViewById(R.id.expandableListView);

        currentListIndex = -1;
        loadLists();
        prepareMenuData();
        populateExpandableList();
        activity = this;



        listOfLabels = new ArrayList<>();
        listOfLabels.add(new Label("Tamara<3"));
        txtDate = (EditText) findViewById(R.id.editTextDate);
        txtTime = (EditText) findViewById(R.id.editTextTime);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_addNote, R.id.nav_slideshow, R.id.nav_showTodoList, R.id.nav_addList)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);

        //TODO
        //navigationView.setNavigationItemSelectedListener(this);

    }

    public static NavController getNavController() {
        return navController;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void loadLists() {//TODO
        allTodoLists = new ArrayList<>();
        ArrayList<Note> arr = new ArrayList();
        ArrayList<Note> arr_arr = new ArrayList();

        Note note = new Note("temp1", "test", true);
        arr_arr.add(note);
        note = new Note("temp2", "test2", false);
        arr_arr.add(note);
        note = new Note("temp3", "", false);
        arr_arr.add(note);
        //arr.add(new TodoList("Temp",new ArrayList()))
        //arr.add(new TodoList("Test",arr_arr,null));
        allTodoLists.add(new TodoList("Test", arr_arr, null));

        note = new Note("temp1", "test", true);
        arr.add(note);
        note = new Note("temp2", "test2", false);
        arr.add(note);
        note = new Note("temp3", "", false);
        arr.add(note);
        allTodoLists.add(new TodoList("Test2", arr_arr, null));

        if (allTodoLists.size() != 0) {//TODO
            currentListIndex = allTodoLists.size() - 1;
        }

    }

    public static void hideKeyboard(Activity act, EditText et) {
        Context c = act.getBaseContext();
        View v = et.findFocus();
        if (v == null)
            return;
        InputMethodManager inputManager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nave_lists) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_help) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*public void openCalendar(android.view.View view){
        CalendarForToDo cftd = new CalendarForToDo();
        AddNoteFragment adf = new AddNoteFragment();
        adf.openCalendar(view);
        cftd.createDatePickerDialog(new AddNoteFragment(),view,txtTime,txtDate);

        //startActivity(new Intent(this, addNotes.class));
    }*/


    private void prepareMenuData() {

        /*MenuModel menuModel = new MenuModel("Lists", true, true);
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }*/

        MenuModel menuModel = new MenuModel("Lists", true, true);

        headerList.add(menuModel);


        List<MenuModel> childModelsList = new ArrayList<>();
        for (TodoList list:allTodoLists) {
            MenuModel childModel = new MenuModel(list.getTitle(), false, false);
            childModel.setList(list);
            childModelsList.add(childModel);
        }
       /* MenuModel childModel = new MenuModel("Core Java Tutorial", false, false, "https://www.journaldev.com/7153/core-java-tutorial");
        childModel.setList();
        childModelsList.add(childModel);

        childModel = new MenuModel("Java FileInputStream", false, false, "https://www.journaldev.com/19187/java-fileinputstream");
        childModelsList.add(childModel);

        childModel = new MenuModel("Java FileReader", false, false, "https://www.journaldev.com/19115/java-filereader");
        childModelsList.add(childModel);
*/

        if (menuModel.hasChildren) {
            // Log.d("API123", "here");
            childList.put(menuModel, childModelsList);
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new htlgrieskirchen.net.tawimmer.todo_newtry.ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        //TODO open Fragment
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.getList()!= null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ShowToDoList",model.getList());
                        Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.action_nav_home_to_showTodoListFragment,bundle);

                       onBackPressed();
                    }
                }

                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}