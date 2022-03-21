package com.polarbookshop.catalogservice.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The @ConfigurationProperties annotation marks this class as a source
 * for configuration properties starting with the prefix "polar".
 * Spring Boot will try to map all properties with that prefix to fields in the record.
 */
@ConfigurationProperties(prefix = "polar")
public class PolarProperties {

    String greeting;
}
