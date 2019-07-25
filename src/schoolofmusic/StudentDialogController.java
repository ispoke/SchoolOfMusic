package schoolofmusic;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class StudentDialogController {

    @FXML
    private TextField studentName;

    @FXML
    private TextField studentContact;

    @FXML
    private TextField instrument;

    @FXML
    private TextField level;

    // get the data back from text fields

    public String getStudentName(){
        return studentName.getText().trim();
    }

    public String getStudentContact(){
        return studentContact.getText().trim();
    }

    public String getInstrument(){
        return instrument.getText().trim();
    }

    public String getLevel(){
        return level.getText().trim();
    }

    public void setStudentName(String name){
        studentName.setText(name);
    }

    public void setStudentContact(String contact){
        studentContact.setText(contact);
    }

    public  void setInstrument(String instr){
        instrument.setText(instr);
    }

    public void setLevel(String lvl){
        level.setText(lvl);
    }

}
