package schoolofmusic;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SearchByInstrumentDialogController {

    @FXML
    private TextField searchInstrument;

    @FXML
    public String searchStudentsByInstrument(){
        String instrument = searchInstrument.getText().trim();
        return instrument;
    }
}
