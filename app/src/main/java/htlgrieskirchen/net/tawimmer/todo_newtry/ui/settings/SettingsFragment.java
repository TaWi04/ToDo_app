package htlgrieskirchen.net.tawimmer.todo_newtry.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity;
import htlgrieskirchen.net.tawimmer.todo_newtry.R;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.drawerMenuActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences,
                rootKey);

        sharedPreferences = DrawerMenuActivity.prefs;
        setOnPreferenceChangeListenerOnPreferences();
        drawerMenuActivity.getSupportActionBar().setTitle("Settings");

    }

    private void setOnPreferenceChangeListenerOnPreferences() {
        sharedPreferences.getAll()
                .entrySet()
                .stream()
                .forEach(entry -> {
                    Preference pref = findPreference(entry.getKey());
                    pref.setOnPreferenceChangeListener((preference, newValue) -> {

                        switch (preference.getKey()) {
                            case "username":
                                String newUsername = newValue.toString();
                                boolean isValid = newUsername.length() > 2;
                                Toast.makeText(getActivity(), "The username has to contain at least 2 characters", Toast.LENGTH_LONG).show();
                                return isValid;
                            case "password":
                                String newPassword = newValue.toString();
                                boolean isValidPw = newPassword.length() > 3;
                                Toast.makeText(getActivity(), "The password has to contain at least 3 characters", Toast.LENGTH_LONG).show();
                                return isValidPw;
                            case "signature":
                                String newSignature = newValue.toString();
                                if (newSignature.length() < 1) {
                                    Toast.makeText(getActivity(), " The signature has to contain at least 1 character", Toast.LENGTH_LONG).show();
                                } else {
                                    DrawerMenuActivity.setSignature(newSignature);
                                }
                                break;
                            case "hideDone":
                                DrawerMenuActivity.setHideDone((boolean) newValue);
                                break;
                            case "darkActivate":
                                DrawerMenuActivity.setNightMode((boolean) newValue);
                                break;
                            case "sync":
                                DrawerMenuActivity.setCloudSaving((boolean) newValue);
                                break;
                            case "savingAutomatically":
                                DrawerMenuActivity.setSaveAutomatically((boolean) newValue);
                                break;
                        }
                        return true;
                    });
                });


    }
}
