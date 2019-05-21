package net.thumbtack.school.onlineshop.spring.handler.exception.error;

public enum ErrorCode {
    INVALID_REQUEST_ARGUMENT("Request contains invalid arguments", ""),
    ACCOUNT_LOGIN_ALREADY_EXISTS("Account with this login already exists", "login"),
    ACCOUNT_NOT_FOUND_BY_LOGIN("Account with this login cannot be found", "login"),
    ACCOUNT_WRONG_PASSWORD("Wrong account password", "password"),
    ACCOUNT_INCORRECT_TYPE_OF_USER("You should enter into proper type of account", "login"),
    ACCOUNT_NOT_LOGGED_IN("You have to log in before making any operations", "cookie"),
    CLIENT_INCORRECT_ID("Problem adding client to this account id", "account_id"),
    ADMINISTRATOR_INCORRECT_ID("Problem adding administrator to this account id", "account_id"),
    CLIENT_NOT_FOUND_BY_ACCOUNT_ID("There is no such client account", "account_id"),
    CLIENTS_NOT_FOUND("There are no clients on server yet", ""),
    ADMINISTRATOR_NOT_FOUND_BY_ACCOUNT_ID("There is no such administrator account", "account_id"),
    SESSION_NOT_FOUND("Your session cannot be found. Try to log in first.", "cookie"),
    CATEGORY_EXISTING_NAME("Category should have unique name", "name"),
    CATEGORY_NOT_FOUND("Category with specified id was not found", "id"),
    CATEGORY_PARENT_NOT_FOUND("Category's parentId was not found", "parentId"),
    ACCOUNT_INSERT_ERROR("", "id"),
    ACCOUNT_GET_BY_LOGIN_ERROR("", "login"),
    ACCOUNT_UPDATE_ERROR("", "id"),
    CATEGORY_FROM_PARENT_TO_CHILD("Parent category cannot be converted into subcategory", "parent_id"),
    CATEGORY_CANNOT_ADD_SUBCATEGORY_TO_CHILD("You can only add subcategory to a parent category", "parent_id"),
    PRODUCT_CANNOT_BE_ADDED("There is an error occurred while inserting product", ""),
    PRODUCT_CATEGORY_BATCH_INSERT_ERROR("", "id"),
    PRODUCT_NOT_FOUND("Product cannot be found", "id"),
    CATEGORY_FROM_CHILD_TO_PARENT("Subcategory cannot be converted to parent category", "parent_id"),
    DATABASE_ERROR("", ""),
    PURCHASE_INSUFFICIENT_FUNDS("There's not enough money on client's deposit", "count, price"),
    PURCHASE_PRODUCT_DATA_MISMATCH("Product data in Purchase does not correspond data in Database", ""),
    CLIENT_EMAIL_ALREADY_EXISTS("Client with this email already exists", "email"),
    CLIENT_PHONE_ALREADY_EXISTS("Client with this phone number already exists", "phone"),
    REQUEST_BODY_NOT_READABLE("", "HttpMessage"),
    REQUEST_BODY_INVALID_PARAMETERS("Your Http message has invalid body parameters", "HttpMessage"),
    HANDLER_NOT_FOUND("", "URL"), INTERNAL_ERROR("Internal Server Error Has occurred", ""),
    INVALID_ORDER("Products can only be ordered by `products` or `categories`", "order"),
    TRANSACTION_BLOCKED("Transaction has been interfered", ""),
    NO_CLIENTS_FOUND("There are no clients to get", ""),
    INVALID_PARAMETERS("Choose different parameters for this request", "");


    private String errorDescription;
    private String field;

    ErrorCode(String errorDescription, String field) {
        this.errorDescription = errorDescription;
        this.field = field;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
