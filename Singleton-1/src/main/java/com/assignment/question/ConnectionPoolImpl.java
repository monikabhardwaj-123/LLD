package com.assignment.question;
import java.util.*;

public class ConnectionPoolImpl implements ConnectionPool {
    private static ConnectionPoolImpl instance = null;
    private int maxConnections;

    private List<DatabaseConnection> availableConnections;
    private List<DatabaseConnection> usedConnections;
    
    private ConnectionPoolImpl(int maxConnections) {
        this.maxConnections = maxConnections;
        this.availableConnections = new ArrayList<>();
        this.usedConnections = new ArrayList<>();
        initializePool();
    }

    public static ConnectionPoolImpl getInstance(int maxConnections) {
        if (instance == null) {
            instance = new ConnectionPoolImpl(maxConnections);
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }
    @Override
    public void initializePool() {
        for(int i=0; i<maxConnections; i++){
            availableConnections.add(new DatabaseConnection());
        }
    }

    @Override
    public DatabaseConnection getConnection() {
        if(availableConnections.isEmpty()){
            return null;
        }
        DatabaseConnection connection = availableConnections.remove(0);
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public void releaseConnection(DatabaseConnection connection) {
        if(connection==null){
            return;
        }
        usedConnections.remove(connection);
        availableConnections.add(connection);
    }

    @Override
    public int getAvailableConnectionsCount() {
        return availableConnections.size();
    }

    @Override
    public int getTotalConnectionsCount() {
        return availableConnections.size() + usedConnections.size();
    }
}