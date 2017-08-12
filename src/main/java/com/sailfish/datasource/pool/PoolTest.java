package com.sailfish.datasource.pool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sailfish
 * @create 2017-08-12-下午10:47
 */
public class PoolTest {

    static MyPool myPool = MyPoolConnectionFactory.getInstance();

    public static void main(String[] args) {


        ExecutorService service = Executors.newFixedThreadPool(20);
        for (int i = 0; i<20; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    //要创建连接池
                    for (int i = 0; i<1000; i++) {
                        MyPoolConnection connection = myPool.getConnection();
                        ResultSet query = connection.query("select * from sys_role");
                        try {
                            while (query.next()) {
                                System.out.println(query.getString("role_name") + ",使用连接:" + connection.getConnection());
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        connection.close();
                    }
                }
            });
        }

    }
}

/*
管理员,使用连接:com.mysql.jdbc.JDBC4Connection@52d7ed98
普通用户,使用连接:com.mysql.jdbc.JDBC4Connection@52d7ed98
管理员,使用连接:com.mysql.jdbc.JDBC4Connection@54fd0439
普通用户,使用连接:com.mysql.jdbc.JDBC4Connection@54fd0439
Sat Aug 12 23:05:58 CST 2017 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
管理员,使用连接:com.mysql.jdbc.JDBC4Connection@653c8c96
普通用户,使用连接:com.mysql.jdbc.JDBC4Connection@653c8c96
Sat Aug 12 23:05:58 CST 2017 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
管理员,使用连接:com.mysql.jdbc.JDBC4Connection@31396499
普通用户,使用连接:com.mysql.jdbc.JDBC4Connection@31396499
管理员,使用连接:com.mysql.jdbc.JDBC4Connection@4e3d6764
普通用户,使用连接:com.mysql.jdbc.JDBC4Connection@4e3d6764
管理员,使用连接:com.mysql.jdbc.JDBC4Connection@30e7e137
普通用户,使用连接:com.mysql.jdbc.JDBC4Connection@30e7e137
com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: Data source rejected establishment of connection,  message from server: "Too many connections"
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)

 */