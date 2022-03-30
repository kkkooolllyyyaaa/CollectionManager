package Server.datebase;

import Server.collection.ServerStudyGroup;
import exceptions.EnumNotFoundException;
import exceptions.InvalidFieldException;
import exceptions.SQLNoDataException;
import general.User;
import validation.StudyGroupBuilder;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostgreStudyGroupDataBase implements StudyGroupDataBase {
    private final static String URL = "jdbc:postgresql://localhost:9090/postgres";
    private final static String login = "tsypk";
    private final static String password = "kekkekkek";
    private final StudyGroupBuilder builder;

    public PostgreStudyGroupDataBase(StudyGroupBuilder builder) {
        this.builder = builder;
    }

    @Override
    public ServerStudyGroup getStudyGroup(long id) throws SQLNoDataException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM studygroups WHERE ID = ?")) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            return buildStudyGroup(rs);
        } catch (SQLException e) {
            throw new SQLNoDataException();
        }
    }

    @Override
    public int getNextId() throws SQLNoDataException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT MAX(id) FROM studygroups")) {
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            throw new SQLNoDataException();
        }
    }

    @Override
    public void insertStudyGroup(ServerStudyGroup studyGroup) throws Exception {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO studygroups VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
            initStatement(statement, studyGroup);
            statement.execute();
        } catch (SQLException sqlEx) {
            throw new Exception(sqlEx.getMessage());
        }
    }

    @Override
    public boolean deleteStudyGroups() {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("truncate studygroups")) {
            statement.execute();
            return true;
        } catch (SQLException unknownError) {
            unknownError.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStudyGroupsOfUser(User user) throws SQLNoDataException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM studygroups WHERE username = ?")) {
            statement.setString(1, user.getUserName());
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new SQLNoDataException();
        }
    }

    @Override
    public boolean deleteStudyGroupById(long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM studygroups WHERE id = ?")) {
            statement.setInt(1, (int) id);
            statement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void updateStudyGroup(long id, ServerStudyGroup studyGroup) throws SQLNoDataException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE studygroups SET " +
                     "name = ?," +
                     "coordinate_x = ?," +
                     "coordinate_y = ?," +
                     "zoned_date_time = ?," +
                     "students_count = ?," +
                     "form_of_education = ?," +
                     "semester = ?," +
                     "ga_name = ?," +
                     "ga_passport_id = ?," +
                     "ga_location_x = ?," +
                     "ga_location_y = ?," +
                     "ga_location_z = ?," +
                     "ga_location_name = ?," +
                     "username = ? " +
                     "WHERE id = ?")) {
            initStatement(statement, studyGroup);
            statement.setLong(15, id);
            statement.execute();
        } catch (SQLException e) {
            throw new SQLNoDataException();
        }
    }

    @Override
    public CopyOnWriteArrayList<ServerStudyGroup> getAll() throws SQLNoDataException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM studygroups")) {
            CopyOnWriteArrayList<ServerStudyGroup> arrayList = new CopyOnWriteArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                arrayList.add(buildStudyGroup(rs));
            }
            return arrayList;
        } catch (SQLException e) {
            throw new SQLNoDataException();
        }
    }


    private ServerStudyGroup buildStudyGroup(ResultSet rs) throws SQLException {
        try {
            builder.setName(rs.getString(1));
            builder.setCoordinateX(rs.getInt(2));
            builder.setCoordinateY(rs.getLong(3));
            builder.setCreationDate(ZonedDateTime.parse(rs.getString(4)));
            builder.setStudentsCount(rs.getInt(5));
            builder.setFormOfEducation(builder.checkFormOfEducation(rs.getString(6)));
            builder.setSemester(builder.checkSemester(rs.getString(7)));
            builder.setGAName(rs.getString(8));
            builder.setGAPassportID(rs.getString(9));
            builder.setGALocationX(rs.getInt(10));
            builder.setGALocationY(rs.getLong(11));
            builder.setGALocationZ(rs.getLong(12));
            builder.setGALocationName(rs.getString(13));
            builder.setUsername(rs.getString(14));
            return new ServerStudyGroup(builder.getStudyGroup(), rs.getInt(15));
        } catch (InvalidFieldException | EnumNotFoundException notExcepted) {
            notExcepted.printStackTrace();
        }
        return null;
    }

    private void initStatement(PreparedStatement statement, ServerStudyGroup studyGroup) throws SQLException {
        statement.setString(1, studyGroup.getName());
        statement.setLong(2, studyGroup.getCoordinates().getX());
        statement.setDouble(3, studyGroup.getCoordinates().getY());
        statement.setString(4, studyGroup.getCreationDate().toString());
        statement.setInt(5, studyGroup.getStudentsCount());
        statement.setString(6, studyGroup.getFormOfEducation().getUrl());
        statement.setString(7, studyGroup.getSemesterEnum().getUrl());
        statement.setString(8, studyGroup.getGroupAdmin().getName());
        statement.setString(9, studyGroup.getGroupAdmin().getPassportID());
        statement.setLong(10, studyGroup.getGroupAdmin().getLocation().getX());
        statement.setLong(11, studyGroup.getGroupAdmin().getLocation().getY());
        statement.setLong(12, studyGroup.getGroupAdmin().getLocation().getZ());
        statement.setString(13, studyGroup.getGroupAdmin().getLocation().getName());
        statement.setString(14, studyGroup.getUsername());
    }

    private Connection getConnection() throws SQLException {
        DataBaseConnector dataBaseConnector = new DataBaseConnector();
        dataBaseConnector.connect();
        return dataBaseConnector.getCon();
//        return DriverManager.getConnection(URL, login, password);
    }
}