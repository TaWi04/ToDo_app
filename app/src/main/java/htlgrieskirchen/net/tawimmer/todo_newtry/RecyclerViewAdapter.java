package htlgrieskirchen.net.tawimmer.todo_newtry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.cloud;
import static htlgrieskirchen.net.tawimmer.todo_newtry.DrawerMenuActivity.getHideDone;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int DROPDOWN = 1;

    private Activity activity;
    private TodoList dataSet;
    public static LinearLayout dropdownLayout;

    public RecyclerViewAdapter(Activity activity, TodoList dataSet) {
        this.dataSet = dataSet;
        this.activity = activity;
        DrawerMenuActivity.setHideDone(getHideDone());
    }

    private boolean checkedShown = false;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DrawerMenuActivity.setHideDone(getHideDone());
        if (viewType == DROPDOWN) {
            DrawerMenuActivity.setHideDone(getHideDone());
            RecyclerView.ViewHolder viewHolder =  new ViewHolderDropdown(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_dropdown, parent, false));
            DrawerMenuActivity.setHideDone(getHideDone());
            return viewHolder;
        } else {
            return new ViewHolderNote(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_note, parent, false), parent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dataSet.getNotes().size()) {
            return DROPDOWN;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderDropdown) {
            ((ViewHolderDropdown) holder).bind();
        } else {
            ((ViewHolderNote) holder).bind(getItem(position));
        }
    }

    @Override
    public int getItemCount() {
        int count = dataSet.getNotes().size() + 1;
        if (checkedShown) {
            count += dataSet.getCheckedNotes().size();
        }
        return count;
    }

    private Note getItem(int adapterPosition) {
        if (adapterPosition >= dataSet.getNotes().size()) {
            //is checked
            return dataSet.getCheckedNotes().get(adapterPosition - (dataSet.getNotes().size() + 1));
        } else {
            //isn't checked
            return dataSet.getNotes().get(adapterPosition);
        }
    }

    private int getCheckedIndex(int adapterPostition) {
        return adapterPostition - dataSet.getCheckedNotes().size() - 1;
    }


    public class ViewHolderNote extends RecyclerView.ViewHolder {

        private ImageButton check;
        private TextView title;
        private ImageButton changeNote;
        private TextView details;
        private TextView date;
        private ViewGroup parent;

        public ViewHolderNote(View view, ViewGroup parent) {
            super(view);
            this.parent = parent;
            check = view.findViewById(R.id.view_note_checkImageButton);
            title = view.findViewById(R.id.view_list_title);
            changeNote = view.findViewById(R.id.view_note_change_imageButton);
            details = view.findViewById(R.id.view_note_detail);
            date = view.findViewById(R.id.view_note_date);
        }

        @SuppressLint("ResourceAsColor")
        public void bind(Note note) {
            bindCheck(note);
            setChangeNoteOnClick(note);
            setImage(note);

            title.setText(note.getTitle());

            String tempDetails = note.getNoteString();
            if (tempDetails != null && !tempDetails.equals("")) {
                String[] linesWithDetails = tempDetails.split("\n");
                if (linesWithDetails.length <= 2) {
                    details.setText(tempDetails);
                } else {
                    details.setText(linesWithDetails[0] + "\n...");
                }
                date.setVisibility(View.VISIBLE);
            } else {
                details.setText("");
                details.setVisibility(View.GONE);
            }
            //ToDo maybe show time due (bis beendet sein muss)
            String dateString = note.getFormatedDateString("dd.MM.yyyy", "HH:mm");


            if (dateString != null && !note.isChecked()) {
                date.setText("" + dateString);
                //date.setHint(null);
                date.setVisibility(View.VISIBLE);
                if (note.isOver()) {
                    date.setTextColor(activity.getColor(R.color.dateIsOver));
                } else {
                    date.setTextColor(activity.getColor(R.color.black));
                }
            } else {
                date.setText("");
                date.setVisibility(View.GONE);
            }
        }


        private void bindCheck(Note note) {
            check.setOnClickListener(v -> {
                check.setOnClickListener(null);
                if (note.isChecked()) {
                    dataSet.uncheckNote(getAdapterPosition() - (dataSet.getNotes().size() + 1));
                    cloud.editNote(dataSet,note);//TODO NEW
                    setImage(note);


                    String dateString = note.getFormatedDateString("dd.MM.yyyy", "HH:mm");
                    if (dateString != null && !note.isChecked()) {
                        date.setText("" + dateString);
                        date.setVisibility(View.VISIBLE);
                        if (note.isOver()) {
                            date.setTextColor(activity.getColor(R.color.dateIsOver));
                        } else {
                            date.setTextColor(activity.getColor(R.color.black));
                        }
                    } else {
                        date.setText("");
                        date.setVisibility(View.GONE);
                    }
                    notifyItemMoved(getAdapterPosition(), 0);
                    if (dataSet.getCheckedNotes().isEmpty()) {
                        notifyItemRemoved(getItemCount());
                        checkedShown = false;
                    }

                } else {
                    dataSet.checkNote(getAdapterPosition());
                    cloud.editNote(dataSet,note);//TODO NEW
                    setImage(note);

                    if (checkedShown) {
                        notifyItemMoved(getAdapterPosition(), dataSet.getNotes().size() + 1);
                    } else {
                        notifyItemRemoved(getAdapterPosition());
                    }

                }
                bindCheck(note);
            });
        }

        private void setChangeNoteOnClick(Note note) {
            changeNote.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Note", note);
                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.action_nav_showTodoList_to_editNoteFragment, bundle);
            });
        }

        private void setImage(Note note) {
            if (note.isChecked()) {
                check.setImageResource(R.drawable.baseline_check_circle_outline_black_18dp);
            } else {
                check.setImageResource(R.drawable.baseline_radio_button_unchecked_black_18dp);
            }
        }
    }

    public class ViewHolderDropdown extends RecyclerView.ViewHolder {

        private LinearLayout dropdown;
        private View view;

        private ImageView expand;

        public ViewHolderDropdown(View view) {
            super(view);
            this.view = view;
            DrawerMenuActivity.setHideDone(getHideDone());
            dropdown = view.findViewById(R.id.dropdown);
            dropdownLayout = dropdown;

            expand = view.findViewById(R.id.imageView);
        }

        public LinearLayout getDropdown(){
            return dropdown;
        }

        public void bind() {
            dropdownLayout.setOnClickListener(v -> {
                DrawerMenuActivity.setHideDone(getHideDone());
                if (checkedShown || DrawerMenuActivity.getHideDone()) {
                    expand.animate().rotation(360f).start();
                    checkedShown = false;
                    notifyItemRangeRemoved(getAdapterPosition() + 1, dataSet.getCheckedNotes().size());
                } else {
                    expand.animate().rotation(180f).start();
                    checkedShown = true;
                    notifyItemRangeInserted(getAdapterPosition() + 1, dataSet.getCheckedNotes().size());
                }
            });
        }

    }
}
