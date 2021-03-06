package pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class User {

    private int id;
    private String login;
    private String password;
    private String role;
    private ClientPassport passport;
//    Инициализируется при создании паспорта...
    private List<Order> orders = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ClientPassport getPassport() {
        return passport;
    }

    public void setPassport(ClientPassport passport) {
        this.passport = passport;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(passport, user.passport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, passport);
    }

    @Override
    public String toString() {
        return String.format("%-6s%-30s%-15s", id, login, password);
    }
}
