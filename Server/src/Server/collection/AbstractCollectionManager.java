package Server.collection;


import java.util.LinkedList;

/**
 * Абстрактный класс Collection Manager
 * задает структуру, то что нужно реализовать
 * задает методы по управлению id
 */
public abstract class AbstractCollectionManager implements CollectionManager {
    protected LinkedList<ServerStudyGroup> studyGroups;
}
