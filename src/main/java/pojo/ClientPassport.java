package pojo;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "passports")
public class ClientPassport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "birthday", columnDefinition = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;
    @Column(name = "address")
    private String address;
    @OneToOne(mappedBy = "passport")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getClient() {
        return user;
    }

    public void setClient(User user) {
        this.user = user;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientPassport passport = (ClientPassport) o;
        return id == passport.id && Objects.equals(name, passport.name) && Objects.equals(surname, passport.surname) && Objects.equals(patronymic, passport.patronymic) && Objects.equals(birthday, passport.birthday) && Objects.equals(address, passport.address) && Objects.equals(user, passport.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronymic, birthday, address, user);
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%-6s%-20s%-20s%-20s%-20s%-20s", id, name, surname, patronymic, format.format(birthday), address);
    }
}
