package htlgrieskirchen.net.tawimmer.todo_newtry;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Note implements Serializable{

    private String title;
    private String noteString;
    private LocalDate date;
    private LocalTime time;
    private boolean important;
    //private List<Note> subTasks;
    private List<Label> labels;
    private boolean checked; //TODO



    public Note(String title, String noteString, boolean important) {
        this.title = title;
        this.noteString = noteString;
        this.important = important;
    }

    public Note(String title, String noteString, LocalDate date, boolean important) {//, List<Note> subTasks) {
        this.title = title;
        this.noteString = noteString;
        this.date = date;
        this.important = important;
        //this.subTasks = subTasks;
    }

    public Note(String title, String noteString, LocalDate date, LocalTime time, boolean important) {//, List<Note> subTasks) {
        this.title = title;
        this.noteString = noteString;
        this.date = date;
        this.time = time;
        this.important = important;
        //this.subTasks = subTasks;
    }

    public boolean isOver(){
        LocalDate localDateNow = LocalDate.now();
        if(date!=null){
            if(localDateNow.isAfter(date)){
                return true;
            }else if(localDateNow.isEqual(date)){
             LocalTime localTimeNow = LocalTime.now();
             if(time != null){
                 if(localTimeNow.getHour() > time.getHour()){
                     return true;
                 }else if(localTimeNow.getHour() == time.getHour()){
                     if(localTimeNow.getMinute() > time.getMinute()){
                         return true;
                     }
                 }
             }
            }
        }
        return false;
    }

    public String getFormatedDateString(String patternDate, String patternTime){
        if(date!=null){
            if(time!=null){
                return date.atTime(time).format(DateTimeFormatter.ofPattern(patternDate+" "+patternTime));
            }else{
                return date.format(DateTimeFormatter.ofPattern(patternDate));
            }
        }else{
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteString() {
        return noteString;
    }

    public void setNoteString(String noteString) {
        this.noteString = noteString;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    /*public List<Note> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Note> subTasks) {
        this.subTasks = subTasks;
    }*/

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
