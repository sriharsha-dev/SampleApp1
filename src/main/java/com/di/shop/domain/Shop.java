package com.di.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Shop.
 */
@Entity
@Table(name = "shop")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "s_id")
    private Long sId;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "jhi_password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "shop")
    @JsonIgnore
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "shop")
    @JsonIgnore
    private Set<Items> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getsId() {
        return sId;
    }

    public Shop sId(Long sId) {
        this.sId = sId;
        return this;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }

    public String getLogin() {
        return login;
    }

    public Shop login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public Shop password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public Shop name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Shop customers(Set<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public Shop addCustomers(Customer customer) {
        this.customers.add(customer);
        customer.setShop(this);
        return this;
    }

    public Shop removeCustomers(Customer customer) {
        this.customers.remove(customer);
        customer.setShop(null);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Items> getItems() {
        return items;
    }

    public Shop items(Set<Items> items) {
        this.items = items;
        return this;
    }

    public Shop addItems(Items items) {
        this.items.add(items);
        items.setShop(this);
        return this;
    }

    public Shop removeItems(Items items) {
        this.items.remove(items);
        items.setShop(null);
        return this;
    }

    public void setItems(Set<Items> items) {
        this.items = items;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shop shop = (Shop) o;
        if (shop.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shop.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Shop{" +
            "id=" + getId() +
            ", sId=" + getsId() +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
