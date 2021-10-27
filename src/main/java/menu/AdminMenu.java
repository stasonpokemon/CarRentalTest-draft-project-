package menu;

import info.InfoAdminMenu;
import pojo.Admin;
import pojo.Car;
import pojo.Order;
import services.AdminService;
import services.CarService;
import services.OrderService;
import utils.OperationNumberUtil;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private Scanner scanner = new Scanner(System.in);
    private OperationNumberUtil operationNumberUtil = OperationNumberUtil.getOperationNumberUtil();
    private InfoAdminMenu infoAdminMenu = InfoAdminMenu.getInfoAdminMenu();
    private AdminService adminService = AdminService.getAdminService();
    private CarService carService = CarService.getCarService();
    private OrderService orderService = OrderService.getOrderService();
    private static AdminMenu menu = null;
    private int operationNumber;

    public static AdminMenu getMenu() {
        if (menu == null) {
            menu = new AdminMenu();
        }
        return menu;

    }

    /*
     * Меню инициализации администратора
     * */
    public void adminInitializationMenu() {
        boolean exitAdminInitializationMenu = false;
        do {
            boolean operationNumberValid = false;
            do {
                try {
                    infoAdminMenu.initializationMenuInfo();
                    operationNumber = scanner.nextInt();
                    scanner.nextLine();
                    operationNumberValid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Enter integer value...");
                    scanner.nextLine();
                    System.out.println("Exception: " + e);
                }
            } while (!operationNumberValid);
            switch (operationNumber) {
                case 1:
                    adminLoginMenu();
                    break;
                case 2:
                    exitAdminInitializationMenu = true;
                    System.out.println("Exit to the home menu...");
                    break;
                default:
                    System.out.println("There is no such operation. Try again");
                    break;
            }
        } while (!exitAdminInitializationMenu);
    }

    /*
     * Меню входа в учётную запись администраотра
     * */
    private void adminLoginMenu() {
        String adminLogin;
        String adminPassword;
        boolean exitAdminLogin = false;
        boolean adminLoginValid = false;
        do {
            System.out.println("Enter login...");
            adminLogin = scanner.next();

            System.out.println("Enter password...");
            adminPassword = scanner.next();

            List<Admin> allAdmins = adminService.findAllAdmins();
            for (Admin admin : allAdmins) {
                if (admin.getLogin().equals(adminLogin)) {
                    if (admin.getPassword().equals(adminPassword)) {
                        exitAdminLogin = true;
                        adminLoginValid = true;
                        adminMenu(admin);
                    }
                }
            }
            if (adminLoginValid == false) {
                boolean operationNumberValid = false;
                do {
                    try {
                        System.out.println("Login or password entered incorrectly...\n" +
                                "1. Try again \n" +
                                "2. Exit to the initialization menu");
                        operationNumber = scanner.nextInt();
                        operationNumberValid = true;
                    } catch (InputMismatchException e) {
                        System.out.println("Enter integer value...");
                        scanner.nextLine();
                        System.out.println("Exception: " + e);
                    }
                } while (!operationNumberValid);
                switch (operationNumber) {
                    case 1:
                        break;
                    case 2:
                        exitAdminLogin = true;
                        System.out.println("Exit to the initialization menu...");
                        break;
                    default:
                        System.out.println("There is no such operation. Try again");
                        break;
                }
            }
        } while (!exitAdminLogin);

    }

    /*
     * Меню администратора
     * */
    private void adminMenu(Admin admin) {
        boolean exitAdminMenu = false;
        do {
            boolean operationNumberValid = false;
            do {
                try {
                    infoAdminMenu.adminMenuInfo(admin);
                    operationNumber = scanner.nextInt();
                    scanner.nextLine();
                    operationNumberValid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Enter integer value...");
                    scanner.nextLine();
                    System.out.println("Exception: " + e);
                }
            } while (!operationNumberValid);
            switch (operationNumber) {
                case 1:
                    adminCarsMenu();
                    break;
                case 2:
                    adminOrdersMenu();
                    break;
                case 3:
                    adminClientsMenu();
                    break;
                case 4:
                    exitAdminMenu = true;
                    System.out.println("Exit to the home menu...");
                    break;
                default:
                    System.out.println("There is no such operation. Try again");
                    break;
            }
        } while (!exitAdminMenu);
    }

    /*
     * Меню администратора для взаимодействя с автомобилями
     * */
    private void adminCarsMenu() {
        boolean exitAdminCarsMenu = false;
        do {
            operationNumber = operationNumberUtil.operationNumberValid(operationNumber, infoAdminMenu.adminCarsMenuInfo());
            switch (operationNumber) {
                case 1:
                    boolean exitAllCarMenu = false;
                    System.out.println("Список всех автомобилей:");
                    carService.findAllCars().forEach(System.out::println);
                    do {
                        operationNumber = operationNumberUtil.operationNumberValid(operationNumber, "1. Назад");
                        switch (operationNumber) {
                            case 1:
                                System.out.println("Exit to the admin cars menu...");
                                exitAllCarMenu = true;
                                break;
                            default:
                                System.out.println("There is no such operation. Try again");
                                break;
                        }
                    } while (!exitAllCarMenu);
                    break;
                case 2:
                    System.out.println("Список свободных автомобилей:");
                    carService.findAllFreeCars().forEach(System.out::println);
                    break;
                case 3:
                    String model;
                    double price = 0;
                    boolean priceValid = false;
                    System.out.println("Добавлени енового автомобиля...\n" +
                            "Введите модель автомобиля:");
                    model = scanner.nextLine();
                    do {
                        try {
                            System.out.println("Введите цену за сутки:");
                            price = scanner.nextDouble();
                            priceValid = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Enter integer value...");
                            scanner.nextLine();
                            System.out.println("Exception: " + e);
                        }
                    } while (!priceValid);

                    Car newCar = new Car();
                    newCar.setModel(model);
                    newCar.setPricePerDay(price);
                    newCar.setEmploymentStatus("FREE");
                    newCar.setDamageStatus("WITHOUT DAMAGE");
                    carService.addNewCar(newCar);
                    System.out.println("Добавлен новый автомобиль: " + newCar);

                    break;
                case 4:
                    int carId = 0;
                    boolean carIdValid = false;
                    boolean carIdTrue = false;
                    System.out.println("Список всех автомобилей:");
                    carService.findAllCars().forEach(System.out::println);
                    do {
                        try {
                            System.out.println("Введите номер(id) автомобиля, которвый хотите удалить:");
                            carId = scanner.nextInt();
                            carIdValid = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Enter integer value...");
                            scanner.nextLine();
                            System.out.println("Exception: " + e);
                        }
                    } while (!carIdValid);
                    for (Car car : carService.findAllCars()) {
                        if (carId == car.getId()) {
                            carIdTrue = true;
                            carService.deleteCar(car);
                            System.out.println("Удаление автомобиля " + car);
                        }
                    }
                    if (!carIdTrue) {
                        System.out.println("Нет автомобиля с данным номером(id)...");
                    }
                    break;
                case 5:
                    exitAdminCarsMenu = true;
                    System.out.println("Exit to the admin menu...");
                    break;
                default:
                    System.out.println("There is no such operation. Try again");
                    break;
            }


        } while (!exitAdminCarsMenu);

    }

    /*
     * Меню администратора для взаимодействя с заказами
     * */
    private void adminOrdersMenu() {
        boolean exitAdminOrdersMenu = false;
        do {
            operationNumber = operationNumberUtil.operationNumberValid(operationNumber, infoAdminMenu.adminOrdersMenuInfo());
            switch (operationNumber) {
                case 1:
                    boolean exitAllOrdersMenu = false;
                    System.out.println("Список всех заказов:");
                    orderService.findAllOrders().forEach(System.out::println);
                    do {
                        operationNumber = operationNumberUtil.operationNumberValid(operationNumber, "1. Назад");
                        switch (operationNumber) {
                            case 1:
                                System.out.println("Exit to the admin orders menu...");
                                exitAllOrdersMenu = true;
                                break;
                            default:
                                System.out.println("There is no such operation. Try again");
                                break;
                        }
                    } while (!exitAllOrdersMenu);
                    break;
                case 2:
                    boolean exitOrdersUnderConsiderationMenu = false;
                    System.out.println("Список заявок на заказ:");
                    orderService.findOrdersUnderConsideration().forEach(System.out::println);
                    do {
                        operationNumber = operationNumberUtil.operationNumberValid(operationNumber, infoAdminMenu.adminOrdersUnderConsiderationMenuInfo());
                        switch (operationNumber) {
                            case 1:
                                Order selectedOrder = null;
                                int orderId;
                                boolean orderIdValid = false;
                                boolean orderIdTrue = false;


                                System.out.println("Введите номер(id) заявки:");
                                orderId = scanner.nextInt();

                                for (Order order : orderService.findOrdersUnderConsideration()) {
                                    if (order.getId() == orderId) {
                                        selectedOrder = order;
                                        orderIdTrue = true;
                                    }
                                }
                                if (orderIdTrue) {
                                    adminOrderStatusMenu(selectedOrder);
                                } else {
                                    System.out.println("Заявки с данным номером(id) не существует...");
                                }


                            case 2:
                                System.out.println("Exit to the admin orders menu...");
                                exitOrdersUnderConsiderationMenu = true;
                                break;
                            default:
                                System.out.println("There is no such operation. Try again");
                                break;
                        }
                    } while (!exitOrdersUnderConsiderationMenu);
                    break;
                case 3:
                    break;
                case 4:
                    exitAdminOrdersMenu = true;
                    System.out.println("Exit to the admin menu...");
                    break;
                default:
                    System.out.println("There is no such operation. Try again");
                    break;
            }
        } while (!exitAdminOrdersMenu);

    }

    /*
     * Меню администратора для взаимодействя с клиентами
     * */
    private void adminClientsMenu() {
        boolean exitAdminClientsMenu = false;
        do {
            operationNumber = operationNumberUtil.operationNumberValid(operationNumber, infoAdminMenu.adminClientsMenuInfo());

        } while (!exitAdminClientsMenu);
    }

    /*
     * Меню администратора для указания статуса заказа
     */
    private void adminOrderStatusMenu(Order selectedOrder) {
        boolean exitAdminOrderStatusMenu = false;
        do {
            operationNumber = operationNumberUtil.operationNumberValid(operationNumber, infoAdminMenu.adminOrderStatusMenuInfo(selectedOrder));
            switch (operationNumber) {
                case 1:
                    orderService.setOrderApprovedStatus(selectedOrder);
                    System.out.println("Заказ одобрен...");
                    /*
                    * !!! Сделать, чтобы при оодобрении заявки к заказу добавлялось время
                    * */
                    exitAdminOrderStatusMenu = true;
                    break;
                case 2:
                    orderService.setOrderRejectStatus(selectedOrder);
                    System.out.println("Заказ отклонён...");
                    /*
                    * Если заказ отменён, то автомобилю возвращается статус(СВОБОДНА)
                    * */
                    Car orderCar = selectedOrder.getCar();
                    carService.setCarStatusToFree(orderCar);
                    exitAdminOrderStatusMenu = true;
                    break;
                case 3:
                    exitAdminOrderStatusMenu = true;
                    break;
                default:
                    System.out.println("There is no such operation. Try again");
                    break;
            }
        } while (!exitAdminOrderStatusMenu);

    }

}
