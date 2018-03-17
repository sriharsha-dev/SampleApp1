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
 * A Items.
 */
@Entity
@Table(name = "items")
public class Items implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "total_placed_credits")
    private Long totalPlacedCredits;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "commission")
    private Double commission;

    @ManyToOne
    private Shop shop;

    @OneToOne
    @JoinColumn(unique = true)
    private Options finalOption;

    @OneToOne
    @JoinColumn(unique = true)
    private Shop createdBy;

    @OneToMany(mappedBy = "items")
    @JsonIgnore
    private Set<Options> options = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public Items itemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Items createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public Items name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Items createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Long getTotalPlacedCredits() {
        return totalPlacedCredits;
    }

    public Items totalPlacedCredits(Long totalPlacedCredits) {
        this.totalPlacedCredits = totalPlacedCredits;
        return this;
    }

    public void setTotalPlacedCredits(Long totalPlacedCredits) {
        this.totalPlacedCredits = totalPlacedCredits;
    }

    public Boolean isStatus() {
        return status;
    }

    public Items status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getCommission() {
        return commission;
    }

    public Items commission(Double commission) {
        this.commission = commission;
        return this;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Shop getShop() {
        return shop;
    }

    public Items shop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Options getFinalOption() {
        return finalOption;
    }

    public Items finalOption(Options options) {
        this.finalOption = options;
        return this;
    }

    public void setFinalOption(Options options) {
        this.finalOption = options;
    }

    public Shop getCreatedBy() {
        return createdBy;
    }

    public Items createdBy(Shop shop) {
        this.createdBy = shop;
        return this;
    }

    public void setCreatedBy(Shop shop) {
        this.createdBy = shop;
    }

    public Set<Options> getOptions() {
        return options;
    }

    public Items options(Set<Options> options) {
        this.options = options;
        return this;
    }

    public Items addOptions(Options options) {
        this.options.add(options);
        options.setItems(this);
        return this;
    }

    public Items removeOptions(Options options) {
        this.options.remove(options);
        options.setItems(null);
        return this;
    }

    public void setOptions(Set<Options> options) {
        this.options = options;
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
        Items items = (Items) o;
        if (items.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), items.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Items{" +
            "id=" + getId() +
            ", itemId=" + getItemId() +
            ", createdBy=" + getCreatedBy() +
            ", name='" + getName() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", totalPlacedCredits=" + getTotalPlacedCredits() +
            ", status='" + isStatus() + "'" +
            ", commission=" + getCommission() +
            "}";
    }
}
