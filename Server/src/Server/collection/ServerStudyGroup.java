package Server.collection;


import general.StudyGroup;

public class ServerStudyGroup extends StudyGroup {
    public ServerStudyGroup(StudyGroup sg, long id) {
        super(sg.getName(), sg.getCoordinates(), sg.getStudentsCount(), sg.getFormOfEducation(), sg.getSemesterEnum(), sg.getGroupAdmin(), sg.getUsername());
        setId(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
