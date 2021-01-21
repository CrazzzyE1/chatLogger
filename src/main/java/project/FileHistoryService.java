package project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class FileHistoryService implements HistoryService {

    private static FileHistoryService instanse;
    private String path = "C:\\Program Project\\GeekBrainsEducation2\\JAVA2\\src\\company\\lesson7\\history.txt";

    private FileHistoryService() {

    }

    public static FileHistoryService getInstance() {
        return instanse == null ?
                new FileHistoryService() : instanse;
    }


    @Override
    public void save(List<String> chat) {
        try {
            Files.delete(Paths.get(path));
            Files.write(Paths.get(path), chat, StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> load() {
        try {
            return Files.newBufferedReader(Paths.get(path))
                    .lines()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("File not found");
        }
    }
}