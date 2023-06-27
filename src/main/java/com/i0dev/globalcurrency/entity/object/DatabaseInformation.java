package com.i0dev.globalcurrency.entity.object;

import lombok.Getter;

@Getter
public class DatabaseInformation {
    String address = "localhost";
    short port = 3306;
    String database = "globalcurrency";
    String username = "root";
    String password = "password";
    long reconnectToDatabaseEveryMillis = 60 * 60 * 1000; // 1 hour
}
