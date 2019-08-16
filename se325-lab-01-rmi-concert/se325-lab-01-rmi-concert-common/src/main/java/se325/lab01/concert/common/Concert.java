package se325.lab01.concert.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to represent a music concert.
 */
public class Concert implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String title;
    private LocalDateTime date;

    public Concert(long id, String title, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public Concert(String title, LocalDateTime date) {
        this(-1, title, date);
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Concert))
            return false;
        if (other == this)
            return true;

        Concert rhs = (Concert) other;
        return new EqualsBuilder().
                append(id, rhs.getId()).
                append(title, rhs.getTitle()).

                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(getClass().getName()).
                append(id).
                append(title).
                toHashCode();
    }
}
