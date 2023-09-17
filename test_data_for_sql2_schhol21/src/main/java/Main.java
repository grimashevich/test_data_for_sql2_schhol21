import models.*;
import services.Generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main2(Peer p) {
        p.setNickName("Anton");
        System.out.println(p);
    }

    public static void main(String[] args) throws Exception {


        GenContext context = new GenContext();
        Generator generator = new Generator(context);
        generator.generate();
        System.out.println(context.getP2pList());

    }

    public static <T> void printList(List<T> list) {
        for (T item: list) {
            System.out.println(item);
        }
    }


    public static void gen_task() {
        Map<String, Integer> tasksBase = new LinkedHashMap<>();
        String csvText;
        String taskCsvFileName = getNewResourcesFileName("tasks.csv");

        tasksBase.put("C", 8);
        tasksBase.put("DO", 7);
        tasksBase.put("DOE-T", 1);
        tasksBase.put("CPP", 9);
        tasksBase.put("CPP-E", 1);
        tasksBase.put("A", 8);
        tasksBase.put("SQL", 3);
        tasksBase.put("APJ", 6);
        tasksBase.put("APE", 1);
        tasksBase.put("DB Bootcamp", 1);
        tasksBase.put("DB", 6);
        tasksBase.put("Career Track", 1);
        tasksBase.put("Internship", 1);
        tasksBase.put("BE", 6);

        csvText = gen_tasks_to_csv(tasksBase);
        if (! Files.exists(Paths.get(taskCsvFileName))) {
            writeToCsvFile(csvText, taskCsvFileName);
            return;
        }
        System.err.println("WARNING! file task.csv already exists.  Nothing has been written.");
        System.out.println(csvText);
    }

    private static String gen_tasks_to_csv(Map<String, Integer> bases) {
        StringBuilder result = new StringBuilder();
        String currentTask;
        String parentTask = "NULL";

        result.append("title,parent_task,max_xp").append(System.lineSeparator());

        for (Map.Entry<String, Integer> base: bases.entrySet()) {
            String name = base.getKey();
            int count = base.getValue();
            Random random = new Random();
            for (int i = 1; i <= count; i++) {
                currentTask = name + (base.getValue() == 1 ? "" : i);
                result.append(currentTask)
                        .append(",")
                        .append(parentTask)
                        .append(",")
                        .append(random.nextInt(28) * 50 + 100)
                        .append(System.lineSeparator());
                parentTask = currentTask;
            }
        }
        result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        return result.toString();
    }


    public static void writeToCsvFile(String text, String fileName) {
        Path path = Paths.get(fileName);
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNewResourcesFileName(String newFileName) {
        String fullPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()
                .substring(1);
        return Paths.get(fullPath).resolve("../../src/main/resources/" + newFileName).normalize().toString();
    }

}

// todo Мини-генератор CSV для тасок