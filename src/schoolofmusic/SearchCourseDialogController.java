package schoolofmusic;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SearchCourseDialogController {

    @FXML
    private TextField courseCode;

    public String getCourseCode(){
        return courseCode.getText().trim();
    }
}
