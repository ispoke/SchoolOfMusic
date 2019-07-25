package schoolofmusic.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Teacher {

    private SimpleIntegerProperty id;
    private SimpleStringProperty code;
    private SimpleStringProperty name;
    private SimpleStringProperty instrument;
    private SimpleStringProperty contact;

    public Teacher(){
        this.id = new SimpleIntegerProperty();
        this.code = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.instrument = new SimpleStringProperty();
        this.contact = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getInstrument() {
        return instrument.get();
    }

    public void setInstrument(String instrument) {
        this.instrument.set(instrument);
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }
}
