package com.polarbookshop.catalogservice.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

/**
 * This class is used for JDBC auditing, useful to know the creation date
 * for each row in a table and the date of when it’s been updated last.
 * After securing an application with authentication and authorization,
 * you can register who created each entity and recently updated it.
 * All of that is called "database auditing".
 */
@Configuration
@EnableJdbcAuditing         // Enables auditing for persistent entities
public class DataConfig {
}
