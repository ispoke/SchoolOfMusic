package schoolofmusic;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TeacherDialogController {

    @FXML
    private TextField teacherName;

    @FXML
    private TextField teacherContact;

    @FXML
    private TextField teacherInstrument;

    @FXML
    private TextField teacherCode;

    public String getTeacherName(){
        return teacherName.getText().trim();
    }

    public String getTeacherContact(){
        return teacherContact.getText().trim();
    }

    public String getTeacherInstrument(){
        return teacherInstrument.getText().trim();
    }

    public String getTeacherCode(){
        return teacherCode.getText().trim();
    }

    public void setTeacherName(String name){
        teacherName.setText(name);
    }

    public void setTeacherContact(String contact) {
        teacherContact.setText(contact);
    }

    public void setTeacherInstrument(String instrument){
        teacherInstrument.setText(instrument);
    }

    public void setTeacherCode(String code){
        teacherCode.setText(code);
    }
}
