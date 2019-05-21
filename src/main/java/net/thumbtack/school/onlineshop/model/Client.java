package net.thumbtack.school.onlineshop.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.thumbtack.school.onlineshop.spring.dto.request.administrator.RegisterAdminDtoRequest;
import net.thumbtack.school.onlineshop.spring.dto.request.client.RegisterClientDtoRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Component
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client extends Account {
    private String email;
    private String address;
    private String phone;
    private Deposit deposit;
    private List<Product> productBasket;
    private List<Purchase> purchases;

    public Client() {
        super();
        this.deposit = new Deposit(0);
        this.productBasket = new ArrayList<>();
        this.purchases = new ArrayList<>();
    }

    public Client(String firstName, String lastName, String patronymic, String login, String password, String email, String address, String phone) {
        super(firstName, lastName, patronymic, login, password, AccountType.CLIENT);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = new Deposit(0);
        this.productBasket = new ArrayList<>();
        this.purchases = new ArrayList<>();
    }

    public Client(int id, String firstName, String lastName, String patronymic, String login, String password, String email, String address, String phone) {
        super(id, firstName, lastName, patronymic, login, password, AccountType.CLIENT);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = new Deposit(0);
        this.productBasket = new ArrayList<>();
        this.purchases = new ArrayList<>();
    }

    public void setDeposit(int deposit) {
        this.deposit = new Deposit(deposit);
    }

    public void decreaseDeposit(int amount) {
        deposit.setAmount(deposit.getAmount() - amount);
    }
}
