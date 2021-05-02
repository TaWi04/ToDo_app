package htlgrieskirchen.net.tawimmer.todo_newtry;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawerMenuActivity extends AppCompatActivity {

    public static DrawerLayout root;
    //prefs
    private static String signature;
    private static boolean nightMode;
    private static boolean hideDone;
    private static boolean cloudSaving;
    private static boolean saveAutomatically;

    //TODO for prefs
    public int colorForDatesOverDueDate;
    public int colorForDates;

    public static List<Label> listOfLabels;
    EditText txtDate, txtTime;
    private AppBarConfiguration mAppBarConfiguration;
    public static NavController navController;
    public static int currentListIndex;
    public static ArrayList<TodoList> allTodoLists;
    public static SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    public static DrawerMenuActivity drawerMenuActivity;
    private boolean permissionWriting;
    private boolean permissionReading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            checkPermissions();
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener = (sharedPrefs, key) -> preferenceChanged(sharedPrefs, key);
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        root = findViewById(R.id.drawer_layout);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setPrefs();
        drawerMenuActivity = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("");
        currentListIndex = -1;
        setHideDone(getHideDone());

        allTodoLists = new ArrayList<>();
        listOfLabels = new ArrayList<>();
        listOfLabels.add(new Label("Tamara<3"));
        listOfLabels.add(new Label("Tamara"));
        listOfLabels.add(new Label("Testt"));

        loadLists();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        txtDate = (EditText) findViewById(R.id.editTextDate);
        txtTime = (EditText) findViewById(R.id.editTextTime);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_showTodoList, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        //getActionBar().setTitle("Hello world App");
        //getSupportActionBar().setTitle("Hello world App");

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public static NavController getNavController() {
        return navController;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return (super.onCreateOptionsMenu(menu));//true
    }

    private final static int RQ_PREFERENCES = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this,
                    SettingActivity.class);
            startActivityForResult(intent, RQ_PREFERENCES);

        } else if(item.getItemId() == R.id.action_save){
            saveLists();
            Toast.makeText(this, "Your Lists have been saved", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private static final int RQ_WRITE_STORAGE = 2;
    private static final int RQ_READ_STORAGE = 3;

    private void checkPermissions(){
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RQ_WRITE_STORAGE);
        } else {
            permissionWriting = true;
        }
        if (checkSelfPermission (Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RQ_READ_STORAGE);
        } else {
            permissionReading = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==RQ_WRITE_STORAGE) {
            permissionWriting = grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (requestCode==RQ_READ_STORAGE) {
            permissionReading = grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }


   // @RequiresApi(Build.VERSION_CODES.Q)
    @TargetApi(Build.VERSION_CODES.Q)
    private String getPath() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return getExternalFilesDir(null).getAbsolutePath();
        }
    }

    public void saveLists(){
        File file;
        try {

            PrintWriter outPrintWriter;
            String state = Environment.getExternalStorageState();
            if (! state . equals(Environment.MEDIA_MOUNTED)) return;
            if(permissionReading && permissionWriting){
                String folder = getPath();
                        file = new File(folder + File.separator + "lists.json");
                outPrintWriter= new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            }else{
                file = new File("lists.json");
                OutputStream outputStream = null;
                try {
                    outputStream = openFileOutput(file.getName(), Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                outPrintWriter = new PrintWriter(new OutputStreamWriter(outputStream));
            }

            Gson gson = new Gson();

            String json =  gson.toJson(allTodoLists);
            System.out.println(allTodoLists);
            outPrintWriter.println(json);
            outPrintWriter.flush();
            outPrintWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLists();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveLists();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveLists();
    }


    private void loadLists(){
        File file;
        try {
            String state = Environment.getExternalStorageState();
            if (! state . equals(Environment.MEDIA_MOUNTED)) return;
            FileInputStream fileInputStream;
            if(permissionReading && permissionWriting){
                String folder =  getPath();
                file = new File(folder + File.separator + "lists.json");
                fileInputStream = new FileInputStream(file);
            }else{
                file = new File("lists.json");
                fileInputStream = openFileInput(file.getName());
            }
            allTodoLists.clear();
            try(BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(fileInputStream))){
                String s = bufferedInputStream.readLine();
                Gson gson = new Gson();
                allTodoLists.addAll(gson.fromJson(s,new TypeToken<List<TodoList>>(){}.getType()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (allTodoLists.size() != 0) {
            currentListIndex = allTodoLists.size() - 1;
        }
    }


    /*private void loadLists() {//TODO
        allTodoLists = new ArrayList<>();
        ArrayList<TodoList> arr = new ArrayList();
        ArrayList<Note> arr_arr = new ArrayList();
        ArrayList<Label> labels = new ArrayList();
        labels.add(listOfLabels.get(1));
        labels.add(listOfLabels.get(2));


        Note note = new Note("temp1", "test", true);
        arr_arr.add(note);
        note = new Note("temp2", "test2", false);
        arr_arr.add(note);
        note = new Note("temp3", "", false);
        arr_arr.add(note);
        //arr.add(new TodoList("Temp",new ArrayList()))
        //arr.add(new TodoList("Test",arr_arr,null));


        allTodoLists.add(new TodoList("Test", arr_arr, labels));
        if (allTodoLists.size() != 0) {
            currentListIndex = allTodoLists.size() - 1;
        }

    }*/

    public static void hideKeyboard(Activity act, EditText et) {
        Context c = act.getBaseContext();
        View v = et.findFocus();
        if (v == null)
            return;
        InputMethodManager inputManager = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setPrefs() {
        //TODO USERNAME + PASSWORD

            /*case "username":
                String newUsername = newValue.toString();
                boolean isValid = newUsername.length() > 2;
                Toast.makeText(getActivity(), "The username has to contain at least 2 characters", Toast.LENGTH_LONG).show();
                return isValid ;
            case "password":
                String newPassword = newValue.toString();
                boolean isValidPw = newPassword.length() > 3;
                Toast.makeText(getActivity(), "The password has to contain at least 3 characters", Toast.LENGTH_LONG).show();
                return isValidPw ;*/


        String signatur = prefs.getString("signature", "");
        if (signatur.length() < 1) {
            Toast.makeText(this, " The signature has to contain at least 1 character", Toast.LENGTH_LONG).show();
        } else {
            DrawerMenuActivity.setSignature(signatur);
        }
        boolean hideDone = prefs.getBoolean("hideDone", false);
        DrawerMenuActivity.setHideDone(hideDone);
        boolean darkMode = prefs.getBoolean("darkActivate", false);
        DrawerMenuActivity.setNightMode(darkMode);
          boolean sync = prefs.getBoolean("sync", false);
        DrawerMenuActivity.setCloudSaving(sync);
        boolean savingAutomatically = prefs.getBoolean("savingAutomatically", false);
        DrawerMenuActivity.setSaveAutomatically(savingAutomatically);

    }



    private void preferenceChanged(SharedPreferences sharedPrefs , String key) {//NOT NEEDED
        Map<String, ?> allEntries = sharedPrefs. getAll () ;
        String sValue = "";
        if ( allEntries . get(key) instanceof String )
            sValue = sharedPrefs. getString (key, "");
        else if ( allEntries . get(key) instanceof Boolean)
            sValue = String.valueOf( sharedPrefs .getBoolean(key, false ));
    }

    private static void changeTheme(boolean night){
        if(night) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void setSignature(String pSignature) {
        signature = pSignature;
    }

    public static void setNightMode(boolean nightMode) {
        DrawerMenuActivity.nightMode = nightMode;
        changeTheme(nightMode);
    }

    public static void setHideDone(boolean hideDone) {
        DrawerMenuActivity.hideDone = hideDone;
        if(hideDone && RecyclerViewAdapter.dropdownLayout != null){
            RecyclerViewAdapter.dropdownLayout.setVisibility(View.GONE);
        }else if(!hideDone && RecyclerViewAdapter.dropdownLayout != null){
            RecyclerViewAdapter.dropdownLayout.setVisibility(View.VISIBLE);
        }
    }
    public static boolean getHideDone(){
        return hideDone;
    }
    public static void setCloudSaving(boolean cloudSaving) {
        DrawerMenuActivity.cloudSaving = cloudSaving;
    }

    public static void setSaveAutomatically(boolean saveAutomatically) {
        DrawerMenuActivity.saveAutomatically = saveAutomatically;
    }

}