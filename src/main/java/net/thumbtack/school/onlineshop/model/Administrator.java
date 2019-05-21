package net.thumbtack.school.onlineshop.model;

import java.util.Objects;

public class Administrator extends Account {
    private String position;

    public Administrator() {
    }

    public Administrator(int userId, String firstName, String lastName, String patronymic, String login, String password, String position) {
        super(userId, firstName, lastName, patronymic, login, password, AccountType.ADMINISTRATOR);
        this.position = position;
    }

    public Administrator(String firstName, String lastName, String patronymic, String login, String password, String position) {
        this(0, firstName, lastName, patronymic, login, password, position);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Administrator)) return false;
        if (!super.equals(o)) return false;
        Administrator that = (Administrator) o;
        return getPosition().equals(that.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPosition());
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "position='" + position + '\'' +
                '}';
    }
}
