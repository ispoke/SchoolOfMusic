package schoolofmusic;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CourseDialogController {

    @FXML
    TextField courseCode;

    @FXML
    TextField courseInstrument;

    @FXML
    TextField courseLevel;

    @FXML
    TextField teacherCode;

    //setters & getters
    public String getCourseCode(){
        return courseCode.getText().trim();
    }

    public String getCourseInstrument(){
        return courseInstrument.getText().trim();
    }

    public String getCourseLevel(){
        return courseLevel.getText().trim();
    }

    public String getTeacherCode(){
        return teacherCode.getText().trim();
    }

    public void setCourseCode(String code){
        courseCode.setText(code);
    }

    public void setCourseInstrument(String instrument){
        courseInstrument.setText(instrument);
    }

    public void setCourseLevel(String level) {
        courseLevel.setText(level);
    }

    public void setTeacherCode(String code){
        teacherCode.setText(code);
    }
}
