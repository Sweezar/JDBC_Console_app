package org.berneick;

public class Main {
    public static void main(String[] args) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ConsoleDialogWindow dialogWindow = new ConsoleDialogWindow(dbHandler);

        dialogWindow.display();
    }
}