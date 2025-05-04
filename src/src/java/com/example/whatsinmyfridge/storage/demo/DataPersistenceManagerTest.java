package com.example.whatsinmyfridge.storage.demo;

import com.example.whatsinmyfridge.storage.DataPersistenceManager;
import com.example.whatsinmyfridge.storage.data.FoodItem;
import com.example.whatsinmyfridge.utils.FoodNameGenerator;

import java.util.HashMap;
import java.util.Scanner;

public class DataPersistenceManagerTest {

    private static final HashMap<String, Runnable> commands = new HashMap<>();

    public static void main(String[] args) {
        DataPersistenceManager.initializeStaticInstance();
        DataPersistenceManager manager = DataPersistenceManager.instance;

        DemoImplementor useScript = new DemoImplementor();
        manager.RegisterPersistenceObject(useScript);

        manager.initialize();

        Scanner scanner = new Scanner(System.in);

        commands.put("save", manager::SaveData);
        commands.put("load", manager::LoadData);
        commands.put("writef", () ->
        {
            FoodItem item = new FoodItem(FoodNameGenerator.getRandomFoodName(), new java.util.Random().nextInt(100));
            //useScript.WriteNew(item);
        });
        commands.put("help", () -> {
            System.out.println("Available commands:");
            commands.keySet().forEach(System.out::println);
        });
        commands.put("exit", () -> {
            System.exit(0);
        });

        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (commands.containsKey(input)) {
                commands.get(input).run();
            } else {
                System.out.println("Unknown.");
            }
        }
    }
}
