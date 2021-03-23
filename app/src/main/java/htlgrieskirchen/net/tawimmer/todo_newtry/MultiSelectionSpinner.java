package htlgrieskirchen.net.tawimmer.todo_newtry;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;

//import com.woolha.example.models.Item;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.Arrays;


public class MultiSelectionSpinner extends AppCompatSpinner implements//android.support.v7.widget.AppCompatSpinner
        DialogInterface.OnMultiChoiceClickListener {
    // Activity activity;
    ArrayList<Label> labels = null;
    boolean[] selection = null;
    ArrayAdapter adapter;

    public MultiSelectionSpinner(Context context) {
        super(context);
        //this.activity = activity;

        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (selection != null && which < selection.length) {
            selection[which] = isChecked;

            adapter.clear();
            adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] labelsNames = new String[labels.size()];

        for (int i = 0; i < labels.size(); i++) {
            labelsNames[i] = labels.get(i).getName();
        }
        builder.setTitle("Labels:");
        builder.setMultiChoiceItems(labelsNames, selection, this);
        builder.setNeutralButton("Add Label", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setId(R.id.dialog_label_name_field);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext()).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Do nothing
                    }
                });
                alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //EditText editText = findViewById(R.id.dialog_label_name_field);
                        Label label = new Label(input.getText().toString());
                        labels.add(label);
                        setItems(labels);
                        ArrayList arr = getSelectedItems();
                        arr.add(label);
                        setSelection(arr);
                    }
                });
                alertDialog.setView(input);
                alertDialog.setTitle("Create new Label");
                alertDialog.show();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // Do nothing
            }
        });

        builder.show();

        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(ArrayList<Label> labels) {
        this.labels = labels;
        selection = new boolean[this.labels.size()];
        adapter.clear();
        //adapter.add(new Label(""));
        adapter.addAll(labels);
        Arrays.fill(selection, false);
    }

    public void setSelection(ArrayList<Label> selection) {
        for (int i = 0; i < this.selection.length; i++) {
            this.selection[i] = false;
        }

        for (Label sel : selection) {
            for (int j = 0; j < labels.size(); ++j) {
                if (labels.get(j).getValue().equals(sel.getValue())) {
                    this.selection[j] = true;
                }
            }
        }

        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < labels.size(); ++i) {
            if (selection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }

                foundOne = true;

                sb.append(labels.get(i).getName());
            }
        }

        return sb.toString();
    }

    public ArrayList<Label> getSelectedItems() {
        ArrayList<Label> selectedItems = new ArrayList<>();

        for (int i = 0; i < labels.size(); ++i) {
            if (selection[i]) {
                selectedItems.add(labels.get(i));
            }
        }

        return selectedItems;
    }
}