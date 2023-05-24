package com.i0dev.globalcurrency.entity.object;

import lombok.Getter;

@Getter
public class DatabaseInformation {
    String address = "localhost";
    short port = 3306;
    String database = "globalcurrency";
    String username = "root";
    String password = "password";
}
