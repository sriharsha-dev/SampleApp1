package com.di.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "u_id")
    private Long uId;

    @NotNull
    @Column(name = "u_login", nullable = false)
    private String uLogin;

    @NotNull
    @Column(name = "u_password", nullable = false)
    private String uPassword;

    @NotNull
    @Column(name = "u_total_credits", nullable = false)
    private Long uTotalCredits;

    @Column(name = "u_placed_credits")
    private Long uPlacedCredits;

    @Column(name = "u_gained_credits")
    private Long uGainedCredits;

    @Column(name = "u_name")
    private String uName;

    @Column(name = "u_contact_info")
    private String uContactInfo;

    @Column(name = "created_on")
    private Instant createdOn;

    @ManyToOne
    private Shop shop;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private Set<Transaction> transactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getuId() {
        return uId;
    }

    public Customer uId(Long uId) {
        this.uId = uId;
        return this;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getuLogin() {
        return uLogin;
    }

    public Customer uLogin(String uLogin) {
        this.uLogin = uLogin;
        return this;
    }

    public void setuLogin(String uLogin) {
        this.uLogin = uLogin;
    }

    public String getuPassword() {
        return uPassword;
    }

    public Customer uPassword(String uPassword) {
        this.uPassword = uPassword;
        return this;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public Long getuTotalCredits() {
        return uTotalCredits;
    }

    public Customer uTotalCredits(Long uTotalCredits) {
        this.uTotalCredits = uTotalCredits;
        return this;
    }

    public void setuTotalCredits(Long uTotalCredits) {
        this.uTotalCredits = uTotalCredits;
    }

    public Long getuPlacedCredits() {
        return uPlacedCredits;
    }

    public Customer uPlacedCredits(Long uPlacedCredits) {
        this.uPlacedCredits = uPlacedCredits;
        return this;
    }

    public void setuPlacedCredits(Long uPlacedCredits) {
        this.uPlacedCredits = uPlacedCredits;
    }

    public Long getuGainedCredits() {
        return uGainedCredits;
    }

    public Customer uGainedCredits(Long uGainedCredits) {
        this.uGainedCredits = uGainedCredits;
        return this;
    }

    public void setuGainedCredits(Long uGainedCredits) {
        this.uGainedCredits = uGainedCredits;
    }

    public String getuName() {
        return uName;
    }

    public Customer uName(String uName) {
        this.uName = uName;
        return this;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuContactInfo() {
        return uContactInfo;
    }

    public Customer uContactInfo(String uContactInfo) {
        this.uContactInfo = uContactInfo;
        return this;
    }

    public void setuContactInfo(String uContactInfo) {
        this.uContactInfo = uContactInfo;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Customer createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Shop getShop() {
        return shop;
    }

    public Customer shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Customer transactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Customer addTransactions(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setCustomer(this);
        return this;
    }

    public Customer removeTransactions(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setCustomer(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
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
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", uId=" + getuId() +
            ", uLogin='" + getuLogin() + "'" +
            ", uPassword='" + getuPassword() + "'" +
            ", uTotalCredits=" + getuTotalCredits() +
            ", uPlacedCredits=" + getuPlacedCredits() +
            ", uGainedCredits=" + getuGainedCredits() +
            ", uName='" + getuName() + "'" +
            ", uContactInfo='" + getuContactInfo() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
