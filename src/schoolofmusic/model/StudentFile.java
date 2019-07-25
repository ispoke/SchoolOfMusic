package schoolofmusic.model;

import javafx.beans.property.SimpleStringProperty;

public class StudentFile {

    private SimpleStringProperty studentName;
    private SimpleStringProperty contact;
    private SimpleStringProperty instrument;
    private SimpleStringProperty level;
    private SimpleStringProperty teacher;

    public StudentFile(){
        this.studentName = new SimpleStringProperty();
        this.contact = new SimpleStringProperty();
        this.instrument = new SimpleStringProperty();
        this.level = new SimpleStringProperty();
        this.teacher = new SimpleStringProperty();
    }

    public String getStudentName() {
        return studentName.get();
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
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

    public String getTeacher() {
        return teacher.get();
    }

    public void setTeacher(String teacher) {
        this.teacher.set(teacher);
    }
}
