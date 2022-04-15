package validation;


import exceptions.EnumNotFoundException;
import exceptions.InvalidFieldException;
import general.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

/**
 * Класс, обеспечивающий ввод элементов StudyGroups
 * Обеспечивает валидность вводимых полей
 */
public class StudyGroupBuilderFunctional implements StudyGroupBuilder {

    private final boolean isScript;
    private final StudyGroupValidator validator;
    private final BufferedReader reader;
    private String line;

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int studentsCount; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле не может быть null
    private Semester semesterEnum; //Поле не может быть null
    private Person groupAdmin; //Поле может быть null
    private String username;

    public StudyGroupBuilderFunctional(BufferedReader bufferedReader, boolean isScript, StudyGroupValidator validator) {
        reader = bufferedReader;
        this.isScript = isScript;
        this.validator = validator;
    }

    @Override
    public void setName(String name) throws InvalidFieldException {
        try {
            validator.validateName(name);
        } catch (InvalidFieldException e) {
            e.printStackTrace();
        }
        this.name = name;
    }

    @Override
    public void setCoordinateX(int x) throws InvalidFieldException {
        validator.validateCoordinateX(x);
        if (coordinates == null)
            this.coordinates = new Coordinates();
        coordinates.setX(x);
    }

    @Override
    public void setCoordinateY(Long y) throws InvalidFieldException {
        validator.validateCoordinateY(y);
        if (coordinates == null)
            this.coordinates = new Coordinates();
        coordinates.setY(y);
    }

    @Override
    public void setStudentsCount(int studentsCount) throws InvalidFieldException {
        validator.validateStudentsCount(studentsCount);
        this.studentsCount = studentsCount;
    }

    @Override
    public void setFormOfEducation(FormOfEducation formOfEducation) throws InvalidFieldException {
        validator.validateFormOfEducation(formOfEducation);
        this.formOfEducation = formOfEducation;
    }

    @Override
    public void setSemester(Semester semester) throws InvalidFieldException {
        validator.validateSemester(semester);
        this.semesterEnum = semester;

    }

    @Override
    public void setGAName(String name) throws InvalidFieldException {
        validator.validateGAName(name);
        if (groupAdmin == null)
            groupAdmin = new Person();
        groupAdmin.setName(name);
    }

    @Override
    public void setGAPassportID(String passportID) throws InvalidFieldException {
        validator.validateGAPassportID(passportID);
        if (groupAdmin == null)
            groupAdmin = new Person();
        groupAdmin.setPassportID(passportID);
    }

    @Override
    public void setGALocation(Location location) {
        groupAdmin.setLocation(location);
    }

    @Override
    public void setGALocationX(long x) {
        if (groupAdmin.getLocation() == null)
            setGALocation(new Location());
        groupAdmin.getLocation().setX(x);
    }

    @Override
    public void setGALocationY(Long y) throws InvalidFieldException {
        validator.validateGALocationY(y);
        if (groupAdmin.getLocation() == null)
            setGALocation(new Location());
        groupAdmin.getLocation().setY(y);
    }

    @Override
    public void setGALocationZ(Long z) throws InvalidFieldException {
        validator.validateGALocationZ(z);
        if (groupAdmin.getLocation() == null)
            setGALocation(new Location());
        groupAdmin.getLocation().setZ(z);
    }

    @Override
    public void setGALocationName(String locationName) throws InvalidFieldException {
        validator.validateGALocationName(locationName);
        if (groupAdmin.getLocation() == null)
            groupAdmin.setLocation(new Location());
        groupAdmin.getLocation().setName(locationName);
    }

    @Override
    public void setCreationDate() {
        creationDate = ZonedDateTime.now();
    }

    @Override
    public void setCreationDate(ZonedDateTime creationDate) throws InvalidFieldException {
        if (creationDate == null)
            throw new InvalidFieldException("creation date can't be null, should be date format");
        this.creationDate = creationDate;
    }

    @Override
    public void setUsername(String username) throws InvalidFieldException {
        validator.validateUsername(username);
        this.username = username;
    }

    @Override
    public StudyGroup getStudyGroup() {
        return new StudyGroup(name, coordinates, studentsCount, formOfEducation, semesterEnum, groupAdmin, username);
    }

    /**
     * Находит enum Semester
     *
     * @param str
     * @return найденный Semester
     * @throws InvalidFieldException
     */
    @Override
    public Semester checkSemester(String str) throws InvalidFieldException, EnumNotFoundException {
        for (Semester smstr : Semester.values()) {
            if (str.equalsIgnoreCase(smstr.getUrl())) {
                validator.validateSemester(smstr);
                return smstr;
            }
        }
        throw new EnumNotFoundException("There is no enum named " + str);
    }

    /**
     * Находит enum FormOfEducation
     *
     * @param str
     * @return найденный FormOfEducation
     * @throws InvalidFieldException
     */
    @Override
    public FormOfEducation checkFormOfEducation(String str) throws InvalidFieldException, EnumNotFoundException {
        for (FormOfEducation fOE : FormOfEducation.values()) {
            if (str.equalsIgnoreCase(fOE.getUrl())) {
                return fOE;
            }
        }
        throw new EnumNotFoundException("There is no enum named " + str);
    }

    /**
     * Ввод всех полей
     */
    @Override
    public void inputFieldsFile() {
        askName();
        askCoordinateX();
        askCoordinateY();
        askStudentsCount();
        askFormOfEducation();
        askSemester();
        askGAName();
        askGAPassportID();
        askGALocationX();
        askGALocationY();
        askGALocationZ();
        askGALocationName();
    }

    /**
     * Обработка ввода Id Study Group
     *
     * @return Long id
     */
    @Override
    public Long askStudyGroupId() {
        String s;
        try {
            IO.println("Input Study group id: ");
            s = reader.readLine();
            if (InputChecker.checkLong(s.trim()))
                return Long.parseLong(s);
            else
                IO.println("Study group id can't be null; should be long");
        } catch (IOException e) {
            IO.println("Input error");
        }
        return null;
    }

    @Override
    public void askName() {
        IO.println("Input Study Group Name: ");
        try {
            setName(inputLine());
        } catch (InvalidFieldException e) {
            IO.println("Study Group Name should be String, can't be null");
            if (!isScript)
                askName();
        }
    }

    @Override
    public void askCoordinateX() {
        IO.println("Input Study Group Coordinate X: ");
        try {
            setCoordinateX(Integer.parseInt(inputLine()));
        } catch (InvalidFieldException | NumberFormatException e) {
            IO.println("Study Group Coordinate X should be int and greater than -393");
            if (!isScript)
                askCoordinateX();
        }
    }

    @Override
    public void askCoordinateY() {
        IO.println("Input Study Group Coordinate Y: ");
        try {
            setCoordinateY(Long.parseLong(inputLine()));
        } catch (InvalidFieldException | NumberFormatException e) {
            IO.println("Study Group Coordinate Y should be Long and greater than -741, can't be null");
            if (!isScript)
                askCoordinateY();
        }
    }

    @Override
    public void askStudentsCount() {
        IO.println("Input Study Group students count: ");
        try {
            setStudentsCount(Integer.parseInt(inputLine()));
        } catch (InvalidFieldException | NumberFormatException e) {
            IO.println("Study Group students count should be int and greater than 0");
            if (!isScript)
                askStudentsCount();
        }
    }

    @Override
    public void askFormOfEducation() {
        FormOfEducation.printValues();
        IO.println("Input Study Group Form Of Education: ");
        try {
            setFormOfEducation(checkFormOfEducation(inputLine()));
        } catch (InvalidFieldException e) {
            IO.println("Field Form Of Education can't be null");
            if (!isScript)
                askFormOfEducation();
        } catch (EnumNotFoundException e) {
            System.out.println(e.getMessage());
            if (!isScript)
                askFormOfEducation();
        }
    }

    @Override
    public void askSemester() {
        Semester.printValues();
        IO.println("Input Study Group Semester: ");
        try {
            setSemester(checkSemester(inputLine()));
        } catch (InvalidFieldException e) {
            IO.println("Field Semester can't be null");
            if (!isScript)
                askSemester();
        } catch (EnumNotFoundException e) {
            System.out.println(e.getMessage());
            if (!isScript)
                askSemester();
        }
    }

    @Override
    public void askGAName() {
        IO.println("Input Study Group Group Admin name: ");
        try {
            setGAName(inputLine());
        } catch (InvalidFieldException e) {
            IO.println("Study Group Group Admin name can't be void or null");
            if (!isScript)
                askGAName();
        }
    }

    @Override
    public void askGAPassportID() {
        IO.println("Input Study Group Group Admin passportID: ");
        try {
            setGAPassportID(inputLine());
        } catch (InvalidFieldException e) {
            IO.println("Study Group Group Admin passportID can't be null");
            if (!isScript)
                askGAPassportID();
        }
    }

    @Override
    public void askGALocationX() {
        IO.println("Input Study Group Group Admin Location X: ");
        try {
            setGALocationX(Long.parseLong(inputLine()));
        } catch (NumberFormatException e) {
            IO.println("Study Group Group Admin Location should be long");
            if (!isScript)
                askGALocationX();
        }

    }

    @Override
    public void askGALocationY() {
        IO.println("Input Study Group Group Admin Location Y: ");
        try {
            setGALocationY(Long.parseLong(inputLine()));
        } catch (NumberFormatException | InvalidFieldException e) {
            IO.println("Study Group Group Admin Location should be Long, can't be null");
            if (!isScript)
                askGALocationY();
        }
    }

    @Override
    public void askGALocationZ() {
        IO.println("Input Study Group Group Admin Location Z: ");
        try {
            setGALocationZ(Long.parseLong(inputLine()));
        } catch (NumberFormatException | InvalidFieldException e) {
            IO.println("Study Group Group Admin Location should be Long, can't be null");
            if (!isScript)
                askGALocationZ();
        }
    }

    @Override
    public void askGALocationName() {
        IO.println("Input Study Group Admin Location name: ");
        try {
            setGALocationName(inputLine());
        } catch (InvalidFieldException e) {
            IO.println("Study Group Group Admin Location name should be String, can't be null");
            if (!isScript)
                askGALocationName();
        }
    }

    @Override
    public void askUsername() {
        IO.println("Input username: ");
        try {
            setUsername(inputLine());
        } catch (InvalidFieldException e) {
            IO.println("username should be String, can't be null");
            if (!isScript)
                askUsername();
        }
    }

    /**
     * Обеспечивает ввод всех полей и передачу экземпляра StudyGroup
     *
     * @return studyGroup
     */
    @Override
    public StudyGroup askStudyGroup() {
        inputFieldsFile();
        return getStudyGroup();
    }

    /**
     * Прочитать строку
     *
     * @return line
     */
    private String inputLine() {
        try {
            line = reader.readLine().toLowerCase().trim();
        } catch (IOException ioException) {
            IO.println(ioException.getMessage());
        } catch (NoSuchElementException | NullPointerException e) {
            IO.errPrint("You can't input this");
            System.exit(0);
        }
        return line;
    }
}
