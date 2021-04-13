package htlgrieskirchen.net.tawimmer.todo_newtry;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;


public class MultiSelectionSpinner extends AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener {

    ArrayList<Label> labels ;
    boolean[] selection ;
    ArrayAdapter adapter;

    public MultiSelectionSpinner(Context context) {
        super(context);

        labels = new ArrayList<>();
        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        labels = new ArrayList<>();
        selection = new boolean[labels.size()];
        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (selection != null && which < selection.length) {
            selection[which] = isChecked;
            labels.get(which).setChecked(isChecked);
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
        builder.setMultiChoiceItems(labelsNames, this.selection, this);
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
                        Label label = new Label(input.getText().toString());
                        labels.add(label);
                        DrawerMenuActivity.listOfLabels.add(label);
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
        boolean[] temp =  new boolean[this.labels.size()];
        for (int i = 0; i < temp.length; i++) {
            if(i >= selection.length){
                temp[i]=false;
            }else{

                temp[i] = selection[i];
            }

        }
        this.selection = new boolean[temp.length];
        selection = temp;

        adapter.clear();
        adapter.addAll(labels);
    }

    public void setSelection(ArrayList<Label> selection) {
        for (int i = 0; i < this.selection.length; i++) {
            this.selection[i] = false;
            labels.get(i).setChecked(false);
        }

        for (Label sel : selection) {
            for (int j = 0; j < labels.size(); ++j) {
                if (labels.get(j).getValue().equals(sel.getValue())) {
                    this.selection[j] = true;
                    labels.get(j).setChecked(true);
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

    public ArrayList<Label> getLabels(){
        return labels;
    }
}