package net.thumbtack.school.onlineshop.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Account {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private AccountType type;

    public Account() {}

    public Account(String firstName, String lastName, String patronymic, String login, String password, AccountType type) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getId() == account.getId() &&
                getFirstName().equals(account.getFirstName()) &&
                getLastName().equals(account.getLastName()) &&
                Objects.equals(getPatronymic(), account.getPatronymic()) &&
                getLogin().equals(account.getLogin()) &&
                getPassword().equals(account.getPassword()) &&
                getType() == account.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getPatronymic(), getLogin(), getPassword(), getType());
    }
}
