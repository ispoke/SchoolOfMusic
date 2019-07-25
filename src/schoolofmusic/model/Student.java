package schoolofmusic.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty contact;
    private SimpleStringProperty course;

    public Student() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.contact = new SimpleStringProperty();
        this.course = new SimpleStringProperty();
    }


    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public String getCourse() {
        return course.get();
    }

    public void setCourse(String course){
        this.course.set(course);
    }
}
