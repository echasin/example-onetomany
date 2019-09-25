package com.innvo.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Parent.
 */
@Entity
@Table(name = "parent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Parent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Child> children = new HashSet<>();

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

    public Parent name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Child> getChildren() {
        return children;
    }

    public Parent children(Set<Child> children) {
        this.children = children;
        return this;
    }

    public Parent addChild(Child child) {
        this.children.add(child);
        child.setParent(this);
        return this;
    }

    public Parent removeChild(Child child) {
        this.children.remove(child);
        child.setParent(null);
        return this;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parent)) {
            return false;
        }
        return id != null && id.equals(((Parent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Parent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
