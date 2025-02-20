package org.berneick;

import java.util.Scanner;

public class ConsoleDialogWindow {
    DatabaseHandler dbHandler;

    public ConsoleDialogWindow(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void display() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Выберите операцию:");
            System.out.println("    1. Просмотр всех пользователей");
            System.out.println("    2. Добавить пользователя");
            System.out.println("    3. Обновить пользователя");
            System.out.println("    4. Удалить пользователя");
            System.out.println("    5. Выход");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> dbHandler.readUsers();
                case 2 -> {
                    scanner.nextLine();
                    System.out.println("Введите имя:");
                    String name = scanner.nextLine();
                    System.out.println("Введите email:");
                    String email = scanner.nextLine();
                    System.out.println("Введите возраст:");
                    int age = scanner.nextInt();

                    dbHandler.addUser(new User(name, email, age));
                }
                case 3 -> {
                    System.out.println("Введите id пользователя:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Введите имя:");
                    String name = scanner.nextLine();
                    System.out.println("Введите email:");
                    String email = scanner.nextLine();
                    System.out.println("Введите возраст:");
                    int age = scanner.nextInt();
                    dbHandler.updateUser(id, new User(name, email, age));
                }
                case 4 -> {
                    System.out.println("Введите id пользователя:");
                    int id = scanner.nextInt();
                    dbHandler.deleteUser(id);
                }
                case 5 -> {
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
