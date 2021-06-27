package Server.fileWorker;

import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import general.*;
import exceptions.*;
import validation.InputChecker;
import validation.StudyGroupBuilder;
import validation.StudyGroupBuilderImpl;
import validation.StudyGroupValidatorImpl;

import java.io.*;
import java.util.Scanner;

public class CSVStudyGroupReader implements StudyGroupReader, IOimpl {
    private final CollectionManager manager;
    private String filePath;
    private String separator;
    private BufferedReader bufferedReader;

    public CSVStudyGroupReader(CollectionManager collectionManager) {
        manager = collectionManager;
    }

    /**
     * Парсит из массива строк в элемент коллекции
     *
     * @param values массив полей коллекции в строчном представлении
     * @return Эллемент коллекции
     */
    @Override
    public ServerStudyGroup read(String[] values) {
        StudyGroupBuilder studyGroupBuilder = new StudyGroupBuilderImpl(bufferedReader, false, new StudyGroupValidatorImpl());
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
            if (values[i].isEmpty()) {
                println("You has empty field in the file!");
                return null;
            }
        }
        try {
            studyGroupBuilder.setName(values[0]);

            if (InputChecker.checkInt(values[1]))
                studyGroupBuilder.setCoordinateX(Integer.parseInt(values[1]));
            else
                throw new InvalidFieldException("Coordinate X should be int");
            if (InputChecker.checkLong(values[2]))
                studyGroupBuilder.setCoordinateY(Long.parseLong(values[2]));
            else
                throw new InvalidFieldException("Coordinate Y should be Long");
            if (InputChecker.checkInt(values[3]))
                studyGroupBuilder.setStudentsCount(Integer.parseInt(values[3]));
            else
                throw new InvalidFieldException("Students count should be int and greater than 0");
            try {
                studyGroupBuilder.setFormOfEducation(studyGroupBuilder.checkFormOfEducation(values[4]));
                studyGroupBuilder.setSemester(studyGroupBuilder.checkSemester(values[5]));
            } catch (EnumNotFoundException e) {
                System.out.println(e.getMessage());
            }
            studyGroupBuilder.setGAName(values[6]);
            studyGroupBuilder.setGAPassportID(values[7]);
            if (InputChecker.checkLong(values[8]))
                studyGroupBuilder.setGALocationX(Long.parseLong(values[8]));
            else
                throw new InvalidFieldException("Group Admin Location X should be long");
            if (InputChecker.checkLong(values[9]))
                studyGroupBuilder.setGALocationY(Long.parseLong(values[9]));
            else
                throw new InvalidFieldException("Group Admin Location Y should be Long");
            if (InputChecker.checkLong(values[10]))
                studyGroupBuilder.setGALocationZ(Long.parseLong(values[10]));
            else
                throw new InvalidFieldException("Group Admin Location Z should be Long");
            studyGroupBuilder.setGALocationName(values[11]);
        } catch (InvalidFieldException e) {
            println(e.getMessage());
            return null;
        }
        return manager.getServerStudyGroup(studyGroupBuilder.getStudyGroup());
    }

    /**
     * Читает построчно файл csv и добавляет элементы в коллекцию по одному
     */
    @Override
    public void loadInput() {
        setSep(",");
        try {
            bufferedReader = new BufferedReader(new FileReader(getFilePath().trim()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lines = line.split(getSep());
                if (lines.length != 12 && lines.length != 14) {
                    println("You have the wrong number of fields, element will not added to the collection");
                    continue;
                }
                ServerStudyGroup studyGroup = read(lines);
                if (studyGroup != null) {
                    println("Element " + studyGroup.getName() + " is successfully added to the collection");
                    manager.getStudyGroups().add(studyGroup);
                } else {
                    println("StudyGroup instance is not added to the collection");
                }
            }
        } catch (IOException e) {
            println("Incorrect file path\nFix the file");
        }
    }

    @Override
    public void inputFilePath() {
        println("Enter the path to file:");
        Scanner scan = new Scanner(System.in);
        filePath = scan.nextLine().trim();
        File file = new File(filePath);
        while (!file.exists() || file.isDirectory() || !file.canRead()) {
            if (!file.exists())
                println("File isn't exist, please try again:");
            else if (!file.canRead())
                println("Permission denied! File can't be read, please try again:");
            else
                println("This is the Directory! Please try again:");
            filePath = scan.nextLine();
            file = new File(filePath.trim());
        }
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            println("File path is incorrect, please try again");
            return;
        }
        println("The path to the file is successfully entered");
    }

    @Override
    public String getSep() {
        return separator;
    }

    @Override
    public void setSep(String separator) {
        this.separator = separator;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String fileName) throws UnsupportedFileException {
        File file = new File(fileName.trim());
        if (file.exists() && !file.isDirectory() && file.canRead())
            filePath = fileName;
        else {
            String message;
            if (!file.exists())
                message = "File isn't exist";
            else if (!file.canRead())
                message = "Permission denied! File can't be read";
            else
                message = "This is the directory";
            throw new UnsupportedFileException(message);
        }
    }

}
