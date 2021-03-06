package dao.mysql;

import pojo.*;
import pojo.constant.UserRoleConst;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl extends BaseDaoImpl implements OrderDaoI {

    private static OrderDaoImpl orderDaoImpl;

    public OrderDaoImpl() throws SQLException {
    }

    public static OrderDaoImpl getOrderDaoImpl() throws SQLException {
        if (orderDaoImpl == null) {
            orderDaoImpl = new OrderDaoImpl();
        }
        return orderDaoImpl;
    }


    /**
     * Работает, но по отдельности
     */
    @Override
    public void save(Order order) {
        String sql = "INSERT INTO orders (id, user_id, car_id, price, status, order_date, rental_Period/*, refund_id*/) VALUES(?, ?, ?, ?, ?, ?, ?/*, ?*/)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, order.getId());
            statement.setInt(2, order.getClient().getId());
            statement.setInt(3, order.getCar().getId());
            statement.setDouble(4, order.getPrice());
            statement.setString(5, order.getOrderStatus());
            statement.setTimestamp(6, order.getOrderDate());
            statement.setInt(7, order.getRentalPeriod());
//            statement.setInt(8, order.getRefund().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Работает!!!!!!!!!!!!!!, но по отдельности
     */
    @Override
    public Order read(int id) {
        String sqlOrder = "SELECT id, user_id, car_id, price, status, order_date, rental_Period,  refund_id FROM orders WHERE id = ?";
        String sqlClient = "SELECT id, user_login, user_password, user_role, passport_id FROM users WHERE id = ? AND user_role LIKE ?";
        String sqlCar = "SELECT id, model, price_per_day, employment_status, damage_status FROM cars WHERE id = ?";
        String sqlPassport = "SELECT id, name, surname, patronymic,birthday, address FROM passports WHERE id = ?";
        String sqlRefund = "SELECT id, damage_status, price FROM refunds WHERE id = ?";

        PreparedStatement statementOrder = null;
        PreparedStatement statementClient = null;
        PreparedStatement statementCar = null;
        PreparedStatement statementPassport = null;
        PreparedStatement statementRefund = null;

        ResultSet resultSetOrder = null;
        ResultSet resultSetClient = null;
        ResultSet resultSetCar = null;
        ResultSet resultSetPassport = null;
        ResultSet resultSetRefund = null;

        Order order = null;
        User client = null;
        Car car = null;
        ClientPassport passport = null;
        Refund refund = null;
        try {
            statementOrder = connection.prepareStatement(sqlOrder);
            statementClient = connection.prepareStatement(sqlClient);
            statementCar = connection.prepareStatement(sqlCar);
            statementPassport = connection.prepareStatement(sqlPassport);
            statementRefund = connection.prepareStatement(sqlRefund);

            statementOrder.setInt(1, id);
            resultSetOrder = statementOrder.executeQuery();
            if (resultSetOrder.next()) {
                order = new Order();
                order.setId(id);

                int clientId = resultSetOrder.getInt("user_id");
                statementClient.setInt(1, clientId);
                statementClient.setString(2, UserRoleConst.CLIENT_ROLE);
                resultSetClient = statementClient.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetClient.next()) {
                        client = new User();
                        client.setId(resultSetClient.getInt("id"));
                        client.setLogin(resultSetClient.getString("user_login"));
                        client.setPassword(resultSetClient.getString("user_password"));
                        client.setRole(resultSetClient.getString("user_role"));

                        int passportId = resultSetClient.getInt("passport_id");
                        statementPassport.setInt(1, passportId);
                        resultSetPassport = statementPassport.executeQuery();
                        if (!resultSetClient.wasNull()) {
                            if (resultSetPassport.next()) {
                                passport = new ClientPassport();
                                passport.setId(resultSetPassport.getInt("id"));
                                passport.setName(resultSetPassport.getString("name"));
                                passport.setSurname(resultSetPassport.getString("surname"));
                                passport.setPatronymic(resultSetPassport.getString("patronymic"));
                                passport.setBirthday(resultSetPassport.getTimestamp("birthday"));
                                passport.setAddress(resultSetPassport.getString("address"));
                            }
                            client.setPassport(passport);
                        }

                    }
                    order.setClient(client);
                }

                int carId = resultSetOrder.getInt("car_id");
                statementCar.setInt(1, carId);
                resultSetCar = statementCar.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetCar.next()) {
                        car = new Car();
                        car.setId(resultSetCar.getInt("id"));
                        car.setModel(resultSetCar.getString("model"));
                        car.setPricePerDay(resultSetCar.getDouble("price_per_day"));
                        car.setEmploymentStatus(resultSetCar.getString("employment_status"));
                        car.setDamageStatus(resultSetCar.getString("damage_status"));
                    }
                    order.setCar(car);
                }

                order.setPrice(resultSetOrder.getDouble("price"));
                order.setOrderStatus(resultSetOrder.getString("status"));
                order.setOrderDate(resultSetOrder.getTimestamp("order_date"));
                order.setRentalPeriod(resultSetOrder.getInt("rental_Period"));

                int refund_id = resultSetOrder.getInt("refund_id");
                statementRefund.setInt(1, refund_id);
                resultSetRefund = statementRefund.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetRefund.next()) {
                        refund = new Refund();
                        refund.setId(resultSetRefund.getInt("id"));
                        refund.setDamageStatus(resultSetRefund.getString("damage_status"));
                        refund.setPrice(resultSetRefund.getDouble("price"));
                    }
                    order.setRefund(refund);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSetOrder != null) {
                    resultSetOrder.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }


    /*
     * Обновление без указания возврата
     * */
    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET user_id = ?, car_id = ?, price = ?, status = ?, order_date = ?, rental_Period = ?/*, refund_id = ?*/  WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, order.getClient().getId());
            statement.setInt(2, order.getCar().getId());
            statement.setDouble(3, order.getPrice());
            statement.setString(4, order.getOrderStatus());
            statement.setTimestamp(5, order.getOrderDate());
            statement.setInt(6, order.getRentalPeriod());
//            statement.setInt(7, order.getRefund().getId());
            statement.setInt(7, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Обновление с указанием возврата
     * */
    @Override
    public void updateWithRefund(Order order) {
        String sql = "UPDATE orders SET user_id = ?, car_id = ?, price = ?, status = ?, order_date = ?, rental_Period = ?, refund_id = ?  WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, order.getClient().getId());
            statement.setInt(2, order.getCar().getId());
            statement.setDouble(3, order.getPrice());
            statement.setString(4, order.getOrderStatus());
            statement.setTimestamp(5, order.getOrderDate());
            statement.setInt(6, order.getRentalPeriod());
            statement.setInt(7, order.getRefund().getId());
            statement.setInt(8, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Order order) {
        String sql = "DELETE FROM orders WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Работает!!!!!!!!!!!!!!, но по отдельности
     */
    @Override
    public List<Order> readAll() {
        String sqlOrder = "SELECT id, user_id, car_id, price, status, order_date, rental_Period, refund_id FROM orders";
        String sqlClient = "SELECT id, user_login, user_password, user_role, passport_id FROM users WHERE id = ? AND user_role LIKE ?";
        String sqlCar = "SELECT id, model, price_per_day, employment_status, damage_status FROM cars WHERE id = ?";
        String sqlPassport = "SELECT id, name, surname, patronymic, birthday, address FROM passports WHERE id = ?";
        String sqlRefund = "SELECT id, damage_status, type_damage, price FROM refunds WHERE id = ?";
        List<Order> orders = new ArrayList<>();

        PreparedStatement statementOrder = null;
        PreparedStatement statementClient = null;
        PreparedStatement statementCar = null;
        PreparedStatement statementPassport = null;
        PreparedStatement statementRefund = null;

        ResultSet resultSetOrder = null;
        ResultSet resultSetClient = null;
        ResultSet resultSetCar = null;
        ResultSet resultSetPassport = null;
        ResultSet resultSetRefund = null;

        Order order = null;
        User client = null;
        Car car = null;
        ClientPassport passport = null;
        Refund refund = null;


        try {
            statementOrder = connection.prepareStatement(sqlOrder);
            statementClient = connection.prepareStatement(sqlClient);
            statementCar = connection.prepareStatement(sqlCar);
            statementPassport = connection.prepareStatement(sqlPassport);
            statementRefund = connection.prepareStatement(sqlRefund);


            resultSetOrder = statementOrder.executeQuery();
            while (resultSetOrder.next()) {
                order = new Order();
                order.setId(resultSetOrder.getInt("id"));

                int clientId = resultSetOrder.getInt("user_id");
                statementClient.setInt(1, clientId);
                statementClient.setString(2, UserRoleConst.CLIENT_ROLE);
                resultSetClient = statementClient.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetClient.next()) {
                        client = new User();
                        client.setId(resultSetClient.getInt("id"));
                        client.setLogin(resultSetClient.getString("user_login"));
                        client.setPassword(resultSetClient.getString("user_password"));
                        client.setRole(resultSetClient.getString("user_role"));

                        int passportId = resultSetClient.getInt("passport_id");
                        statementPassport.setInt(1, passportId);
                        resultSetPassport = statementPassport.executeQuery();
                        if (!resultSetClient.wasNull()) {
                            if (resultSetPassport.next()) {
                                passport = new ClientPassport();
                                passport.setId(resultSetPassport.getInt("id"));
                                passport.setName(resultSetPassport.getString("name"));
                                passport.setSurname(resultSetPassport.getString("surname"));
                                passport.setPatronymic(resultSetPassport.getString("patronymic"));
                                passport.setBirthday(resultSetPassport.getTimestamp("birthday"));
                                passport.setAddress(resultSetPassport.getString("address"));
                            }
                            client.setPassport(passport);
                        }

                    }
                    order.setClient(client);
                }

                int carId = resultSetOrder.getInt("car_id");
                statementCar.setInt(1, carId);
                resultSetCar = statementCar.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetCar.next()) {
                        car = new Car();
                        car.setId(resultSetCar.getInt("id"));
                        car.setModel(resultSetCar.getString("model"));
                        car.setPricePerDay(resultSetCar.getDouble("price_per_day"));
                        car.setEmploymentStatus(resultSetCar.getString("employment_status"));
                        car.setDamageStatus(resultSetCar.getString("damage_status"));
                    }
                    order.setCar(car);
                }

                order.setPrice(resultSetOrder.getDouble("price"));
                order.setOrderStatus(resultSetOrder.getString("status"));
                order.setOrderDate(resultSetOrder.getTimestamp("order_date"));
                order.setRentalPeriod(resultSetOrder.getInt("rental_Period"));

                int refund_id = resultSetOrder.getInt("refund_id");
                statementRefund.setInt(1, refund_id);
                resultSetRefund = statementRefund.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetRefund.next()) {
                        refund = new Refund();
                        refund.setId(resultSetRefund.getInt("id"));
                        refund.setDamageStatus(resultSetRefund.getString("damage_status"));
                        refund.setTypeDamage(resultSetRefund.getString("type_damage"));
                        refund.setPrice(resultSetRefund.getDouble("price"));
                    }
                    order.setRefund(refund);
                }
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Работает!!!!!!!!!!!!!!, но по отдельности
     */
    @Override
    public List<Order> findAllOrdersByClient(User user) {
        String sqlOrder = "SELECT id, user_id, car_id, price, status, order_date, rental_Period, refund_id FROM orders WHERE user_id = ?";
        String sqlClient = "SELECT id, user_login, user_password, user_role, passport_id FROM users WHERE id = ? AND user_role LIKE ?";
        String sqlCar = "SELECT id, model, price_per_day, employment_status, damage_status FROM cars WHERE id = ?";
        String sqlPassport = "SELECT id, name, surname, patronymic, birthday, address FROM passports WHERE id = ?";
        String sqlRefund = "SELECT id, damage_status, price FROM refunds WHERE id = ?";
        List<Order> orders = new ArrayList<>();

        PreparedStatement statementOrder = null;
        PreparedStatement statementClient = null;
        PreparedStatement statementCar = null;
        PreparedStatement statementPassport = null;
        PreparedStatement statementRefund = null;

        ResultSet resultSetOrder = null;
        ResultSet resultSetClient = null;
        ResultSet resultSetCar = null;
        ResultSet resultSetPassport = null;
        ResultSet resultSetRefund = null;

        Order order = null;
        User client = null;
        Car car = null;
        ClientPassport passport = null;
        Refund refund = null;


        try {
            statementOrder = connection.prepareStatement(sqlOrder);
            statementClient = connection.prepareStatement(sqlClient);
            statementCar = connection.prepareStatement(sqlCar);
            statementPassport = connection.prepareStatement(sqlPassport);
            statementRefund = connection.prepareStatement(sqlRefund);

            statementOrder.setInt(1, user.getId());
            resultSetOrder = statementOrder.executeQuery();
            while (resultSetOrder.next()) {
                order = new Order();
                order.setId(resultSetOrder.getInt("id"));

                int clientId = resultSetOrder.getInt("user_id");
                statementClient.setInt(1, clientId);
                statementClient.setString(2, UserRoleConst.CLIENT_ROLE);
                resultSetClient = statementClient.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetClient.next()) {
                        client = new User();
                        client.setId(resultSetClient.getInt("id"));
                        client.setLogin(resultSetClient.getString("user_login"));
                        client.setPassword(resultSetClient.getString("user_password"));
                        client.setRole(resultSetClient.getString("user_role"));

                        int passportId = resultSetClient.getInt("passport_id");
                        statementPassport.setInt(1, passportId);
                        resultSetPassport = statementPassport.executeQuery();
                        if (!resultSetClient.wasNull()) {
                            if (resultSetPassport.next()) {
                                passport = new ClientPassport();
                                passport.setId(resultSetPassport.getInt("id"));
                                passport.setName(resultSetPassport.getString("name"));
                                passport.setSurname(resultSetPassport.getString("surname"));
                                passport.setPatronymic(resultSetPassport.getString("patronymic"));
                                passport.setBirthday(resultSetPassport.getTimestamp("birthday"));
                                passport.setAddress(resultSetPassport.getString("address"));
                            }
                            client.setPassport(passport);
                        }

                    }
                    order.setClient(client);
                }

                int carId = resultSetOrder.getInt("car_id");
                statementCar.setInt(1, carId);
                resultSetCar = statementCar.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetCar.next()) {
                        car = new Car();
                        car.setId(resultSetCar.getInt("id"));
                        car.setModel(resultSetCar.getString("model"));
                        car.setPricePerDay(resultSetCar.getDouble("price_per_day"));
                        car.setEmploymentStatus(resultSetCar.getString("employment_status"));
                        car.setDamageStatus(resultSetCar.getString("damage_status"));
                    }
                    order.setCar(car);
                }

                order.setPrice(resultSetOrder.getDouble("price"));
                order.setOrderStatus(resultSetOrder.getString("status"));
                order.setOrderDate(resultSetOrder.getTimestamp("order_date"));
                order.setRentalPeriod(resultSetOrder.getInt("rental_Period"));

                int refund_id = resultSetOrder.getInt("refund_id");
                statementRefund.setInt(1, refund_id);
                resultSetRefund = statementRefund.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetRefund.next()) {
                        refund = new Refund();
                        refund.setId(resultSetRefund.getInt("id"));
                        refund.setDamageStatus(resultSetRefund.getString("damage_status"));
                        refund.setPrice(resultSetRefund.getDouble("price"));
                    }
                    order.setRefund(refund);
                }
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Работает!!!!!!!!!!!!!!, но по отдельности
     */
    @Override
    public List<Order> findOrdersByStatus(String status) {
        String sqlOrder = "SELECT id, user_id, car_id, price, status, order_date, rental_Period, refund_id FROM orders WHERE status = ?";
        String sqlClient = "SELECT id, user_login, user_password, user_role, passport_id FROM users WHERE id = ? AND user_role LIKE ?";
        String sqlCar = "SELECT id, model, price_per_day, employment_status, damage_status FROM cars WHERE id = ?";
        String sqlPassport = "SELECT id, name, surname, patronymic, birthday, address FROM passports WHERE id = ?";
        String sqlRefund = "SELECT id, damage_status, price FROM refunds WHERE id = ?";
        List<Order> orders = new ArrayList<>();

        PreparedStatement statementOrder = null;
        PreparedStatement statementClient = null;
        PreparedStatement statementCar = null;
        PreparedStatement statementPassport = null;
        PreparedStatement statementRefund = null;

        ResultSet resultSetOrder = null;
        ResultSet resultSetClient = null;
        ResultSet resultSetCar = null;
        ResultSet resultSetPassport = null;
        ResultSet resultSetRefund = null;

        Order order = null;
        User client = null;
        Car car = null;
        ClientPassport passport = null;
        Refund refund = null;


        try {
            statementOrder = connection.prepareStatement(sqlOrder);
            statementClient = connection.prepareStatement(sqlClient);
            statementCar = connection.prepareStatement(sqlCar);
            statementPassport = connection.prepareStatement(sqlPassport);
            statementRefund = connection.prepareStatement(sqlRefund);

            statementOrder.setString(1, status);
            resultSetOrder = statementOrder.executeQuery();
            while (resultSetOrder.next()) {
                order = new Order();
                order.setId(resultSetOrder.getInt("id"));

                int clientId = resultSetOrder.getInt("user_id");
                statementClient.setInt(1, clientId);
                statementClient.setString(2, UserRoleConst.CLIENT_ROLE);
                resultSetClient = statementClient.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetClient.next()) {
                        client = new User();
                        client.setId(resultSetClient.getInt("id"));
                        client.setLogin(resultSetClient.getString("user_login"));
                        client.setPassword(resultSetClient.getString("user_password"));
                        client.setRole(resultSetClient.getString("user_role"));

                        int passportId = resultSetClient.getInt("passport_id");
                        statementPassport.setInt(1, passportId);
                        resultSetPassport = statementPassport.executeQuery();
                        if (!resultSetClient.wasNull()) {
                            if (resultSetPassport.next()) {
                                passport = new ClientPassport();
                                passport.setId(resultSetPassport.getInt("id"));
                                passport.setName(resultSetPassport.getString("name"));
                                passport.setSurname(resultSetPassport.getString("surname"));
                                passport.setPatronymic(resultSetPassport.getString("patronymic"));
                                passport.setBirthday(resultSetPassport.getTimestamp("birthday"));
                                passport.setAddress(resultSetPassport.getString("address"));
                            }
                            client.setPassport(passport);
                        }

                    }
                    order.setClient(client);
                }

                int carId = resultSetOrder.getInt("car_id");
                statementCar.setInt(1, carId);
                resultSetCar = statementCar.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetCar.next()) {
                        car = new Car();
                        car.setId(resultSetCar.getInt("id"));
                        car.setModel(resultSetCar.getString("model"));
                        car.setPricePerDay(resultSetCar.getDouble("price_per_day"));
                        car.setEmploymentStatus(resultSetCar.getString("employment_status"));
                        car.setDamageStatus(resultSetCar.getString("damage_status"));
                    }
                    order.setCar(car);
                }

                order.setPrice(resultSetOrder.getDouble("price"));
                order.setOrderStatus(resultSetOrder.getString("status"));
                order.setOrderDate(resultSetOrder.getTimestamp("order_date"));
                order.setRentalPeriod(resultSetOrder.getInt("rental_Period"));

                int refund_id = resultSetOrder.getInt("refund_id");
                statementRefund.setInt(1, refund_id);
                resultSetRefund = statementRefund.executeQuery();
                if (!resultSetOrder.wasNull()) {
                    if (resultSetRefund.next()) {
                        refund = new Refund();
                        refund.setId(resultSetRefund.getInt("id"));
                        refund.setDamageStatus(resultSetRefund.getString("damage_status"));
                        refund.setPrice(resultSetRefund.getDouble("price"));
                    }
                    order.setRefund(refund);
                }

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public int getMaxOrderId() {
        String sql = "SELECT MAX(id) FROM orders";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id = 0;
        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("MAX(id)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

}
