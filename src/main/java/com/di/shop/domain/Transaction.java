package com.di.shop.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "t_id")
    private Long tId;

    @NotNull
    @Column(name = "placed_credits", nullable = false)
    private Long placedCredits;

    @NotNull
    @Column(name = "placed_on", nullable = false)
    private Instant placedOn;

    @ManyToOne
    private Customer customer;

    @OneToOne
    @JoinColumn(unique = true)
    private Customer placedBy;

    @OneToOne
    @JoinColumn(unique = true)
    private Options option;

    @OneToOne
    @JoinColumn(unique = true)
    private Items item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long gettId() {
        return tId;
    }

    public Transaction tId(Long tId) {
        this.tId = tId;
        return this;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Long getPlacedCredits() {
        return placedCredits;
    }

    public Transaction placedCredits(Long placedCredits) {
        this.placedCredits = placedCredits;
        return this;
    }

    public void setPlacedCredits(Long placedCredits) {
        this.placedCredits = placedCredits;
    }

    public Instant getPlacedOn() {
        return placedOn;
    }

    public Transaction placedOn(Instant placedOn) {
        this.placedOn = placedOn;
        return this;
    }

    public void setPlacedOn(Instant placedOn) {
        this.placedOn = placedOn;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Transaction customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getPlacedBy() {
        return placedBy;
    }

    public Transaction placedBy(Customer customer) {
        this.placedBy = customer;
        return this;
    }

    public void setPlacedBy(Customer customer) {
        this.placedBy = customer;
    }

    public Options getOption() {
        return option;
    }

    public Transaction option(Options options) {
        this.option = options;
        return this;
    }

    public void setOption(Options options) {
        this.option = options;
    }

    public Items getItem() {
        return item;
    }

    public Transaction item(Items items) {
        this.item = items;
        return this;
    }

    public void setItem(Items items) {
        this.item = items;
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
        Transaction transaction = (Transaction) o;
        if (transaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", tId=" + gettId() +
            ", placedCredits=" + getPlacedCredits() +
            ", placedOn='" + getPlacedOn() + "'" +
            "}";
    }
}
