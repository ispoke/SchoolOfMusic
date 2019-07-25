package schoolofmusic.model;

import java.sql.*;          // Note: add  "requires java.sql;" in module-info.java file
import java.util.ArrayList;
import java.util.List;

public class MusicSchoolData {
    private static final boolean DEBUG = false;  // set to true for debug mode
    private static final String DB_NAME = "schoolOfMusicDB.db";
    private static final String DB_CONNECTION_PATH = "jdbc:sqlite:C:\\JavaDevelopment\\Projects\\SchoolOfMusicUI\\" + DB_NAME;

    private static final String TABLE_COURSES = "courses";
    private static final String COLUMN_COURSE_ID = "_id";
    private static final String COLUMN_COURSE_CODE = "code";
    private static final String COLUMN_COURSE_INSTRUMENT = "instrument";
    private static final String COLUMN_COURSE_LEVEL = "level";
    private static final String COLUMN_COURSE_TEACHER_CODE = "teacher_id";

    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_STUDENT_ID = "_id";
    private static final String COLUMN_STUDENT_NAME = "name";
    private static final String COLUMN_STUDENT_CONTACT = "contact";
    private static final String COLUMN_STUDENT_COURSE = "course";

    private static final String TABLE_TEACHERS = "teachers";
    private static final String COLUMN_TEACHER_ID = "_id";
    private static final String COLUMN_TEACHER_CODE = "code";
    private static final String COLUMN_TEACHER_NAME = "name";
    private static final String COLUMN_TEACHER_INSTRUMENT = "instrument";
    private static final String COLUMN_TEACHER_CONTACT = "contact";

    // Indices for table columns: More efficient to use column indices in queries
    private static final int INDEX_COURSE_ID = 1;
    private static final int INDEX_COURSE_CODE = 2;
    private static final int INDEX_COURSE_INSTRUMENT = 3;
    private static final int INDEX_COURSE_LEVEL = 4;
    private static final int INDEX_COURSE_TEACHER_CODE = 5;

    private static final int INDEX_STUDENT_ID = 1;
    private static final int INDEX_STUDENT_NAME = 2;
    private static final int INDEX_STUDENT_CONTACT = 3;
    private static final int INDEX_STUDENT_COURSE = 4;

    private static final int INDEX_TEACHER_ID = 1;
    private static final int INDEX_TEACHER_CODE = 2;
    private static final int INDEX_TEACHER_NAME = 3;
    private static final int INDEX_TEACHER_INSTRUMENT = 4;
    private static final int INDEX_TEACHER_CONTACT = 5;

    // Queries
    private static final String QUERY_ALL_STUDENTS = "SELECT * FROM " + TABLE_STUDENTS;
    private static final String QUERY_ALL_TEACHERS = "SELECT * FROM " + TABLE_TEACHERS;
    private static final String QUERY_ALL_COURSES = "SELECT * FROM " + TABLE_COURSES;
    private static final String QUERY_COURSE_CODE = "SELECT " + COLUMN_COURSE_CODE + " FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_INSTRUMENT
            + " = ?" + " AND " + COLUMN_COURSE_LEVEL + " = ?";
    private static final String QUERY_COURSE_ID = "SELECT " + COLUMN_COURSE_ID + " FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_INSTRUMENT
            + " = ?" + " AND " + COLUMN_COURSE_LEVEL + " = ?";

    private static final String QUERY_COURSE_INSTRUMENT = "SELECT " + COLUMN_COURSE_INSTRUMENT + " FROM " + TABLE_COURSES + " WHERE " +
            COLUMN_COURSE_CODE + " = ?";

    private static final String QUERY_COURSE_LEVEL = "SELECT " + COLUMN_COURSE_LEVEL + " FROM " + TABLE_COURSES + " WHERE " +
            COLUMN_COURSE_CODE + " = ?";

    private static final String QUERY_COURSE_BY_CODE = "SELECT * FROM " + TABLE_COURSES + " WHERE "+ COLUMN_COURSE_CODE + "= ?";

    private static final String QUERY_COURSES_BY_TEACHER = "SELECT * FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_TEACHER_CODE + " = ?";

    private static final String QUERY_TEACHER_CODE = "SELECT " + COLUMN_TEACHER_CODE + " FROM " + TABLE_TEACHERS + " WHERE " + COLUMN_TEACHER_NAME
            + " = ? ";
    private static final String QUERY_TEACHER_BY_NAME = "SELECT * FROM " + TABLE_TEACHERS + " WHERE " + COLUMN_TEACHER_NAME
            + " LIKE ?";

    private static final String QUERY_TEACHER_BY_CODE = "SELECT * FROM " + TABLE_TEACHERS + " WHERE " + COLUMN_TEACHER_CODE
            + " = ?";

    // Create a view of students, courses and teachers
    private static final String VIEW_STUDENTS_COURSES = "student_list";
    private static final String CREATE_STUDENTS_COURSES_VIEW = "CREATE VIEW IF NOT EXISTS " + VIEW_STUDENTS_COURSES +
            " AS SELECT " + TABLE_STUDENTS + "." + COLUMN_STUDENT_NAME + ", " + TABLE_STUDENTS + "." + COLUMN_STUDENT_CONTACT + ", " +
            TABLE_COURSES + "." + COLUMN_COURSE_INSTRUMENT + ", " + TABLE_COURSES + "." + COLUMN_COURSE_LEVEL + ", " +
            TABLE_TEACHERS + "." + COLUMN_TEACHER_NAME +
            " FROM " + TABLE_STUDENTS +
            " INNER JOIN " + TABLE_COURSES + " ON " + TABLE_STUDENTS + "." + COLUMN_STUDENT_COURSE + " = " + TABLE_COURSES + "." + COLUMN_COURSE_CODE +
            " INNER JOIN " + TABLE_TEACHERS + " ON " + TABLE_COURSES + "." + COLUMN_COURSE_TEACHER_CODE + " = " + TABLE_TEACHERS + "." + COLUMN_TEACHER_CODE +
            " ORDER BY " + TABLE_COURSES + "." + COLUMN_COURSE_LEVEL;


    // Query View for students studying a particular instrument
    private static final String QUERY_STUDENTS_BY_INSTRUMENT = "SELECT  * FROM " + VIEW_STUDENTS_COURSES + " WHERE " +
            COLUMN_COURSE_INSTRUMENT + " = ?";


    private static final String INSERT_NEW_STUDENT = "INSERT INTO " + TABLE_STUDENTS + "(" + COLUMN_STUDENT_NAME + ", " +
            COLUMN_STUDENT_CONTACT + ", " + COLUMN_STUDENT_COURSE + ")" + " VALUES " + "(? , ?, ?)";   //

    private static final String UPDATE_STUDENT_COURSE = "UPDATE OR REPLACE " + TABLE_STUDENTS + " SET " + COLUMN_STUDENT_COURSE + " = ? " + " WHERE " + COLUMN_STUDENT_ID + " = ? ";

    private static final String UPDATE_STUDENT_CONTACT = "UPDATE OR REPLACE " + TABLE_STUDENTS + " SET " + COLUMN_STUDENT_CONTACT + " = ? "+ " WHERE " + COLUMN_STUDENT_ID + " = ? ";

    private static final String UPDATE_STUDENT_NAME = "UPDATE OR REPLACE " + TABLE_STUDENTS + " SET " + COLUMN_STUDENT_NAME + " = ? " + " WHERE " + COLUMN_STUDENT_ID + " = ?";

    private static final String DELETE_STUDENT = "DELETE FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_ID + " = ? ";

    private static final String ADD_NEW_COURSE = "INSERT INTO " + TABLE_COURSES + "(" + COLUMN_COURSE_CODE + ", " + COLUMN_COURSE_INSTRUMENT + ", "
            + COLUMN_COURSE_LEVEL + ", " + COLUMN_COURSE_TEACHER_CODE + ")" + " VALUES " + "(?, ?, ?, ? )";

    private static final String ADD_TEACHER = "INSERT INTO " + TABLE_TEACHERS + "(" + COLUMN_TEACHER_CODE + ", " + COLUMN_TEACHER_NAME + ", " +
            COLUMN_TEACHER_INSTRUMENT + ", " + COLUMN_TEACHER_CONTACT + ")" + " VALUES " + "(?, ?, ?, ?)";

    private static final String UPDATE_TEACHER_NAME = "UPDATE OR REPLACE "+ TABLE_TEACHERS + " SET "+ COLUMN_TEACHER_NAME + " = ?"+
            " WHERE "+ COLUMN_TEACHER_ID +"= ?";

    private static final String UPDATE_TEACHER_CONTACT = "UPDATE OR REPLACE "+ TABLE_TEACHERS + " SET "+ COLUMN_TEACHER_CONTACT + " = ?"+
            " WHERE "+ COLUMN_TEACHER_ID +"= ?";

    private static final String UPDATE_TEACHER_INSTRUMENT = "UPDATE OR REPLACE "+ TABLE_TEACHERS + " SET "+ COLUMN_TEACHER_INSTRUMENT + " = ?"+
            " WHERE "+ COLUMN_TEACHER_ID +"= ?";

    private static final String UPDATE_COURSE_TEACHER_CODE = "UPDATE OR REPLACE " + TABLE_COURSES + " SET " + COLUMN_COURSE_TEACHER_CODE + " = ? " +
            " WHERE " + COLUMN_COURSE_CODE + " = ? ";

    private static final String DELETE_COURSE = "DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_ID + " = ? ";
    private static final String DELETE_TEACHER = "DELETE FROM " + TABLE_TEACHERS + " WHERE " + COLUMN_TEACHER_ID + " = ? ";

    // update teachers contact
    // update student contact

    //--------------------------------------------------------------------------------------------------------------------------------------
    private Connection conn;
    private PreparedStatement queryAllCourses;
    private PreparedStatement queryAllTeachers;
    private PreparedStatement queryAllStudents;
    private PreparedStatement getStudentsByInstrument;
    private PreparedStatement queryCourseCode;
    private PreparedStatement queryCourseId;
    private PreparedStatement queryCourseInstrument;
    private PreparedStatement queryCourseLevel;
    private PreparedStatement queryCourseByCode;
    private PreparedStatement queryCoursesByTeacher;
    private PreparedStatement queryTeacherCode;
    private PreparedStatement queryTeacherByName;
    private PreparedStatement queryTeacherByCode;
    private PreparedStatement addStudent;
    private PreparedStatement addCourse;
    private PreparedStatement addTeacher;
    private PreparedStatement updateTeacherName;
    private PreparedStatement updateTeacherContact;
    private PreparedStatement updateTeacherInstrument;
    private PreparedStatement updateStudentCourse;
    private PreparedStatement updateStudentContact;
    private PreparedStatement updateStudentName;
    private PreparedStatement updateCourseTeacher;
    private PreparedStatement deleteStudent;
    private PreparedStatement deleteCourse;
    private PreparedStatement deleteTeacher;

    // Singleton
    private static MusicSchoolData instance = new MusicSchoolData();

    private MusicSchoolData() {
    }

    public static MusicSchoolData getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(DB_CONNECTION_PATH);

            queryAllCourses = conn.prepareStatement(QUERY_ALL_COURSES);
            queryAllTeachers = conn.prepareStatement(QUERY_ALL_TEACHERS);
            queryAllStudents = conn.prepareStatement(QUERY_ALL_STUDENTS);

            if (createViewOfStudents()) { // need this view in order to execute the following prepared statement
                getStudentsByInstrument = conn.prepareStatement(QUERY_STUDENTS_BY_INSTRUMENT);
            } else {
                System.out.println("Couldn't create view of students");
            }

            queryCourseCode = conn.prepareStatement(QUERY_COURSE_CODE);
            queryCourseId = conn.prepareStatement(QUERY_COURSE_ID);
            queryCourseInstrument = conn.prepareStatement(QUERY_COURSE_INSTRUMENT);
            queryCourseLevel = conn.prepareStatement(QUERY_COURSE_LEVEL);
            queryCourseByCode = conn.prepareStatement(QUERY_COURSE_BY_CODE);
            queryTeacherCode = conn.prepareStatement(QUERY_TEACHER_CODE);
            queryTeacherByName = conn.prepareStatement(QUERY_TEACHER_BY_NAME);
            queryTeacherByCode = conn.prepareStatement(QUERY_TEACHER_BY_CODE);
            queryCourseId = conn.prepareStatement((QUERY_COURSE_ID));
            queryCoursesByTeacher = conn.prepareStatement(QUERY_COURSES_BY_TEACHER);
            addStudent = conn.prepareStatement(INSERT_NEW_STUDENT);
            addCourse = conn.prepareStatement(ADD_NEW_COURSE);
            addTeacher = conn.prepareStatement(ADD_TEACHER);
            updateTeacherName = conn.prepareStatement(UPDATE_TEACHER_NAME);
            updateTeacherContact = conn.prepareStatement(UPDATE_TEACHER_CONTACT);
            updateTeacherInstrument = conn.prepareStatement(UPDATE_TEACHER_INSTRUMENT);
            updateStudentCourse = conn.prepareStatement(UPDATE_STUDENT_COURSE);
            updateStudentContact = conn.prepareStatement(UPDATE_STUDENT_CONTACT);
            updateStudentName = conn.prepareStatement(UPDATE_STUDENT_NAME);
            updateCourseTeacher = conn.prepareStatement(UPDATE_COURSE_TEACHER_CODE);
            deleteStudent = conn.prepareStatement(DELETE_STUDENT);
            deleteCourse = conn.prepareStatement(DELETE_COURSE);
            deleteTeacher = conn.prepareStatement(DELETE_TEACHER);

            return true;
        } catch (SQLException e) {
            System.out.println("OOOplaah! Problem opening database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            // close all prepared statements

            if (queryAllStudents != null) {
                queryAllStudents.close();
            }

            if (queryAllCourses != null) {
                queryAllCourses.close();
            }

            if (queryAllTeachers != null) {
                queryAllTeachers.close();
            }

            if (getStudentsByInstrument != null) {
                getStudentsByInstrument.close();
            }

            if (queryCourseCode != null) {
                queryCourseCode.close();
            }

            if (queryCourseId != null) {
                queryCourseId.close();
            }

            if (queryCourseInstrument != null) {
                queryCourseLevel.close();
            }

            if (queryCourseLevel != null) {
                queryCourseLevel.close();
            }

            if(queryCourseByCode != null){
                queryCourseByCode.close();
            }

            if (queryCoursesByTeacher != null) {
                queryCoursesByTeacher.close();
            }

            if (queryTeacherCode != null) {
                queryTeacherCode.close();
            }
            if (queryTeacherByName != null) {
                queryTeacherByName.close();
            }
            if (queryTeacherByCode != null) {
                queryTeacherByCode.close();
            }

            if (addStudent != null) {
                addStudent.close();
            }

            if (updateStudentCourse != null) {
                updateStudentCourse.close();
            }

            if(updateStudentContact != null){
                updateStudentContact.close();
            }

            if(updateStudentName != null){
                updateStudentName.close();
            }

            if (updateCourseTeacher != null) {
                updateCourseTeacher.close();
            }

            if (deleteStudent != null) {
                deleteStudent.close();
            }

            if (addCourse != null) {
                addCourse.close();
            }

            if (addTeacher != null) {
                addTeacher.close();
            }

            if(updateTeacherName != null){
                updateTeacherName.close();
            }

            if(updateTeacherContact != null){
                updateTeacherContact.close();
            }

            if(updateTeacherInstrument != null){
                updateTeacherInstrument.close();
            }

            if (deleteCourse != null) {
                deleteCourse.close();
            }

            // Finally close connection
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());

        }
    }

    public boolean addTeacher(String code, String name, String instrument, String contact) throws Exception {
        boolean returnValue= false;
        Teacher teacher = searchTeacherByCode(code);
        if (teacher != null) {
            throw new Exception("Cannot add this teacher. They are already in the database");
        }
        try {
            conn.setAutoCommit(false);
            addTeacher.setString(1, code);
            addTeacher.setString(2, name);
            addTeacher.setString(3, instrument);
            addTeacher.setString(4, contact);
            int affectedRows = addTeacher.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't add the teacher");
            }
            // get generated primary key and commit change
            else {
                ResultSet generatedKey = addTeacher.getGeneratedKeys();
                if (generatedKey.next()) {
                    conn.commit();
                    returnValue = true;
                    if(DEBUG) {
                        System.out.println("Teacher added with id " + generatedKey.getInt(1));
                    }
                } else {
                    throw new SQLException("Couldn't get the generated primary key for new teacher ");
                }
            }
        } catch (SQLException e) {
            System.out.println("Problem adding new teacher " + e.getMessage());
            rollback();
        } finally {
            resetAutoCommit();
        }
        return returnValue;
    }

    public Boolean updateTeacherName(int id, String name){
        boolean returnValue=false;
        try{
            conn.setAutoCommit(false);
            updateTeacherName.setString(1,name);
            updateTeacherName.setInt(2, id);
            int affectedRows = updateTeacherName.executeUpdate();
            if(affectedRows != 1){
                throw new SQLException("Cannot update teacher name");
            }else{
                conn.commit();
                returnValue = true;
            }
        }catch(SQLException e){
            if(DEBUG) {
                System.out.println("Error in updateTeacherName " + e.getMessage());
            }
            rollback();
        }
        finally {
            resetAutoCommit();
        }
        return returnValue;
    }

    public Boolean updateTeacherContact(int id, String contact){
        boolean retrnValue = false;
        try {
            conn.setAutoCommit(false);
            updateTeacherContact.setString(1, contact);
            updateTeacherContact.setInt(2, id);
            int affectedRows = updateTeacherContact.executeUpdate();
            if(affectedRows != 1){
                throw new SQLException("update execution failed");
            }
            else{
                conn.commit();
                retrnValue = true;
            }
        }catch(SQLException e){
            if(DEBUG){System.out.println("cannot update teacher contact");}
            rollback();
        }
        finally {
            resetAutoCommit();
        }
        return retrnValue;
    }

    public boolean updateTeacherInstrument(int id, String instrument){
        boolean retrnValue = false;
        try {
            conn.setAutoCommit(false);
            updateTeacherInstrument.setString(1, instrument);
            updateTeacherInstrument.setInt(2, id);
            int affectedRows = updateTeacherInstrument.executeUpdate();
            if(affectedRows != 1){
                throw new SQLException("update execution failed");
            }
            else{
                conn.commit();
                retrnValue = true;
            }
        }catch(SQLException e){
            if(DEBUG){System.out.println("cannot update teacher instrument");}
            rollback();
        }
        finally {
            resetAutoCommit();
        }
        return retrnValue;
    }

    private boolean teacherExists(String code) {
        try {
            queryTeacherByCode.setString(1, code);
            ResultSet result = queryTeacherByCode.executeQuery();
            if (result.next()) {
                String name = result.getString(INDEX_TEACHER_NAME);
                return true;
            } else {
                if(DEBUG){System.out.println("Couldn't find teacher with that code ");}
                return false;
            }
        } catch (SQLException e) {
            if(DEBUG){System.out.println("teacherExists() Query failed " + e.getMessage());}
            return false;
        }
    }

    public Teacher searchTeacherByCode(String code) {
        Teacher teacher = new Teacher();
        try{
            queryTeacherByCode.setString(1, code);
            ResultSet result = queryTeacherByCode.executeQuery();
            if(result.next()){
                teacher.setId(result.getInt(INDEX_TEACHER_ID));
                teacher.setCode(result.getString(INDEX_TEACHER_CODE));
                teacher.setName(result.getString(INDEX_TEACHER_NAME));
                teacher.setInstrument(result.getString(INDEX_TEACHER_INSTRUMENT));
                teacher.setContact(result.getString(INDEX_TEACHER_CONTACT));
            }else{
                teacher = null;
            }
        }catch(SQLException e){
            if(DEBUG) {System.out.println("searchTeacherByCode(): "+ e.getMessage());}
            teacher = null;
        }
        return teacher;
    }

    public Teacher searchTeacherByName(String name) {
        Teacher teacher = new Teacher();
        try {
            queryTeacherByName.setString(1, name);
            ResultSet result = queryTeacherByName.executeQuery();
            if (result.next()) {
                teacher.setId(result.getInt(INDEX_TEACHER_ID));
                teacher.setCode(result.getString(INDEX_TEACHER_CODE));
                teacher.setName(result.getString(INDEX_TEACHER_NAME));
                teacher.setInstrument(result.getString(INDEX_TEACHER_INSTRUMENT));
                teacher.setContact(result.getString(INDEX_TEACHER_CONTACT));
            }else{
                teacher = null;
            }
        } catch (SQLException e) {
            if(DEBUG) {System.out.println("Error in queryTeacherId " + e.getMessage());}
            teacher = null;
        }
        return teacher;
    }

    public boolean addStudent(String name, String contact, String instrument, String level) throws Exception {
        boolean returnValue = false;

        if (isStudentRegisteredForCourse(name, instrument, level)) {
            throw new Exception("This student is already registered for this course ");
        }

        String courseCode = queryCourseCode(instrument, level);
        if (courseCode == null) {
            throw new Exception("Can't find that course ");
        }
        try {
            conn.setAutoCommit(false);
            addStudent.setString(1, name);
            addStudent.setString(2, contact);
            addStudent.setString(3, courseCode);
            int affectedRows = addStudent.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Error in addStudent.executeUpdate()");
            } else {
                System.out.println("Getting generated key");
                ResultSet generatedKeys = addStudent.getGeneratedKeys(); // get key. only expect one key to be returned
                if (generatedKeys.next()) {
                    conn.commit();
                    System.out.println("Student: " + name + " added with id = " + generatedKeys.getInt(1));
                    returnValue = true;
                } else {
                    throw new SQLException("Couldn't get _id for student ");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding new student: " + e.getMessage());
            e.printStackTrace();
            rollback();

        } finally {
            resetAutoCommit();
        }
        return returnValue;
    }

    public boolean addCourse(String instrument, String level, String code, String teacher_code) throws Exception {
        boolean returnValue = false;
        int courseId = queryCourseId(instrument, level);
        if (courseId != 0) {
            System.out.println("course with Id " + courseId + " already exists");
            return returnValue;
        } else {
            if (!teacherExists(teacher_code)) {
                System.out.println("There's no teacher to teach that course. You'll have to find a teacher first");
                return returnValue;
            }
        }
        try {
            conn.setAutoCommit(false);
            addCourse.setString(1, code);
            addCourse.setString(2, instrument);
            addCourse.setString(3, level);
            addCourse.setString(4, teacher_code);
            int affectedRows = addCourse.executeUpdate();
            if (affectedRows != 1) {
                System.out.println("Couldn't add new course ");
                throw new SQLException("Error in adding new course ");
            }
            // Get the Primary Key
            else {
                System.out.println("Getting generated primary key for new course");
                ResultSet generatedKeys = addCourse.getGeneratedKeys();
                if (generatedKeys.next()) {
                    conn.commit();
                    System.out.println("Added new course with primary key " + generatedKeys.getInt(1));
                    returnValue = true;
                } else {
                    throw new SQLException("Couldn't get Primary Key for new course");
                }
            }
        } catch (SQLException e) {
            rollback();
        } finally {
            resetAutoCommit();
        }
        return returnValue;
    }

    public boolean deleteTeacher(String code) throws Exception {
        boolean returnValue = false;
        Teacher teacher = searchTeacherByCode(code);   // make sure they're in the DB
        if(teacher != null) {
            List<Course> courses = getCoursesForTeacher(code); // Can't delete if they're teaching a course.
            if (courses == null) {
                returnValue= deleteTeacher(teacher.getId());
            } else {
                throw new Exception("Cannnot delete: This teacher teaches one or more courses.");
            }
        }else{
            throw new Exception("This teacher doesn't exist in the DB");
        }
        return returnValue;
    }

    private boolean deleteTeacher(int id) {
        boolean rtrnValue=false;
        try {
            conn.setAutoCommit(false);
            deleteTeacher.setInt(1, id);
            int affectedRows = deleteTeacher.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Error in deleting teacher from DB");
            } else {
                System.out.println("teacher " + id + " removed ");
                conn.commit();
                rtrnValue = true;
            }
        } catch (SQLException e) {
            if(DEBUG){System.out.println("deleteTeacher() Error " + e.getMessage());}
            rollback();
        } finally {
            resetAutoCommit();
        }
        return rtrnValue;
    }

    public boolean deleteCourse(String instrument, String level) {
        boolean returnValue = false;
        int courseId = queryCourseId(instrument, level);
        if (courseId == 0) {
            System.out.println("deleteCourse(): Course you want to delete cannot be found ");
            return returnValue;
        }
        try {
            conn.setAutoCommit(false);
            deleteCourse.setInt(1, courseId);

            int affectedRows = deleteCourse.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Error deleting course ");
            } else {
                conn.commit();
                returnValue = true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't delete course " + e.getMessage());
            rollback();
        } finally {
            resetAutoCommit();
        }
        return returnValue;
    }

    public boolean deleteStudent(String name, String instrument, String level) throws Exception {
        String course = queryCourseCode(instrument, level);
        return (deleteStudent(name, course));
    }

    private boolean deleteStudent(String name, String course) throws SQLException {
        int studentId = getStudentId(name, course);
        return (deleteStudent(studentId));
    }

    public boolean deleteStudent(int id) throws SQLException {
        boolean result = false;
        try {
            conn.setAutoCommit(false);
            deleteStudent.setInt(1, id);
            int rowsAffected = deleteStudent.executeUpdate();
            if (rowsAffected != 1) {
                rollback();
                throw new SQLException("Failed to remove student from database");
            } else {
                conn.commit();
                result = true;
                System.out.println(" Student with id " + id + " was removed from database");
            }
        } finally {
            resetAutoCommit();
        }
        return result;
    }

    private int getStudentId(String name, String course) {
        String studentSearch = "SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_NAME + " = " + "\"" + name + "\"" + " AND " +
                COLUMN_STUDENT_COURSE + " = " + "\"" + course + "\"";
        int studentId = 0;
        try {

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(studentSearch);
            while (result.next()) {
                studentId = result.getInt(INDEX_STUDENT_ID);
            }
            return studentId;
        } catch (SQLException e) {
            System.out.println("Couldn't find a student by that name " + e.getMessage());
            return 0;
        }
    }

    public List<Course> getCoursesForTeacher(String teacher_code) {
        List<Course> courses = new ArrayList<>();
        try {
            queryCoursesByTeacher.setString(1, teacher_code);
            ResultSet result = queryCoursesByTeacher.executeQuery();
            if (result.next()) {
                while (result.next()) {
                    Course course = new Course();
                    course.setCourseId(result.getInt(INDEX_COURSE_ID));
                    course.setCode(result.getString(INDEX_COURSE_CODE));
                    course.setInstrument(result.getString(INDEX_COURSE_INSTRUMENT));
                    course.setLevel(result.getString(INDEX_COURSE_LEVEL));
                    course.setTeacherCode(result.getString(INDEX_COURSE_TEACHER_CODE));
                    courses.add(course);
                }
                return courses;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("getCoursesForTeacher(); Error in query courses by teacher " + e.getMessage());
            return null;
        }
    }

    public boolean updateStudentCourse(int studentId, String instrument, String level) throws Exception {
        boolean taskOk = false;
        String course = queryCourseCode(instrument, level);
        try {
            conn.setAutoCommit(false);
            updateStudentCourse.setString(1, course);
            updateStudentCourse.setInt(2, studentId);
            int affectedRows = updateStudentCourse.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Error in update of student course");
            } else {
                conn.commit();
                taskOk =true;
            }

        } catch (SQLException e) {
            System.out.println("updateStudentCourse(): Cannot update course for this student " + e.getMessage());
            rollback();
        } finally {
            resetAutoCommit();
        }
        return taskOk;
    }

    public boolean updateStudentContact(int studentId, String newContact) throws Exception{
        boolean taskOk = false;
        try{
            conn.setAutoCommit(false);
            updateStudentContact.setString(1, newContact);
            updateStudentContact.setInt(2,studentId);
            int affectedRows = updateStudentContact.executeUpdate();
            if(affectedRows != 1){
                throw new SQLException("Error in updating Student Name");
            }else{
                conn.commit();
                taskOk = true;
            }
        }catch(SQLException e){
            rollback();
            throw new Exception("Failed to update Student Contact");  // throws this back to the controller
        } finally {
            resetAutoCommit();
        }
        return taskOk;
    }

    public boolean updateStudentName(int studentId, String newName) throws Exception{
        boolean taskOK=false;
        try{
            conn.setAutoCommit(false);
            updateStudentName.setString(1, newName);
            updateStudentName.setInt(2,studentId);

            int affectedRows = updateStudentName.executeUpdate();
            System.out.println("updateStudentName(): Id: "+studentId+" new name: "+newName + "affected rows: "+affectedRows);
            if(affectedRows != 1){
                throw new SQLException("Error in updating Student Name");
            }else{
                conn.commit();
                taskOK = true;
            }
        }catch(SQLException e){
            rollback();
            throw new Exception("Failed to update Student Name");  // throws this back to the controller
        } finally {
            resetAutoCommit();
        }
        return taskOK;
    }

    public boolean updateCourseTeacher(String courseCode, String teacherCode) {
        boolean returnValue = false;

        if(!teacherExists(teacherCode)){
            System.out.println("Teacher with that code does not exist in the database. Check teachers and try again. ");
            return returnValue;
        }

        try {
            conn.setAutoCommit(false);
            updateCourseTeacher.setString(1, teacherCode);
            updateCourseTeacher.setString(2, courseCode);
            int affectedRows = updateCourseTeacher.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Course Update error");
            } else {
                System.out.println("course " + courseCode + " updated with teacher " + teacherCode);
                conn.commit();
                returnValue = true;
            }
        } catch (SQLException e) {
            System.out.println("updateCourseTeacher() error " + e.getMessage());
            rollback();
        } finally {
            resetAutoCommit();
        }
        return returnValue;
    }

    public Course searchCourseByCode(String code){
        Course course = new Course();
        try {
            queryCourseByCode.setString(1, code);
            ResultSet result = queryCourseByCode.executeQuery();
            if(result.next()){
                course.setCourseId(result.getInt(INDEX_COURSE_ID));
                course.setCode(result.getString(INDEX_COURSE_CODE));
                course.setInstrument(result.getString(INDEX_COURSE_INSTRUMENT));
                course.setLevel(result.getString(INDEX_COURSE_LEVEL));
                course.setTeacherCode(result.getString(INDEX_COURSE_TEACHER_CODE));
            }else{
                course = null;
            }
        }catch(SQLException e){
            if(DEBUG){ System.out.println("Error in searchCourseByCode()"+ e.getMessage()); }
            course = null;
        }
        return course;
    }

    public String queryCourseInstrument(String code) throws SQLException {
        String instrument = null;
        queryCourseInstrument.setString(1, code);
        ResultSet result = queryCourseInstrument.executeQuery();
        while (result.next()) {
            instrument = result.getString(1);
        }
        return instrument;
    }

    public String queryCourseLevel(String code) throws SQLException {
        String level = null;
        queryCourseLevel.setString(1, code);
        ResultSet result = queryCourseLevel.executeQuery();
        while (result.next()) {
            level = result.getString(1);
        }
        return level;
    }

    public String queryCourseCode(String instrument, String level) throws Exception{
        try {
            queryCourseCode.setString(1, instrument);
            queryCourseCode.setString(2, level);
            String courseCode = null;
            ResultSet result = queryCourseCode.executeQuery();

            while (result.next()) {
                courseCode = result.getString(1);
            }
            if (courseCode == null) {
                System.out.println("queryCourseCode(): Couldn't find that course ");
                throw new Exception("The course you're looking for doesn't exist");
            }
            return courseCode;
        } catch (SQLException e) {
            System.out.println("queryCourseCode():  " + e.getMessage());
            return null;
        }
    }

    private int queryCourseId(String instrument, String level)  {
        int courseId = 0;
        try {
            queryCourseId.setString(1, instrument);
            queryCourseId.setString(2, level);
            ResultSet result = queryCourseId.executeQuery();

            if (result.next()) {
                courseId = result.getInt(1);
            } else {
                System.out.println("queryCourseId(): Couldn't find that course ");
            }

        } catch (SQLException e) {
            System.out.println("queryCourseId():  " + e.getMessage());
            return courseId;
        }
        return courseId;
    }

    public boolean isStudentRegisteredForCourse(String studentName, String course) {
        List<Student> students = getStudents();
        if (students.isEmpty()) {
            return false;
        }

        boolean isRegisteredForCourse = false;

        for (Student registeredStudent : students) {
            if ((registeredStudent.getName().equalsIgnoreCase(studentName)) && (registeredStudent.getCourse().equalsIgnoreCase(course))) {
                System.out.println("Found " + registeredStudent.getName() + " is already registered for course with code " + registeredStudent.getCourse());
                isRegisteredForCourse = true;
            }
        }
        return isRegisteredForCourse;
    }

    public boolean isStudentRegisteredForCourse(String name, String instrument, String level) throws Exception {
        String courseCode = queryCourseCode(instrument, level);
        return (isStudentRegisteredForCourse(name, courseCode));
    }

    // NOTE: Clients should check for null return value
    public List<Course> getCourses() {
        try {
            ResultSet results = queryAllCourses.executeQuery();

            List<Course> courses = new ArrayList<>();
            while (results.next()) {
                Course course = new Course();
                course.setCourseId(results.getInt(INDEX_COURSE_ID));
                course.setCode(results.getString(INDEX_COURSE_CODE));
                course.setInstrument(results.getString(INDEX_COURSE_INSTRUMENT));
                course.setLevel(results.getString(INDEX_COURSE_LEVEL));
                course.setTeacherCode(results.getString(INDEX_COURSE_TEACHER_CODE));
                courses.add(course);
            }
            return courses;

        } catch (SQLException e) {
            System.out.println("SQL Error reading courses table " + e.getMessage());
            return null;
        }
    }

    public List<Student> getStudents() {
        try {
            ResultSet results = queryAllStudents.executeQuery();
            List<Student> students = new ArrayList<>();
            while (results.next()) {
                Student student = new Student();
                student.setId(results.getInt(INDEX_STUDENT_ID));
                student.setName(results.getString(INDEX_STUDENT_NAME));
                student.setContact(results.getString(INDEX_STUDENT_CONTACT));
                student.setCourse(results.getString(INDEX_STUDENT_COURSE));
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            System.out.println("Error reading students table " + e.getMessage());
            return null;
        }
    }

    public List<Teacher> getTeachers() {

        try {
            ResultSet results = queryAllTeachers.executeQuery();
            List<Teacher> teachers = new ArrayList<>();
            while (results.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(results.getInt(INDEX_TEACHER_ID));
                teacher.setCode(results.getString(INDEX_TEACHER_CODE));
                teacher.setName(results.getString(INDEX_TEACHER_NAME));
                teacher.setInstrument(results.getString(INDEX_TEACHER_INSTRUMENT));
                teacher.setContact(results.getString(INDEX_TEACHER_CONTACT));
                teachers.add(teacher);
            }

            return teachers;
        } catch (SQLException e) {
            System.out.println("Error in query of teachers " + e.getMessage());
            return null;
        }
    }

    private boolean createViewOfStudents() {
        try (Statement statement = conn.createStatement()) {
            statement.execute(CREATE_STUDENTS_COURSES_VIEW);
            return true;
        } catch (SQLException e) {
            System.out.println("Error creating view of students : " + e.getMessage());
            return false;
        }
    }

    public List<StudentFile> getStudentsByInstrument(String instrument) {
        // search the view using 'instrument' parameter
        try {
            getStudentsByInstrument.setString(1, instrument);
            ResultSet results = getStudentsByInstrument.executeQuery();
            List<StudentFile> studentFiles = new ArrayList<>();
            while (results.next()) {
                StudentFile file = new StudentFile();
                file.setStudentName(results.getString(1));
                file.setContact(results.getString(2));
                file.setInstrument(results.getString(3));
                file.setLevel(results.getString(4));
                file.setTeacher(results.getString(5));
                studentFiles.add(file);
            }
            return studentFiles;
        } catch (SQLException e) {
            System.out.println("Error in SQL query " + e.getMessage());
            return null;
        }
    }

    private void rollback() {
        try {
            System.out.println("Performing rollback ");
            conn.rollback();
        } catch (SQLException e2x) {
            System.out.println("Exception in rolling back!!! " + e2x.getMessage());
        }
    }

    private void resetAutoCommit() {
        try {
            // Always reset AutoCommit to true
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Couldn't reset AutoCommit to true ");
        }
    }
}