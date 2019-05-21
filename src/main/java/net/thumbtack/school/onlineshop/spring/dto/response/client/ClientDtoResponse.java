package net.thumbtack.school.onlineshop.spring.dto.response.client;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.onlineshop.model.AccountType;
import net.thumbtack.school.onlineshop.spring.view.AsAdmin;
import net.thumbtack.school.onlineshop.spring.view.AsClient;
import net.thumbtack.school.onlineshop.spring.view.View;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDtoResponse {
    @JsonView(View.class)
    private int id;

    @JsonView(View.class)
    private String firstName;

    @JsonView(View.class)
    private String lastName;

    @JsonView(View.class)
    private String patronymic;

    @JsonView(View.class)
    private String email;

    @JsonView(View.class)
    private String address;

    @JsonView(View.class)
    private String phone;

    @JsonView(AsClient.class)
    private int deposit;

    @JsonView(AsAdmin.class)
    private AccountType userType = AccountType.CLIENT;

    public ClientDtoResponse(int id, String firstName, String lastName, String patronymic, String email, String address, String phone, int deposit) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.deposit = deposit;
    }
}
