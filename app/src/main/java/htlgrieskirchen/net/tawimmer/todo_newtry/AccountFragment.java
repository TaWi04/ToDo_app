package htlgrieskirchen.net.tawimmer.todo_newtry;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import htlgrieskirchen.net.tawimmer.todo_newtry.Cloud.InternetConnectionHandler;
import htlgrieskirchen.net.tawimmer.todo_newtry.Cloud.User;
import htlgrieskirchen.net.tawimmer.todo_newtry.ui.addlist.AddListViewModel;

import static android.app.Activity.RESULT_OK;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.allTodoLists;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.currentUser;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.drawerMenuActivity;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.hideKeyboard;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.internetConnectionHandler;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.setCurrentUser;

public class AccountFragment extends Fragment {

    static EditText txtUsername;
    static EditText txtPassword;
    static EditText txtName;
    static View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_account, container, false);
        drawerMenuActivity.getSupportActionBar().setTitle("Account");
        User user = drawerMenuActivity.getCurrentUser();
//        hideKeyboard(getActivity(), txtUsername);
//        hideKeyboard(getActivity(), txtPassword);
//        hideKeyboard(getActivity(), txtName);
        Button button_login = (Button) root.findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new InternetConnectionHandler().isNetworkAvailable(getActivity())) {
                    login();
                }

            }
        });
        Button button_signin = (Button) root.findViewById(R.id.button_signIn);
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new InternetConnectionHandler().isNetworkAvailable(getActivity())) {
                    signIn();
                }
            }
        });
        txtUsername = root.findViewById(R.id.editTextUsername);
        if (txtUsername.length() <= 0) {
            txtUsername.setError("Must at least have 1 characters!");
        }
        txtPassword = root.findViewById(R.id.editTextPassword);
        if (txtPassword.length() <= 0) {
            txtPassword.setError("Must at least have 1 characters!");
        }
        txtName = root.findViewById(R.id.editTextName);
        if (txtName.length() <= 0) {
            txtName.setError("Must at least have 1 characters!");
        }
        if (user.getUsername().length() >= 1 && user.getPassword().length() >= 1) {
            Button button_register = root.findViewById(R.id.button_signIn);
            button_register.setVisibility(View.GONE);
            Button button_logout = root.findViewById(R.id.button_login);
            button_login.setText("Logout");
            button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

            txtUsername.setClickable(false);
            txtUsername.setFocusable(false);
            txtUsername.setCursorVisible(false);
            txtUsername.setText(user.getUsername());

            txtPassword.setClickable(false);
            txtPassword.setFocusable(false);
            txtPassword.setCursorVisible(false);
            txtPassword.setText(user.getPassword());

            txtName.setClickable(false);
            txtName.setFocusable(false);
            txtName.setCursorVisible(false);
            txtName.setText(user.getName());
        }
        return root;
    }

    public void signIn() {
        User user = new User(txtUsername.getText().toString(), txtPassword.getText().toString());
        user.setName(txtName.getText().toString());
        if (user.getUsername().length() > 0 && user.getPassword().length() > 0) {
            if (user.userLogin(internetConnectionHandler)) {
                Toast.makeText(drawerMenuActivity, "Please log in or create another User!", Toast.LENGTH_LONG).show();
            } else {
                user.userSignIn();
                setCurrentUser(user);
                Toast.makeText(drawerMenuActivity, "New User created!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(drawerMenuActivity, "Please enter a valid Password and Username!", Toast.LENGTH_LONG).show();
        }
        Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_nav_home);

    }

    public void logout() {
        User user = new User("", "");
        setCurrentUser(user);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_nav_home);
        Toast.makeText(drawerMenuActivity, "User has been logged out!", Toast.LENGTH_LONG).show();

    }

    public void login() {
        User user = new User(txtUsername.getText().toString(), txtPassword.getText().toString());
        if (user.userLogin(internetConnectionHandler)) {
            setCurrentUser(user);
            Toast.makeText(drawerMenuActivity, "User logged in!", Toast.LENGTH_LONG).show();
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_accountFragment_to_nav_home);
        } else {
            Toast.makeText(drawerMenuActivity, "Please enter a correct username and password!", Toast.LENGTH_LONG).show();
        }
    }
}