package schoolofmusic.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Course {
    private SimpleIntegerProperty id;
    private SimpleStringProperty code;
    private SimpleStringProperty instrument;
    private SimpleStringProperty level;
    private SimpleStringProperty teacherCode;

    public Course(){
        this.id = new SimpleIntegerProperty();
        this.code = new SimpleStringProperty();
        this.instrument = new SimpleStringProperty();
        this.level = new SimpleStringProperty();
        this.teacherCode = new SimpleStringProperty();
    }

    public int getCourseId() {
        return id.get();
    }

    public void setCourseId(int id) {
        this.id.set(id);
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getInstrument() {
        return instrument.get();
    }

    public void setInstrument(String instrument) {
        this.instrument.set(instrument);
    }

    public String getLevel() {
        return level.get();
    }

    public void setLevel(String level) {
        this.level.set(level);
    }

    public String getTeacherCode() {
        return teacherCode.get();
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode.set(teacherCode);
    }
}
