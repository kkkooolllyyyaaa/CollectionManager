package Server.datebase;

import Server.collection.ServerStudyGroup;
import exceptions.SQLNoDataException;
import general.User;

import java.util.concurrent.CopyOnWriteArrayList;

public interface StudyGroupDataBase {
    ServerStudyGroup getStudyGroup(long id) throws SQLNoDataException;

    int getNextId() throws SQLNoDataException;

    void insertStudyGroup(ServerStudyGroup studyGroup) throws Exception;

    boolean deleteStudyGroups();

    boolean deleteStudyGroupsOfUser(User user) throws SQLNoDataException;

    boolean deleteStudyGroupById(long id);

    void updateStudyGroup(long id, ServerStudyGroup studyGroup) throws SQLNoDataException;

    CopyOnWriteArrayList<ServerStudyGroup> getAll() throws SQLNoDataException;
}
