package Server.collection;


import Server.connection.response.ResponseCreator;
import Server.datebase.StudyGroupDataBase;
import exceptions.InsertException;
import exceptions.NotOwnerException;
import exceptions.SQLNoDataException;
import general.IO;
import general.StudyGroup;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Класс который хранит коллекцию и совершает действия с ней
 */
public final class CollectionManagerImpl implements CollectionManager {
    private final ResponseCreator responseCreator;
    private final StudyGroupDataBase dataBase;
    private final HashMap<String, HashSet<Long>> owners;
    private final ReentrantLock locker;
    private final ZonedDateTime creationDate;
    private LinkedList<ServerStudyGroup> studyGroups;


    public CollectionManagerImpl(ResponseCreator responseCreator, StudyGroupDataBase dataBase) throws SQLNoDataException {
        this.responseCreator = responseCreator;
        this.dataBase = dataBase;

        owners = new HashMap<>();
        locker = new ReentrantLock();
        creationDate = ZonedDateTime.now();
        studyGroups = new LinkedList<>(dataBase.getAll());
        initOwners();
    }

    /**
     * Метод отвечающий за полную очистку коллекции
     */
    @Override
    public void clear(String username) {
        if (username == null)
            throw new RuntimeException("Non excepted error");

        locker.lock();

        if (!dataBase.deleteStudyGroupsOfUser(username))
            responseCreator.addToMsg("Не удалось очистить коллекцию");

        long cnt = studyGroups.stream().filter(x -> x.getUsername().equals(username)).count();
        studyGroups = studyGroups.stream().
                filter(x -> !x.getUsername().
                        equals(username)).collect(Collectors.toCollection(LinkedList::new));

        responseCreator.addToMsg("Коллекция очищена, всего удалено: " + cnt + " элементов");
        owners.remove(username);
        newUser(username);
        locker.unlock();
    }

    /**
     * Метод, отвечающий за подсчет количества всех студентов
     */
    @Override
    public void sumOfStudentsCount() {
        long sum = studyGroups.stream().mapToInt(ServerStudyGroup::getStudentsCount).sum();
        responseCreator.addToMsg("Всего студентов: " + sum);
    }

    /**
     * Метод, отвевающий за удаление первого элемента коллекции
     */
    @Override
    public void removeFirstElement(String username) {
        locker.lock();
        try {
            ServerStudyGroup sg = studyGroups.stream().
                    filter(x -> x.getUsername().equals(username)).findFirst().orElse(null);

            removeById(sg.getId(), username);
            owners.get(username).remove(sg.getId());
        } catch (Exception e) {
            responseCreator.addToMsg("There is no any your element");
        } finally {
            locker.unlock();
        }
    }

    /**
     * Метод, отвечающий за удаление элементов, превышающий заданный (по studentsCount)
     */
    @Override
    public void removeGreater(ServerStudyGroup studyGroup, String username) {
        int cnt = 0;

        locker.lock();

        for (ServerStudyGroup st : studyGroups) {
            if (isOwner(st.getId(), username)) {
                if (st.getStudentsCount() > studyGroup.getStudentsCount()) {
                    try {
                        removeById(st.getId(), username);
                        owners.get(username).remove(st.getId());
                        cnt++;
                    } catch (NotOwnerException unexpected) {
                        unexpected.printStackTrace();
                    }
                }
            }
        }

        locker.unlock();
        responseCreator.addToMsg(cnt + " elements removed at all");
    }

    /**
     * Метод, отвечающий за удаление элемента коллекции по id
     *
     * @param id - искомый  индентификатор
     */
    @Override
    public void removeById(long id, String username) throws NotOwnerException {
        if (!isOwner(id, username))
            throw new NotOwnerException();

        locker.lock();

        if (!dataBase.deleteStudyGroupById(id)) {
            responseCreator.addToMsg("Id is not found");
            return;
        }

        responseCreator.addToMsg("Element with id " + id + " is successfully removed from collection");
        studyGroups = studyGroups.stream()
                .filter(studyGroup -> studyGroup.getId() != id)
                .collect(Collectors.toCollection(LinkedList::new));
        owners.get(username).remove(id);
        locker.unlock();
    }

    /**
     * Метод, отвечающий за добавление элемента в коллекцию
     */
    @Override
    public void addElement(ServerStudyGroup studyGroup) throws InsertException {
        try {
            locker.lock();
            dataBase.insertStudyGroup(studyGroup);
            studyGroups.add(studyGroup);
            newUser(studyGroup.getUsername());
            owners.get(studyGroup.getUsername()).add(studyGroup.getId());
        } catch (Exception e) {
            throw new InsertException("Не удалось добавить элемент: " + e.getMessage());
        } finally {
            locker.unlock();
        }
    }

    /**
     * @param id         айди группы, по которому произойдет обноваление
     * @param studyGroup обновленный экземпляр
     */
    @Override
    public void update(long id, ServerStudyGroup studyGroup) {
        ServerStudyGroup sg =
                studyGroups.stream().filter(x -> x.getId() == id && isOwner(id, x.getUsername())).
                        findFirst().orElse(null);

        if (sg == null) {
            responseCreator.addToMsg("Input id is not found!");
            return;
        }

        try {
            locker.lock();
            dataBase.updateStudyGroup(id, studyGroup);
            sg.setName(studyGroup.getName());
            sg.setCoordinates(studyGroup.getCoordinates());
            sg.setFormOfEducation(studyGroup.getFormOfEducation());
            sg.setGroupAdmin(studyGroup.getGroupAdmin());
            sg.setStudentsCount(studyGroup.getStudentsCount());
            sg.setSemesterEnum(studyGroup.getSemesterEnum());
            responseCreator.addToMsg("Element is updated!");
        } catch (SQLNoDataException e) {
            responseCreator.addToMsg("Invalid field");
        } finally {
            locker.unlock();
        }
    }

    /**
     * Метод выводит кратку информацию о классе
     */
    @Override
    public void info() {
        String info = "Время инциализации коллекции: " + creationDate + "\n" +
                "Количество элементов в коллекции: " + studyGroups.size() + "\n" +
                "Тип коллекции: " + studyGroups.getClass().getSimpleName();
        responseCreator.addToMsg(info);
    }

    /**
     * Проверяет, есть ли элемент с данным id
     *
     * @param id - искомый
     * @return true - если элемент с данным id существует
     */
    @Override
    public boolean containsId(long id) {
        return studyGroups.stream().anyMatch(x -> x.getId() == id);
    }


    /**
     * @return получить коллекцию, отсортированную по полю id
     */
    @Override
    public LinkedList<ServerStudyGroup> getStudyGroupsSortedById() {
        sortById();
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
     * @param username - пользователь, чьи id будут получены
     * @return получить множество id, принадлежащих username
     */
    @Override
    public HashSet<Long> getIds(String username) {
        newUser(username);
        return owners.get(username);
    }

    /**
     * Получить экземпляр ServerStudyGroup, получив id с базы данных
     *
     * @param studyGroup "сырой" studyGroup
     * @return экземпляр
     */
    @Override
    public ServerStudyGroup getServerStudyGroup(StudyGroup studyGroup) {
        locker.lock();
        try {
            return new ServerStudyGroup(studyGroup, dataBase.getNextId());
        } catch (SQLNoDataException e) {
            IO.errPrint(e.getMessage());
            return null;
        } finally {
            locker.unlock();
        }
    }

    /**
     * Метод, обеспечивающий сортировку коллекции
     * использует компаратор по полю id
     */
    private void sortById() {
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
        return Comparator.comparing(ServerStudyGroup::getId);
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
        for (ServerStudyGroup s : studyGroups) {
            if (!owners.containsKey(s.getUsername()))
                newUser(s.getUsername());
            owners.get(s.getUsername()).add(s.getId());
        }
    }

    private void newUser(String username) {
        locker.lock();
        if (!owners.containsKey(username))
            owners.put(username, new HashSet<>());
        locker.unlock();
    }

    private boolean isOwner(long id, String username) {
        return owners.containsKey(username) && owners.get(username).contains(id);
    }
}
