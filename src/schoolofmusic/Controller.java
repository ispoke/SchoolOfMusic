package schoolofmusic;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import schoolofmusic.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class Controller {
/*
    Tables showing Courses, Teachers and Students will be displayed at startup
 */
    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableView<Teacher> teacherTable;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableView<StudentFile> studentByInstrumentTable;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu coursesContextMenu;

    @FXML
    private ContextMenu teachersContextMenu;

    @FXML
    private ContextMenu studentsContextMenu;

    public void initialize(){
        showAllStudents();
        showAllCourses();
        showAllTeachers();

        studentContextSetUp();
        teacherContextSetUp();
        courseContextSetup();
    }

    public void showAllCourses() {
        // Bind Course Observable List to Table items property
        Task<ObservableList<Course>> task = new GetAllCoursesTask();
        courseTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
        courseTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        courseTable.getSelectionModel().selectFirst();
    }

    public void showAllTeachers(){
        //Bind Observable List to table items
        Task<ObservableList<Teacher>> task = new GetAllTeachersTask();
        teacherTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    @FXML
    public  void showAllStudents(){
        Task<ObservableList<Student>> task = new GetAllStudentsTask();
        studentTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }

    @FXML
    public void showStudentByInstrumentDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Show students studying a particular instrument");
        dialog.setHeaderText("Enter the instrument you want to search for: ");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("searchStudentsByInstrumentDialog.fxml"));  // tell it where to look now
        try{
            dialog.getDialogPane().setContent(loader.load());
        }catch(IOException ioex){
            System.out.println("Couldn't load Dialog pane ");
            ioex.printStackTrace();
            return;
        }

        SearchByInstrumentDialogController dialogController = loader.getController();
        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            String instrument = dialogController.searchStudentsByInstrument();
            if (instrument.isEmpty()){
                System.out.println("Instrument value is empty! ");
                return;
            }
            else{
                Task<ObservableList<StudentFile>> task = new GetStudentsByInstrumentTask(instrument);
                studentByInstrumentTable.itemsProperty().bind(task.valueProperty());
                new Thread(task).start();
            }
        }
    }

    @FXML
    public void addNewStudent(){
        studentDialog(true, null);
    }

    @FXML
    public void addNewTeacher(){
        showTeacherDialog(true, null);
    }

    @FXML
    public void addNewCourse(){
        showCourseDialog(true, null);
    }

    @FXML
    public void editStudent(){
        Student student = studentTable.getSelectionModel().getSelectedItem();
        if(student == null){
            itemNotSelectedAlert();
        }else {
            studentDialog(false, student);
        }
    }

    @FXML
    public void editTeacher(){
        Teacher teacher = teacherTable.getSelectionModel().getSelectedItem();
        if(teacher == null){
            itemNotSelectedAlert();
        }else {
            showTeacherDialog(false, teacher);
        }
    }

    @FXML
    public void editCourse(){
        Course course = courseTable.getSelectionModel().getSelectedItem();
        if(course == null){
            itemNotSelectedAlert();
        }else {
            showCourseDialog(false, course);
        }
    }

    @FXML
    public void searchCourseDialog(){
        Dialog<ButtonType> searchCourseDialog = new Dialog<>();
        searchCourseDialog.initOwner(mainBorderPane.getScene().getWindow());
        searchCourseDialog.setTitle("Search for a course by or course code");
        searchCourseDialog.setHeaderText("Note that course code is case sensitive ");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("searchCourseDialog.fxml"));
        try{
            searchCourseDialog.getDialogPane().setContent(loader.load());
        }catch (IOException e){
            System.out.println("Couldn't open the course search dialog pane");
            return;
        }
        searchCourseDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        searchCourseDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        SearchCourseDialogController dialogController = loader.getController();
        Optional<ButtonType> result = searchCourseDialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if(!dialogController.getCourseCode().isEmpty()){
                searchCourse(dialogController.getCourseCode());
            }else{
                showError("Course code field is empty. Cannot search without a code!");
            }
        }
    }

    @FXML
    public void searchTeacherDialog(){
        Dialog<ButtonType> searchTeacherDialog = new Dialog<>();
        searchTeacherDialog.initOwner(mainBorderPane.getScene().getWindow());
        searchTeacherDialog.setTitle("Search for a teacher by name or teacher code");
        searchTeacherDialog.setHeaderText("Please enter either a name or a code: ");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("searchTeacherDialog.fxml"));
        try{
            searchTeacherDialog.getDialogPane().setContent(loader.load());
        }catch (IOException e){
            System.out.println("Couldn't open the teacher search dialog pane");
            return;
        }
        searchTeacherDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        searchTeacherDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        SearchTeacherDialogController dialogController = loader.getController();
        Optional<ButtonType> searchResult = searchTeacherDialog.showAndWait();

        if(searchResult.isPresent() && searchResult.get() == ButtonType.OK){
            if(!dialogController.getTeacherName().isEmpty()){
                searchTeacherByName(dialogController.getTeacherName());
            } else if (!dialogController.getTeacherCode().isEmpty()) {
                searchTeacherByCode(dialogController.getTeacherCode());
            }else{
                showError("Cannot Search for teacher without name or code ");
            }
        }
    }

    // dialog is used for both new student and edit student cases.
    public void studentDialog(Boolean isNewStudent, Student student){
        // Kick off a new Dialog here
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Student Details");
        dialog.setHeaderText("Please Complete all Fields: ");

        // start up the dialog fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("newStudentDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(loader.load());
        }catch(IOException e){
            System.out.println("Couldn't load StudentDialog pane ");
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        StudentDialogController dialogController = loader.getController();

        // Edit case
        if(student != null){
            dialogController.setStudentName(student.getName());
            dialogController.setStudentContact(student.getContact());
            String courseCode = student.getCourse();
            String instrument= findInstrument(courseCode) ;
            String level = findLevel(courseCode);
            dialogController.setInstrument(instrument);
            dialogController.setLevel(level);
        }

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            // Get the data back from the Dialog Controller
            if(isNewStudent){
                    addStudent(dialogController.getStudentName(),
                            dialogController.getStudentContact(),
                            dialogController.getInstrument(),
                            dialogController.getLevel());
            }
            else{  //edit case
                editStudentDetails(student,
                        dialogController.getStudentName(),
                        dialogController.getStudentContact(),
                        dialogController.getInstrument(),
                        dialogController.getLevel());
            }
        }
    }

    @FXML
    public void deleteStudent(){
        Student student = studentTable.getSelectionModel().getSelectedItem();
        if (student == null){
            itemNotSelectedAlert();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Student");
            alert.setHeaderText("Remove "+student.getName()+"?");
            alert.setContentText("Are you sure?  [OK] to confirm, [Cancel] to quit");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && (result.get() == ButtonType.OK)){
                try {
                    removeStudent(student);
                }catch (Exception e){
                    showError(e.getMessage());
                }
            }
        }
    }

    @FXML
    public void deleteTeacher(){
        Teacher teacher = teacherTable.getSelectionModel().getSelectedItem();
        if(teacher == null){
            itemNotSelectedAlert();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Teacher");
            alert.setHeaderText("Are you sure you want to remove "+ teacher.getName()+"?");
            alert.setContentText("If the teacher is associated with a course, delete operation will not execute");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                removeTeacher(teacher);
            }
        }
    }

    @FXML
    public void deleteCourse(){
        Course course = courseTable.getSelectionModel().getSelectedItem();
        if(course == null){
            itemNotSelectedAlert();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Course");
            alert.setHeaderText("Remove course "+course.getCode()+" for "+course.getInstrument()+" level "+course.getLevel());
            alert.setContentText("Warning: This operation will not proceed if there are students registered for the course");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                removeCourse(course);
            }
        }
    }

    public void showTeacherDialog(boolean isNewTeacher, Teacher teacher){
        Dialog<ButtonType> teacherDialog = new Dialog<>();
        teacherDialog.initOwner(mainBorderPane.getScene().getWindow());
        teacherDialog.setTitle("Teacher Details");
        teacherDialog.setHeaderText("Please Complete All fields. Code can be any unique string");

        // start up the dialog fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("teacherDialog.fxml"));
        try{
            teacherDialog.getDialogPane().setContent(loader.load());
        }catch(IOException e){
            System.out.println("Couldn't load Teacher Dialog pane ");
            e.printStackTrace();
            return;
        }

        teacherDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        teacherDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        TeacherDialogController dialogController = loader.getController();

        if(teacher != null){ // edit teacher case
            dialogController.setTeacherName(teacher.getName());
            dialogController.setTeacherContact(teacher.getContact());
            dialogController.setTeacherInstrument(teacher.getInstrument());
            dialogController.setTeacherCode(teacher.getCode());
        }
        Optional<ButtonType> result = teacherDialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if(isNewTeacher){
                addTeacher(dialogController.getTeacherName(), dialogController.getTeacherCode(),
                        dialogController.getTeacherContact(), dialogController.getTeacherInstrument());
            }
            else{
                if(!teacher.getCode().equals(dialogController.getTeacherCode())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Cannot change Code");
                    alert.setContentText("The Teacher's Code cannot be changed");
                    alert.showAndWait();
                }

                editTeacherDetails(teacher, dialogController.getTeacherName(),
                dialogController.getTeacherContact(),
                        dialogController.getTeacherInstrument());
            }
        }
    }

    public void showCourseDialog(boolean isNewCourse, Course course) {
        Dialog<ButtonType> courseDialog = new Dialog<>();
        courseDialog.initOwner(mainBorderPane.getScene().getWindow());
        courseDialog.setTitle("Course Details");
        courseDialog.setHeaderText("Please Complete All fields. Code can be any unique string");

        // start up the dialog fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("courseDialog.fxml"));
        try {
            courseDialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load Course Dialog pane ");
            e.printStackTrace();
            return;
        }

        courseDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        courseDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        CourseDialogController dialogController = loader.getController();

        if (course != null) { // edit case
            courseDialog.setContentText("Course Code, Instrument and Level cannot be changed");
            dialogController.setCourseCode(course.getCode());
            dialogController.setCourseInstrument(course.getInstrument());
            dialogController.setCourseLevel(course.getLevel());
            dialogController.setTeacherCode(course.getTeacherCode());
        }
        Optional<ButtonType> result = courseDialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            if(isNewCourse){
                addCourse(dialogController.getCourseCode(),
                        dialogController.getCourseInstrument(),
                        dialogController.getCourseLevel(),
                        dialogController.getTeacherCode());
            }
            else{
                // only changing teacher for the course.
                editCourseDetails(course.getCode(), dialogController.getTeacherCode());
            }
        }
    }

    /* ===============================================================
                        PRIVATE METHODS
     * =============================================================== */
    private void addStudent(String name, String contact, String instrument, String level)  {
            Task<Boolean> addTask = new Task<>() {
                @Override
                public Boolean call() throws Exception {
                    return (MusicSchoolData.getInstance().addStudent(name, contact, instrument, level));
                }
            };

            addTask.setOnSucceeded(e ->{
                if(addTask.valueProperty().get()){
                    showAllStudents();
                }
            });

            new Thread(addTask).start();
    }

    private void addTeacher(String name, String code, String contact, String instrument){
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return MusicSchoolData.getInstance().addTeacher(code,name,instrument,contact);
            }
        };

        task.setOnSucceeded(e->{
            if(task.valueProperty().get()){
                showAllTeachers();
            }
        });

        new Thread(task).start();
    }

    private void addCourse(String courseCode, String instrument, String level, String teacherCode){
        Task<Boolean> addCourseTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return MusicSchoolData.getInstance().addCourse(instrument, level,courseCode, teacherCode);
            }
        };
        addCourseTask.setOnSucceeded(e->{
            if(addCourseTask.valueProperty().get()){
                showAllCourses();
            }
        });

        new Thread(addCourseTask).start();
    }

    private void removeTeacher(Teacher teacher){
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return MusicSchoolData.getInstance().deleteTeacher(teacher.getCode());
            }
        };

        task.setOnSucceeded(e->{
            if(task.valueProperty().get()){
                showAllTeachers();
            }
        });
        new Thread(task).start();
    }

    private void removeStudent(Student student) throws Exception{
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return MusicSchoolData.getInstance().deleteStudent(student.getId());
            }
        };

        task.setOnSucceeded(e->{
            if(task.valueProperty().get()){
                showAllStudents();
            }
        });

        new Thread(task).start();
    }

    private void removeCourse(Course course){
        Task<Boolean> removeCourseTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return MusicSchoolData.getInstance().deleteCourse(course.getInstrument(), course.getLevel());
            }
        };

        removeCourseTask.setOnSucceeded(e->{
            if(removeCourseTask.valueProperty().get()){
                showAllCourses();
            }
        });

        new Thread(removeCourseTask).start();
    }

    private void editStudentDetails(Student student, String newName, String newContact, String newInstrument, String newLevel){

        if(!student.getName().equals(newName)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Change Student Name?");
            alert.setHeaderText("Change name from "+student.getName()+" to "+ newName+ "?");
            alert.setContentText("Are you sure you want to do this?");
            Optional<ButtonType> change = alert.showAndWait();
            if(change.isPresent()&& change.get()== ButtonType.OK){
                Task<Boolean> updateNameTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return MusicSchoolData.getInstance().updateStudentName(student.getId(), newName);
                    }
                };

                updateNameTask.setOnSucceeded(e->{
                    if(updateNameTask.valueProperty().get()){
                        showAllStudents();
                    }
                });
                new Thread(updateNameTask).start();
            }
        }

        //check if the details have changed
        if(!student.getContact().equals(newContact)){
            Task<Boolean> updateContactTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return MusicSchoolData.getInstance().updateStudentContact(student.getId(), newContact);
                }
            };

            updateContactTask.setOnSucceeded(e->{
                if(updateContactTask.valueProperty().get()){
                    showAllStudents();
                }
            });
            new Thread(updateContactTask).start();
        }

        String newCourseCode= null;
        try{
            newCourseCode = MusicSchoolData.getInstance().queryCourseCode(newInstrument, newLevel);
        }catch (Exception e){
            showError(e.getMessage());
            return;
        }

        if(!student.getCourse().equals(newCourseCode)){
            Task<Boolean> updateCourseTask = new Task<Boolean>(){
                @Override
                protected Boolean call() throws Exception {
                    return  MusicSchoolData.getInstance().updateStudentCourse(student.getId(),
                            newInstrument, newLevel);
                }
            };

            updateCourseTask.setOnSucceeded(e->{
                if(updateCourseTask.valueProperty().get()){
                    showAllStudents();
                }
            });
            new Thread(updateCourseTask).start();
        }
    }

    private String findInstrument(String courseCode){
        String instrument = null;
        try {
            instrument = MusicSchoolData.getInstance().queryCourseInstrument(courseCode);
        }catch(SQLException e ){
            showError(e.getMessage());
        }
        return instrument;
    }

    private String findLevel(String courseCode){
        String newLevel = null;
        try{
            newLevel = MusicSchoolData.getInstance().queryCourseLevel(courseCode);
        }catch(Exception e){
            showError(e.getMessage());
        }
        return newLevel;
    }

    private void editTeacherDetails(Teacher teacher, String name, String contact, String instrument){
        if(!teacher.getName().equals(name)){
            Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.setTitle("Change Teacher Name");
            alert.setContentText("Are you sure you want to change the teacher's name from "+teacher.getName()+" to "+ name+"?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get()== ButtonType.OK){
                Task<Boolean> updateNameTask = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return MusicSchoolData.getInstance().updateTeacherName(teacher.getId(), name);
                    }
                };

                updateNameTask.setOnSucceeded(e->{
                    if(updateNameTask.valueProperty().get()){
                        showAllTeachers();
                    }
                });

                new Thread(updateNameTask).start();
            }
        }

        if(!teacher.getContact().equals(contact)){
            Task<Boolean> updateContactTask = new Task<> (){
                @Override
                protected Boolean call() throws Exception {
                    return MusicSchoolData.getInstance().updateTeacherContact(teacher.getId(), contact);
                }
            };

            updateContactTask.setOnSucceeded(e->{
                if(updateContactTask.valueProperty().get()){
                    showAllTeachers();
                }
            });

            new Thread(updateContactTask).start();
        }

        if(!teacher.getInstrument().equals(instrument)){
            Task<Boolean> updateInstTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return MusicSchoolData.getInstance().updateTeacherInstrument(teacher.getId(), instrument);
                }
            };
            updateInstTask.setOnSucceeded(e->{
                if(updateInstTask.valueProperty().get()){
                    showAllTeachers();
                }
            });
            new Thread(updateInstTask).start();
        }
    }

    private void editCourseDetails(String courseCode, String teacherCode){
        Task<Boolean> updateCourseTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return MusicSchoolData.getInstance().updateCourseTeacher(courseCode, teacherCode);
            }
        };

        updateCourseTask.setOnSucceeded(e->{
            if(updateCourseTask.valueProperty().get()){
                showAllCourses();
            }
        });
        new Thread(updateCourseTask).start();
    }

    private void showError(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            return;
        }
    }

    private void itemNotSelectedAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Item Not Selected");
        alert.setContentText("Select an item from the list to continue with the operation");
        Optional<ButtonType> result = alert.showAndWait();
    }

    private void studentContextSetUp(){
        studentsContextMenu = new ContextMenu();
        MenuItem deleteStudentMenuItem = new MenuItem("Delete");
        MenuItem editStudentMenuItem = new MenuItem("Edit");

        studentsContextMenu.getItems().add(deleteStudentMenuItem);
        studentsContextMenu.getItems().add(editStudentMenuItem);

        studentTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    studentsContextMenu.show(studentTable,mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });

        deleteStudentMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteStudent();
            }
        });

        editStudentMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                editStudent();
            }
        });
    }

    private void courseContextSetup(){
        coursesContextMenu = new ContextMenu();
        MenuItem deleteCourseMenuItem = new MenuItem("Delete Course");
        coursesContextMenu.getItems().add(deleteCourseMenuItem);

        // Will want to edit course to update it with a different teacher
        MenuItem editCourseMenuItem = new MenuItem("Edit Course");
        coursesContextMenu.getItems().add(editCourseMenuItem);

        courseTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    coursesContextMenu.show(courseTable, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });

        deleteCourseMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteCourse();
            }
        });

        editCourseMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                editCourse();
            }
        });
    }

    private void searchTeacherByName(String name){
        Teacher teacher = MusicSchoolData.getInstance().searchTeacherByName(name);
        if(teacher != null){
            showTeacherSearchResult(teacher);
        }else{
            teacherNotFoundAlert(name);
        }
    }

    private void searchTeacherByCode(String code){
        Teacher teacher = MusicSchoolData.getInstance().searchTeacherByCode(code);
        if(teacher != null){
            showTeacherSearchResult(teacher);
        }else{
            teacherNotFoundAlert(code);
        }
    }

    private void searchCourse(String code){
        Course course;
        course = MusicSchoolData.getInstance().searchCourseByCode(code);
        if(course == null){
            courseNotFoundAlert(code);
        }
        else{
            showCourseSearchResult(course);
        }
    }
    private void teacherNotFoundAlert(String searchString){
        Alert teacherNotFound = new Alert(Alert.AlertType.INFORMATION);
        teacherNotFound.setTitle("Teacher Not Found");
        teacherNotFound.setHeaderText("Cant find a teacher using the search '"+ searchString+"'");
        teacherNotFound.showAndWait();
    }

    private void showTeacherSearchResult(Teacher teacher){
        Alert teacherFound = new Alert(Alert.AlertType.INFORMATION);
        teacherFound.setTitle("Here's the Teacher you searched for:");
        teacherFound.setContentText("NAME: "+teacher.getName()+"\n"+
                "ID: " + teacher.getId()+"\n"+
                "CODE: "+teacher.getCode()+"\n"+
                "CONTACT: "+teacher.getContact()+"\n"+
                "TEACHES: "+teacher.getInstrument());
        teacherFound.showAndWait();
    }

    private void courseNotFoundAlert(String code){
        Alert courseNotFound = new Alert(Alert.AlertType.INFORMATION);
        courseNotFound.setTitle("Course Not Found");
        courseNotFound.setHeaderText("Couldn't find a course for code "+code);
        courseNotFound.showAndWait();
    }

    private void showCourseSearchResult(Course course){
        Alert courseFound = new Alert(Alert.AlertType.INFORMATION);
        courseFound.setTitle("Here's the course you searched for: ");
        courseFound.setContentText("Id: "+course.getCourseId()+"\n"+
                "Code: "+course.getCode()+"\n"+
                "Instrument: "+ course.getInstrument()+ "\n"+
                "Level: "+course.getLevel()+"\n"+
                "Teacher Code: "+ course.getTeacherCode());
        courseFound.showAndWait();
    }

    private void teacherContextSetUp(){
        teachersContextMenu = new ContextMenu();
        MenuItem deleteTeacherMenuItem = new MenuItem("Delete");
        teachersContextMenu.getItems().add(deleteTeacherMenuItem);
        MenuItem editTeacherMenuItem = new MenuItem("Edit");
        teachersContextMenu.getItems().add(editTeacherMenuItem);

        teacherTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                    teachersContextMenu.show(teacherTable, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });

        deleteTeacherMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteTeacher();
            }
        });

        editTeacherMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                editTeacher();
            }
        });
    }
}
/* ===================================================================================================================
   Task classes used when reading data back from database
   These tasks are created here as they may need to be used in two places i.e at start up and on explicit request.
 * ===================================================================================================================*/
class GetAllCoursesTask extends Task{
    @Override
    public ObservableList<Course> call()  {
        return FXCollections.observableArrayList(MusicSchoolData.getInstance().getCourses());
    }
}

class GetAllTeachersTask extends Task{
    @Override
    public ObservableList<Teacher> call()  {
        return FXCollections.observableArrayList(MusicSchoolData.getInstance().getTeachers());
    }
}

class GetAllStudentsTask extends Task{
    @Override
    public ObservableList<Student> call() {
        return FXCollections.observableArrayList(MusicSchoolData.getInstance().getStudents());
    }
}

class GetStudentsByInstrumentTask extends Task{
    private String instrument;
    public GetStudentsByInstrumentTask(String instrument){
        this.instrument = instrument;
    }
    @Override
    public ObservableList<StudentFile> call()  {
        return FXCollections.observableArrayList(MusicSchoolData.getInstance().getStudentsByInstrument(instrument));
    }
}













