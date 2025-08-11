import dao.UserDao;
import model.User;
import service.UserService;
import utils.HibernateUtil;

import java.util.Scanner;

public class Main {

    private static final UserService userService = new UserService(new UserDao());

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("""
                    \nВыберите операцию:
                    1. Добавить пользователя
                    2. Показать пользователя по ID
                    3. Показать всех пользователей
                    4. Обновить пользователя
                    5. Удалить пользователя
                    6. Выход
                    """);

            switch (sc.nextLine()) {
                case "1" -> {
                    System.out.print("Имя: ");
                    String name = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Возраст: ");
                    int age = Integer.parseInt(sc.nextLine());

                    Long id = userService.create(name, email, age);
                    if (id != null) {
                        System.out.println("Пользователь сохранён, id=" + id);
                    } else {
                        System.out.println("Ошибка при сохранении пользователя");
                    }
                }
                case "2" -> {
                    System.out.print("ID: ");
                    long id = Long.parseLong(sc.nextLine());
                    User user = userService.getById(id);
                    if (user != null) {
                        System.out.println(user);
                    } else {
                        System.out.println("Пользователь не найден");
                    }
                }
                case "3" -> userService.getAllUsers().forEach(System.out::println);
                case "4" -> {
                    System.out.print("ID пользователя: ");
                    long id = Long.parseLong(sc.nextLine());

                    System.out.print("Новое имя: ");
                    String name = sc.nextLine();
                    System.out.print("Новый email: ");
                    String email = sc.nextLine();
                    System.out.print("Новый возраст: ");
                    int age = Integer.parseInt(sc.nextLine());

                    if (userService.update(id, name, email, age)) {
                        System.out.println("Пользователь обновлён");
                    } else {
                        System.out.println("Ошибка при обновлении");
                    }
                }
                case "5" -> {
                    System.out.print("ID пользователя: ");
                    Long id = Long.parseLong(sc.nextLine());
                    if (userService.delete(id)) {
                        System.out.println("Пользователь удалён");
                    } else {
                        System.out.println("Ошибка при удалении");
                    }
                }
                case "6" -> {
                    running = false;
                    HibernateUtil.shutdown();
                    System.out.println("Завершено.");
                }
                default -> System.out.println("Неверный ввод");
            }
        }
    }
}
