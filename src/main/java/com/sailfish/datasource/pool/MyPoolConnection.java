package com.sailfish.datasource.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 实际的连接，类似mybatis的session或者JDBC的connection
 * @author sailfish
 * @create 2017-08-12-下午9:45
 */
public class MyPoolConnection {

    //具体的一个连接
    private Connection connection;
    private boolean isBusy = false; //不是繁忙，也即是没有连接



    public MyPoolConnection(Connection connection, boolean isBusy) {
        this.connection = connection;
        this.isBusy = isBusy;
    }

    //为了复用，只是关闭标识
    public void close(){
        isBusy = false;
    }

    public ResultSet query(String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }
}
