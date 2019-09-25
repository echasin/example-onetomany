package com.innvo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Child.
 */
@Entity
@Table(name = "child")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name ="parent_id", nullable = false)
    @JsonIgnoreProperties("children")
    private Parent parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Child name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parent getParent() {
        return parent;
    }

    public Child parent(Parent parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Child)) {
            return false;
        }
        return id != null && id.equals(((Child) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Child{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
