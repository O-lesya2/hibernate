import dao.UserDao;
import model.User;
import utils.HibernateUtil;

import java.util.Scanner;

public class Main {

    private static final UserDao userDao = new UserDao();

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

                    userDao.create(new User(name, email, age));
                }
                case "2" -> {
                    System.out.print("ID: ");
                    Long id = Long.parseLong(sc.nextLine());
                    System.out.println(userDao.findById(id));
                }
                case "3" -> userDao.findAll().forEach(System.out::println);
                case "4" -> {
                    System.out.print("ID пользователя: ");
                    Long id = Long.parseLong(sc.nextLine());
                    User user = userDao.findById(id);
                    if (user == null) {
                        System.out.println("Пользователь не найден");
                        break;
                    }

                    System.out.print("Новое имя: ");
                    user.setName(sc.nextLine());
                    System.out.print("Новый email: ");
                    user.setEmail(sc.nextLine());
                    System.out.print("Новый возраст: ");
                    user.setAge(Integer.parseInt(sc.nextLine()));
                    userDao.update(user);
                }
                case "5" -> {
                    System.out.print("ID пользователя: ");
                    Long id = Long.parseLong(sc.nextLine());
                    userDao.delete(id);
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
