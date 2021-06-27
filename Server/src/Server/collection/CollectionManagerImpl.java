package Server.collection;


import Server.connection.response.ResponseCreator;
import Server.datebase.StudyGroupDataBase;
import exceptions.NotOwnerException;
import exceptions.SQLNoDataException;
import general.Semester;
import general.StudyGroup;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Класс который хранит коллекцию и совершает действия с ней
 */
public final class CollectionManagerImpl extends AbstractCollectionManager {
    private final ZonedDateTime creationDate;
    private final ResponseCreator responseCreator;
    private final StudyGroupDataBase dataBase;
    private final HashMap<String, HashSet<Integer>> owners;
    private final ReentrantLock locker;


    public CollectionManagerImpl(ResponseCreator responseCreator, StudyGroupDataBase dataBase) {
        locker = new ReentrantLock();
        studyGroups = new LinkedList<>();
        owners = new HashMap<>();
        creationDate = ZonedDateTime.now();
        this.responseCreator = responseCreator;
        this.dataBase = dataBase;
        try {
            studyGroups.addAll(dataBase.getAll());
            initOwners();
        } catch (SQLNoDataException notExcepted) {
            notExcepted.printStackTrace();
        }
    }

    /**
     * Метод отвечающий за полную очистку коллекции
     */
    @Override
    public void clear(String username) {
        if (username == null)
            throw new RuntimeException("Non excepted error");
        int cnt = 0;
        for (ServerStudyGroup st : studyGroups) {
            if (isOwner(st.getId(), username)) {
                try {
                    removeById(st.getId(), username);
                    cnt++;
                } catch (NotOwnerException unExcepted) {
                    unExcepted.printStackTrace();
                }
            }
        }
        responseCreator.addToMsg("Коллекция очищена, всего удалено: " + cnt + " элементов");
    }

    /**
     * Метод, отвечающий за подсчет количества всех студентов
     */
    @Override
    public void sumOfStudentsCount() {
        int id = studyGroups.stream().mapToInt(ServerStudyGroup::getStudentsCount).sum();
        responseCreator.addToMsg(Integer.toString(id));
    }

    /**
     * Метод, отвевающий за удаление первого элемента коллекции
     */
    @Override
    public void removeFirstElement(String username) {
        if (owners.containsKey(username) && owners.get(username).size() > 0) {
            int firstId = owners.get(username).stream().limit(1).mapToInt(Integer1 -> Integer1).sum();
            try {
                removeById(firstId, username);
            } catch (NotOwnerException unExcepted) {
                unExcepted.printStackTrace();
                responseCreator.addToMsg(unExcepted.getMessage());
            }
        } else
            responseCreator.addToMsg("There is no any your element");
    }

    /**
     * Метод, отвечающий за удаление элементов, превышающий заданный (по studentsCount)
     */
    @Override
    public void removeGreater(ServerStudyGroup studyGroup, String username) {
        int cnt = 0;
        for (ServerStudyGroup st : studyGroups) {
            if (isOwner(st.getId(), username)) {
                if (st.getStudentsCount() > studyGroup.getStudentsCount()) {
                    try {
                        removeById(st.getId(), username);
                        cnt++;
                    } catch (NotOwnerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        responseCreator.addToMsg(cnt + " elements removed at all");
    }

    /**
     * Метод, отвечающий за удаление элемента коллекции по id
     *
     * @param id
     */
    @Override
    public void removeById(long id, String username) throws NotOwnerException {
        if (isOwner(id, username)) {
            if (dataBase.deleteStudyGroupById(id)) {
                responseCreator.addToMsg("Element " + id + " is successfully removed from collection");
                studyGroups = studyGroups.stream()
                        .filter(studyGroup -> studyGroup.getId() != id)
                        .collect(Collectors.toCollection(LinkedList::new));
            } else
                responseCreator.addToMsg("Id is not found");
        } else
            throw new NotOwnerException();
    }

    /**
     * Считает, сколько сколько групп учится в семестрах, ниже заданного
     *
     * @param semester
     */
    @Override
    public void countLessThanSemesterEnum(Semester semester) {
        int count = (int) studyGroups.stream()
                .filter(studyGroup -> studyGroup.getSemesterEnum().getValue() > semester.getValue())
                .count();
        responseCreator.addToMsg("Count of elements is: " + count);
    }

    /**
     * Метод, отвечающий за добавление элемента в коллекцию
     *
     * @param studyGroup
     */
    @Override
    public void addElement(ServerStudyGroup studyGroup) {
        try {
            //
            locker.lock();
            dataBase.insertStudyGroup(studyGroup);
            studyGroups.add(studyGroup);
            newUser(studyGroup.getUsername());
            //
            locker.unlock();
        } catch (Exception e) {
            responseCreator.addToMsg(e.getMessage());
        }
    }

    /**
     * @param id         айди группы, по которому произойдет обноваление
     * @param studyGroup обновленный экземпляр
     */
    @Override
    public void update(long id, ServerStudyGroup studyGroup) {
        for (ServerStudyGroup sg : studyGroups) {
            if (sg.getId() == id) {
                if (isOwner(id, studyGroup.getUsername())) {
                    try {
                        //
                        locker.lock();
                        dataBase.updateStudyGroup(id, studyGroup);
                        sg.setName(studyGroup.getName());
                        sg.setCoordinates(studyGroup.getCoordinates());
                        sg.setFormOfEducation(studyGroup.getFormOfEducation());
                        sg.setGroupAdmin(studyGroup.getGroupAdmin());
                        sg.setStudentsCount(studyGroup.getStudentsCount());
                        sg.setSemesterEnum(studyGroup.getSemesterEnum());
                        responseCreator.addToMsg("Element is updated!");
                        //
                        locker.unlock();
                        return;
                    } catch (SQLNoDataException e) {
                        responseCreator.addToMsg("Invalid field");
                    }
                }
            }
            break;
        }
        responseCreator.addToMsg("Input id is not found!");
    }

    /**
     * Метод выводит кратку информацию о классе
     */
    @Override
    public void info() {
        String info = "Время инциализации коллекции: " + getInitializationTime() + "\n" +
                "Количество элементов в коллекции: " + studyGroups.size() + "\n" +
                "Тип коллекции: " + studyGroups.getClass().getSimpleName();
        responseCreator.addToMsg(info);
    }

    /**
     * Метод возвращающий дату создания обьекта.
     *
     * @return дата создания обькта.
     */
    @Override
    public ZonedDateTime getInitializationTime() {
        return creationDate;
    }

    /**
     * Проверяет, есть ли элемент с данным id
     *
     * @param id
     * @return true - если элемент с данным id существует
     */
    @Override
    public boolean containsId(long id) {
        return studyGroups.stream().anyMatch(x -> x.getId() == id);
    }

    @Override
    public ResponseCreator getResponseCreator() {
        return responseCreator;
    }

    /**
     * @return получить коллекцию, отсортированную по полю id
     */
    @Override
    public LinkedList<ServerStudyGroup> getStudyGroups() {
        sort();
        return studyGroups;
    }

    /**
     * @return получить коллекцию, отсортированную по полю username
     */
    @Override
    public LinkedList<ServerStudyGroup> getStudyGroupsSortedByUsername() {
        sortByUsername();
        return studyGroups;
    }

    /**
     * Получить экземпляр ServerStudyGroup, получив id с базы данных
     *
     * @param studyGroup "сырой" studyGroup
     * @return экземпляр
     */
    @Override
    public ServerStudyGroup getServerStudyGroup(StudyGroup studyGroup) {
        try {
            return new ServerStudyGroup(studyGroup, dataBase.getNextId());
        } catch (SQLNoDataException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод, обеспечивающий сортировку коллекции
     * использует компаратор по полю id
     */
    private void sort() {
        studyGroups = studyGroups.stream()
                .sorted(getComparatorById())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Метод, обеспечивающий сортировку коллекции
     * использует компаратор по полю userName
     */
    private void sortByUsername() {
        studyGroups = studyGroups.stream()
                .sorted(getComparatorByUsername())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Получить Comparator сравнения по id
     *
     * @return компаратор
     */
    private Comparator<ServerStudyGroup> getComparatorById() {
        return (ServerStudyGroup a, ServerStudyGroup b) -> (int) (a.getId() - b.getId());
    }

    /**
     * Получить Comparator сравнения по username
     *
     * @return компаратор
     */
    private Comparator<ServerStudyGroup> getComparatorByUsername() {
        return Comparator.comparing(StudyGroup::getUsername);
    }

    private void initOwners() {
        try {
            CopyOnWriteArrayList<ServerStudyGroup> arrayList = dataBase.getAll();
            for (ServerStudyGroup s : arrayList) {
                if (owners.get(s.getUsername()) == null)
                    newUser(s.getUsername());
                owners.get(s.getUsername()).add((int) s.getId());
            }
        } catch (SQLNoDataException e) {
            e.printStackTrace();
        }
    }

    private void newUser(String username) {
        //
        locker.lock();
        if (!owners.containsKey(username))
            owners.put(username, new HashSet<>());
        //
        locker.unlock();
    }

    private boolean isOwner(long id, String username) {
        return owners.containsKey(username) && owners.get(username).contains((int) id);
    }
}