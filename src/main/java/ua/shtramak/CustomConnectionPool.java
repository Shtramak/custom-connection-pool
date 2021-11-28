package ua.shtramak;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CustomConnectionPool {
    private static final int DEFAULT_POOL_SIZE = 10;
    private final DataSource dataSource;
    private final int poolSize;
    private Queue<Connection> connectionPool;

    public CustomConnectionPool(DataSource dataSource) {
        this(dataSource, DEFAULT_POOL_SIZE);
    }

    public CustomConnectionPool(DataSource dataSource, int poolSize){
        this.dataSource = dataSource;
        this.poolSize = poolSize;
    }

    public Connection getConnection(){
        return connectionPool.poll();
    }

    public int getPoolSize() {
        return poolSize;
    }

    private void initPool() throws SQLException {
        connectionPool = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < poolSize; i++) {
            var pooledConnection = new PooledConnection(connectionPool, dataSource.getConnection());
            connectionPool.add(pooledConnection);
        }
    }
}
