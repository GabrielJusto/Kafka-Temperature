package br.com.bonatto.repository.factory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {


    DataSource dataSource;

    public ConnectionFactory()
    {
        ComboPooledDataSource comboPoolDatasource = new ComboPooledDataSource();
        comboPoolDatasource.setJdbcUrl("jdbc:mysql://localhost:3306/Stations");
        comboPoolDatasource.setUser("gabriel");
        comboPoolDatasource.setPassword("mastersql");

        comboPoolDatasource.setMaxPoolSize(10);

        this.dataSource = comboPoolDatasource;
    }

    public Connection recuperarConexao() throws SQLException {
        return this.dataSource.getConnection();
    }
}