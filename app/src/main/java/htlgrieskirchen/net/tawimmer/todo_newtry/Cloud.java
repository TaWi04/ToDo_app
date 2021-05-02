package htlgrieskirchen.net.tawimmer.todo_newtry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Cloud {

    TodoList lastChangeOnList;
    Note lastChangeOnNote;
    User user;
    String url = "http://sickinger-solutions.at/notesserver/todolists.php?username=";
    InternetConnectionHandler connection;

    public Cloud(TodoList lastChangeOnList, Note lastChangeOnNote, User user) {
        this.lastChangeOnList = lastChangeOnList;
        this.lastChangeOnNote = lastChangeOnNote;
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

    public List<TodoList> loadListsFromCloud(){
        List<TodoList> todoLists = new ArrayList<>();
        InternetConnectionHandler.Response response = connection.get(url+user.getUsername() + "&password");
        boolean result = response.startWith(2);
        if(result){
            try {
                BufferedReader br= new BufferedReader(new InputStreamReader(response.getInputStream()));
                JsonArray json = new JsonParser().parse(br.readLine()).getAsJsonArray();
                response.close();
                for (int i = 0; i < json.size(); i++) {
                    JsonObject jsonObject = json.get(i).getAsJsonObject();
                    int id = jsonObject.get("id").getAsInt();
                    String title = jsonObject.get("name").getAsString();
                    String addtionalData = jsonObject.get("additionalData").getAsString();
                    //TODO add Labels
                    ArrayList<Label> labels = new ArrayList<>();
                    TodoList todoList = new TodoList(addtionalData, new ArrayList<>(),labels);
                    todoList.setId(id);
                    todoLists.add(todoList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return todoLists;
    }
}
