/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Caio Gustavo
 */
public class ConexaoJDBC {
    
    public static Connection getConnection() throws ClassNotFoundException,
            SQLException{
        
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost:5432/estimulos_db";
        String user = "postgres";
        String password = "123Fatec";
        Class.forName( driver );
        Connection conn = 
                        DriverManager.getConnection( url, user, password);

        return conn;
    }
    
}
