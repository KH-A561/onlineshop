package net.thumbtack.school.onlineshop.model;

import java.util.Objects;

public class Deposit {
    private int amount;
    private Integer version;

    public Deposit() {}

    public Deposit(int amount) {
        this.amount = amount;
        this.version = 0;
    }

    public Deposit(int amount, Integer version) {
        this.amount = amount;
        this.version = version;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deposit)) return false;
        Deposit deposit = (Deposit) o;
        return getAmount() == deposit.getAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount());
    }
}
