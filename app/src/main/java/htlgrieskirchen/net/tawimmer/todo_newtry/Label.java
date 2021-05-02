package htlgrieskirchen.net.tawimmer.todo_newtry;

import java.io.Serializable;

public class Label  implements Serializable {

    private String name;
    private String value;
    private boolean checked;

    public Label(String name) {
        this.name = name;
        this.value = name.toLowerCase();
    }

    public String getName() {
        return name;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean pChecked) {
         checked = pChecked;
    }
    public String getValue() {
        return value;
    }
}
