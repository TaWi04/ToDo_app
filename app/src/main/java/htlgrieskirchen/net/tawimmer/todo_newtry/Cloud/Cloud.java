package htlgrieskirchen.net.tawimmer.todo_newtry.Cloud;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import htlgrieskirchen.net.tawimmer.todo_newtry.Label;
import htlgrieskirchen.net.tawimmer.todo_newtry.Note;
import htlgrieskirchen.net.tawimmer.todo_newtry.TodoList;

public class Cloud {

    TodoList lastChangeOnList;
    Note lastChangeOnNote;
    User user;
    String url = "http://sickinger-solutions.at/notesserver/";
    InternetConnectionHandler connection;

    public Cloud(User user) {
        this.user = user;
        this.connection = new InternetConnectionHandler();
    }

    public TodoList getLastChangeOnList() {
        return lastChangeOnList;
    }

    public Note getLastChangeOnNote() {
        return lastChangeOnNote;
    }

    public User getUser() {
        return user;
    }

    public List<TodoList> loadListsFromCloud() {
        ArrayList<TodoList> todoLists = new ArrayList<>();
        InternetConnectionHandler.Response response = connection.get(url+"todolists.php?" +"&username="+ user.getUsername() + "&password="+ user.getPassword());
        boolean result = response.startWith(2);
        if (result) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getInputStream()));
                JsonArray json = new JsonParser().parse(br.readLine()).getAsJsonArray();
                response.close();
                for (int i = 0; i < json.size(); i++) {
                    JsonObject jsonObject = json.get(i).getAsJsonObject();
                    int id = jsonObject.get("id").getAsInt();
                    String title = jsonObject.get("name").getAsString();
                    String addtionalData = jsonObject.get("additionalData").getAsString();
                    //TODO add Labels
                    ArrayList<Label> labels = new ArrayList<>();
                    TodoList todoList = new TodoList(title, new ArrayList<>(), labels);
                    todoList.setId(id);
                    todoLists.add(todoList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadNotesFromCloud(todoLists);
        return todoLists;
    }

    private void loadNotesFromCloud(ArrayList<TodoList> todoLists) {
        InternetConnectionHandler.Response response = connection.get(url+"todo.php?"+"&username=" + user.getUsername() + "&password=" + user.getPassword());
        boolean result = response.startWith(2);
        if (result) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getInputStream()));
                JsonArray json = new JsonParser().parse(br.readLine()).getAsJsonArray();
                response.close();
                for (int i = 0; i < json.size(); i++) {
                    JsonObject jsonObject = json.get(i).getAsJsonObject();
                    int id = jsonObject.get("id").getAsInt();
                    String title = jsonObject.get("title").getAsString();
                    String details = jsonObject.get("description").getAsString();
                    String dateTime = jsonObject.get("dueDate").getAsString();
                    DateTimeFormatter cloudFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDateTime = LocalDateTime.parse(dateTime, cloudFormat);
                    DateTimeFormatter appFormatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    DateTimeFormatter appFormatTime = DateTimeFormatter.ofPattern("HH:mm");
                    String dateString = localDateTime.format(appFormatDate);
                    LocalDate date = LocalDate.parse(dateString, appFormatDate);
                    String timeString = localDateTime.format(appFormatTime);
                    LocalTime time = LocalTime.parse(timeString, appFormatTime);
                    boolean done;
                    done = !jsonObject.get("state").getAsString().equals("OPEN");
                    Note note = new Note(title, details, date, time, false);
                    note.setChecked(done);
                    note.setId(id);
                    int idList = jsonObject.get("todoListId").getAsInt();
                    //note.setLocation(jsonObject.get("additionalData").getAsString()); //TODO
                    for (TodoList todolist : todoLists) {
                        if (todolist.getId() == idList) {
                            todolist.getNotes().add(note);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void addList(TodoList todoList) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", todoList.getTitle());
            jsonObject.put("additionalData", ""); //TODO LABEL
            InternetConnectionHandler.Response response = connection.post(url +"todolists.php?"+"&username="+ user.getUsername() + "&password=" + user.getPassword(),jsonObject.toString());
            boolean result = response.startWith(2);

            if(result){
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getInputStream()));
                JsonObject json = new JsonParser().parse(br.readLine()).getAsJsonObject();
                todoList.setId(json.get("id").getAsInt());
                lastChangeOnList = todoList;
            }
            response.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editList(TodoList todoList) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", todoList.getTitle());
            jsonObject.put("additionalData", ""); //TODO LABEL
            InternetConnectionHandler.Response response = connection.put(url+"todolists.php?" +"id=" + todoList.getId() +"&username=" + user.getUsername() + "&password=" + user.getPassword(),jsonObject.toString());
            response.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteList(TodoList todoList) {
            InternetConnectionHandler.Response response = connection.delete(url +"todolists.php?"+"id=" + todoList.getId() +"&username="+ user.getUsername() + "&password=" + user.getPassword());
            response.close();
    }


    public void addNote(TodoList list, Note note){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("todoListId",list.getId());
            jsonObject.put("title", note.getTitle());
            if(note.getNoteString() == null || note.getNoteString().equals("")){
                jsonObject.put("description", "_");
            }else{
                jsonObject.put("description", note.getNoteString());
            }
            if(note.getFormatedDateString("yyyy-mm-dd","HH:mm:ss") == null){
                jsonObject.put("dueDate", "1999-01-12 00:01:11");
            }else{
                jsonObject.put("dueDate", note.getFormatedDateString("yyyy-mm-dd","HH:mm:ss"));
            }
           if(note.isChecked()){
                jsonObject.put("state","CLOSED");
            }else{
                jsonObject.put("state","OPEN");
            }
            jsonObject.put("additionalData", "_");//note.isImportant() + ";"+ note.getLocation());

            InternetConnectionHandler.Response response = connection.post(url+"todo.php?username="+ user.getUsername() + "&password=" + user.getPassword(),jsonObject.toString());
            boolean result = response.startWith(2);
            System.out.println("Add Note");
            if(result){
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getInputStream()));
                JsonObject json = new JsonParser().parse(br.readLine()).getAsJsonObject();
                note.setId(json.get("id").getAsInt());
                lastChangeOnNote = note;
            }
            response.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editNote(TodoList list, Note note){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("todoListId", list.getId());
            jsonObject.put("title", note.getTitle());
            jsonObject.put("description", note.getNoteString());
            jsonObject.put("dueDate", note.getFormatedDateString("dd.MM.yyyy","HH:mm"));
            if(note.isChecked()){
                jsonObject.put("state","CLOSED");
            }else{
                jsonObject.put("state","OPEN");
            }
            jsonObject.put("additionalData", "");//note.isImportant() + ";"+ note.getLocation());

            InternetConnectionHandler.Response response = connection.put(url+"todo.php?" +"id="+note.getId()+"&username="+ user.getUsername() + "&password=" + user.getPassword(),jsonObject.toString());

            response.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteNote(Note note){
        InternetConnectionHandler.Response response = connection.delete(url +"todo.php?"+"id="+note.getId()+"&username="+ user.getUsername() + "&password=" + user.getPassword());
        response.close();

    }
}
