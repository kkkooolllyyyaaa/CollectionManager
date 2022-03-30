package validation;


import exceptions.EnumNotFoundException;
import exceptions.InvalidFieldException;
import general.FormOfEducation;
import general.Location;
import general.Semester;
import general.StudyGroup;

import java.time.ZonedDateTime;

public interface StudyGroupBuilder {
    void setName(String name) throws InvalidFieldException;

    void setCoordinateX(int x) throws InvalidFieldException;

    void setCoordinateY(Long y) throws InvalidFieldException;

    void setStudentsCount(int studentsCount) throws InvalidFieldException;

    void setFormOfEducation(FormOfEducation formOfEducation) throws InvalidFieldException;

    void setSemester(Semester semester) throws InvalidFieldException;

    void setGAName(String name) throws InvalidFieldException;

    void setGAPassportID(String passportID) throws InvalidFieldException;

    void setGALocation(Location location);

    void setGALocationX(long x);

    void setGALocationY(Long y) throws InvalidFieldException;

    void setGALocationZ(Long z) throws InvalidFieldException;

    void setGALocationName(String name) throws InvalidFieldException;

    void setCreationDate();

    void setCreationDate(ZonedDateTime creationDate) throws InvalidFieldException;

    void setUsername(String username) throws InvalidFieldException;

    StudyGroup getStudyGroup();

    Semester checkSemester(String str) throws InvalidFieldException, EnumNotFoundException;

    FormOfEducation checkFormOfEducation(String str) throws InvalidFieldException, EnumNotFoundException;

    Long askStudyGroupId();

    void inputFieldsFile();

    void askName();

    void askCoordinateX();

    void askCoordinateY();

    void askStudentsCount();

    void askFormOfEducation();

    void askSemester();

    void askGAName();

    void askGAPassportID();

    void askGALocationX();

    void askGALocationY();

    void askGALocationZ();

    void askGALocationName();

    void askUsername();

    StudyGroup askStudyGroup();
}
