package htlgrieskirchen.net.tawimmer.todo_newtry;

public class Label {

    private String name;
    private String value;

    public Label(String name) {
        this.name = name;
        this.value = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
