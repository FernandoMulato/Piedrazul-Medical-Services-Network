package com.piedrazul.Infrastructure.config;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseConnection {
  Connection connect() throws SQLException;
}