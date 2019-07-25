package schoolofmusic;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SearchTeacherDialogController {

    @FXML
    private TextField teacherName;

    @FXML
    private TextField teacherCode;

    public String getTeacherName(){
        return teacherName.getText().trim();
    }

    public String getTeacherCode(){
        return teacherCode.getText().trim();
    }
}
