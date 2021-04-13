package htlgrieskirchen.net.tawimmer.todo_newtry.ui.showtodolist;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShowTodoListModel extends ViewModel {

    private MutableLiveData<EditText> editTextTime;

    private MutableLiveData<String> editTextDate;
    private MutableLiveData<String> mText;

    public ShowTodoListModel() {

        editTextTime = new MutableLiveData<>();
        editTextDate = new MutableLiveData<>();
        //editText.setValue(EditText.);
    }

    public LiveData<String> getText() {
        return mText;
    }
}