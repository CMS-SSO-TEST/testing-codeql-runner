package com.sample;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class SQLInjectionSample {

    DataSource dataSource;
    private String url = "jdbc:mysql://localhost/test";
    private String username = "";
    private String password = "";


    private Connection getConn() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public List<AccountDTO>
    unsafeFind(String customerId)
            throws SQLException {
        List<AccountDTO> accountDTOS = new ArrayList<>();
        String sql = "select "
                + "customer_id,acc_number,branch_id,balance "
                + "from Accounts where customer_id = '"
                + customerId
                + "'";
        Connection c = getConn();

        //////####Vulnerable Code#####//////
        // ResultSet rs = c.createStatement().executeQuery(sql);
        // while(rs.next()) {
        //     accountDTOS.add(new AccountDTO());
        // }

        //////####Fixed Code#####//////
        try (Statement stmt = c.createStatement()) {
          ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            accountDTOS.add(new AccountDTO());
          }
        }
        return accountDTOS;
    }
}
