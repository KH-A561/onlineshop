package net.thumbtack.school.onlineshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private String cookie;
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session = (Session) o;
        return getCookie().equals(session.getCookie()) &&
                getAccount().equals(session.getAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCookie(), getAccount());
    }
}
