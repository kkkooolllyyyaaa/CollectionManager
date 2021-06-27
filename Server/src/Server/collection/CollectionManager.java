package Server.collection;


import Server.connection.response.ResponseCreator;
import exceptions.NotOwnerException;
import general.Semester;
import general.StudyGroup;

import java.time.ZonedDateTime;
import java.util.LinkedList;

public interface CollectionManager {
    void clear(String username);

    void removeGreater(ServerStudyGroup studyGroup, String username);

    void removeFirstElement(String username);

    void removeById(long id, String username) throws NotOwnerException;

    void update(long id, ServerStudyGroup studyGroup);

    void sumOfStudentsCount();

    void countLessThanSemesterEnum(Semester semester);

    void addElement(ServerStudyGroup studyGroup);

    void info();

    ZonedDateTime getInitializationTime();

    boolean containsId(long id);

    LinkedList<ServerStudyGroup> getStudyGroups();

    ServerStudyGroup getServerStudyGroup(StudyGroup studyGroup);

    ResponseCreator getResponseCreator();

    LinkedList<ServerStudyGroup> getStudyGroupsSortedByUsername();


}
