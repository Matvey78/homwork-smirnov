import java.util.ArrayList;
import java.util.Scanner;

class Task {
    private String title;
    private boolean isCompleted;

    public Task(String title) {
        this.title = title;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return (isCompleted ? "[X] " : "[ ] ") + title;
    }
}

public class ToDoList {
    private ArrayList<Task> tasks;
    private Scanner scanner;

    public ToDoList() {
        tasks = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("Меню списка дел:");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Удалить задачу");
        System.out.println("3. Редактировать задачу");
        System.out.println("4. Показать задачи");
        System.out.println("5. Отметить задачу как выполненную");
        System.out.println("6. Выйти");
        System.out.print("Выберите опцию: ");
    }

    public void addTask() {
        System.out.print("Введите название новой задачи: ");
        String title = scanner.nextLine();
        tasks.add(new Task(title));
        System.out.println("Задача добавлена.");
    }

    public void removeTask() {
        displayTasks();
        System.out.print("Введите номер задачи для удаления: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index > 0 && index <= tasks.size()) {
            tasks.remove(index - 1);
            System.out.println("Задача удалена.");
        } else {
            System.out.println("Неверный номер задачи.");
        }
    }

    public void editTask() {
        displayTasks();
        System.out.print("Введите номер задачи для редактирования: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index > 0 && index <= tasks.size()) {
            System.out.print("Введите новое название задачи: ");
            String newTitle = scanner.nextLine();
            tasks.get(index - 1).setTitle(newTitle);
            System.out.println("Задача отредактирована.");
        } else {
            System.out.println("Неверный номер задачи.");
        }
    }

    public void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Задачи отсутствуют.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    public void markTaskAsCompleted() {
        displayTasks();
        System.out.print("Введите номер задачи, чтобы отметить как выполненную: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index > 0 && index <= tasks.size()) {
            tasks.get(index - 1).setCompleted(true);
            System.out.println("Задача отмечена как выполненная.");
        } else {
            System.out.println("Неверный номер задачи.");
        }
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            displayMenu();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    removeTask();
                    break;
                case 3:
                    editTask();
                    break;
                case 4:
                    displayTasks();
                    break;
                case 5:
                    markTaskAsCompleted();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    public static void main(String[] args) {
        ToDoList toDoList = new ToDoList();
        toDoList.run();
    }
}
