package com.github.fridujo.sample.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class LineItem {

    private final String description;
    @Id
    @GeneratedValue
    private Long id;

    public LineItem() {
        description = null;
    }

    public LineItem(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return Objects.equals(id, lineItem.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
