package pojo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "refunds")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(mappedBy = "refund")
    private Order order;
    @Column(name = "damage_status")
    private String damageStatus;
    @Column(name = "type_damage")
    private String typeDamage;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getDamageStatus() {
        return damageStatus;
    }

    public void setDamageStatus(String damageStatus) {
        this.damageStatus = damageStatus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTypeDamage() {
        return typeDamage;
    }

    public void setTypeDamage(String typeDamage) {
        this.typeDamage = typeDamage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Refund refund = (Refund) o;
        return id == refund.id && Double.compare(refund.price, price) == 0 && Objects.equals(order, refund.order) && Objects.equals(damageStatus, refund.damageStatus) && Objects.equals(typeDamage, refund.typeDamage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, damageStatus, typeDamage, price);
    }

    @Override
    public String toString() {
        return String.format("%-6s%-20s%-20s%-15s", id, damageStatus, typeDamage, price);
    }
}
