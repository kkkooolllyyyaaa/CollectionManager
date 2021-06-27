package Server.fileWorker;

import Server.collection.CollectionManager;
import Server.collection.ServerStudyGroup;
import general.*;
import exceptions.*;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Основной класс для работы с csv файлом
 */
public class CSVStudyGroupWriter implements StudyGroupWriter, IOimpl {
    private final CollectionManager manager;
    private String filePath;
    private String separator;
    private BufferedReader bufferedReader;

    public CSVStudyGroupWriter(CollectionManager collectionManager) {
        manager = collectionManager;
    }

    /**
     * Вспомогательный метод, реализующий ввод пути на считываемый csv файл.
     */
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

    /**
     * Метод, обеспечивающий запись в файл, использую BufferedWriter
     */
    @Override
    public void write() {
        try {
            String[] str = parseToString(manager.getStudyGroups());
            String saveFilePath = System.getenv("path");
            File file;
            if (saveFilePath == null) {
                println("The path in the environment variable is incorrect");
                inputFilePath();
                saveFilePath = getFilePath();
                while (!(file = new File(saveFilePath)).canWrite()) {
                    println("Permission denied! Unable to write to this file, please try again:");
                    inputFilePath();
                    saveFilePath = getFilePath();
                }
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFilePath));
            for (String s : str) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Получить значения разделителя, который используется для CSV файла.
     *
     * @return separator разделитель.
     */
    @Override
    public String getSep() {
        return separator;
    }

    /**
     * Установить значение разделителя, которое будет использоваться для CSV файла.
     *
     * @param separator разделитель.
     */
    @Override
    public void setSep(String separator) {
        this.separator = separator;
    }

    /**
     * @return filePath
     */
    @Override
    public String getFilePath() {
        return filePath;
    }

    /**
     * Метод, обеспечивающий за строкове представление элементов коллекции в csv файле
     *
     * @param linkedList Коллекция
     * @return Строковое представление
     */
    private String[] parseToString(LinkedList<ServerStudyGroup> linkedList) {
        String[] toSave = new String[linkedList.size()];
        int cnt = 0;
        for (ServerStudyGroup sg : linkedList) {
            toSave[cnt] = sg.getName();
            toSave[cnt] += ',' + String.valueOf(sg.getCoordinates().getX());
            toSave[cnt] += ',' + String.valueOf(sg.getCoordinates().getY());
            toSave[cnt] += ',' + String.valueOf(sg.getStudentsCount());
            toSave[cnt] += ',' + sg.getFormOfEducation().getUrl();
            toSave[cnt] += ',' + sg.getSemesterEnum().getUrl();
            toSave[cnt] += ',' + sg.getGroupAdmin().getName();
            toSave[cnt] += ',' + sg.getGroupAdmin().getPassportID();
            toSave[cnt] += ',' + String.valueOf(sg.getGroupAdmin().getLocation().getX());
            toSave[cnt] += ',' + String.valueOf(sg.getGroupAdmin().getLocation().getY());
            toSave[cnt] += ',' + String.valueOf(sg.getGroupAdmin().getLocation().getZ());
            toSave[cnt] += ',' + sg.getGroupAdmin().getLocation().getName();
            toSave[cnt] += ',' + String.valueOf(sg.getId());
            toSave[cnt] += ',' + sg.getCreationDate().toString();
            cnt++;
        }
        return toSave;
    }
}