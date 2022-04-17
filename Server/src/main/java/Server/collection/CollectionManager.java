package Server.collection;


import exceptions.InsertException;
import exceptions.NotOwnerException;
import general.StudyGroup;

import java.util.HashSet;
import java.util.LinkedList;

public interface CollectionManager {
    void clear(String username);

    void removeGreater(ServerStudyGroup studyGroup, String username);

    void removeFirstElement(String username);

    void removeById(long id, String username) throws NotOwnerException;

    void update(long id, ServerStudyGroup studyGroup);

    void sumOfStudentsCount();

    void addElement(ServerStudyGroup studyGroup) throws InsertException;

    void info();

    boolean containsId(long id);

    LinkedList<ServerStudyGroup> getStudyGroupsSortedById();

    LinkedList<ServerStudyGroup> getStudyGroupsSortedByUsername();

    ServerStudyGroup getServerStudyGroup(StudyGroup studyGroup);

    HashSet<Long> getIds(String username);

}
