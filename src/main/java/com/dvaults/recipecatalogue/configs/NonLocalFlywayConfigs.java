package com.dvaults.recipecatalogue.configs;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Configuration
@Profile("!local")
public class NonLocalFlywayConfigs {

  public NonLocalFlywayConfigs(
      DataSource dataSource,
      PasswordEncoder passwordEncoder,
      @Value("${flyway.locations}") String locations,
      @Value("${flyway.baseline-on-migrate}") boolean baselineOnMigrate,
      @Value("${flyway.validate-migration-naming}") boolean validateMigrationNaming,
      @Value("${flyway.baseline-version:}") String baselineVersion,
      @Value("${flyway.target-version:}") String targetVersion,
      @Value("${flyway.placeholders.admin_username}") String adminUsername,
      @Value("${flyway.placeholders.admin_password}") String adminPassword
  ) {

    FluentConfiguration config = Flyway.configure()
        .baselineOnMigrate(baselineOnMigrate)
        .dataSource(dataSource)
        .locations(locations)
        .validateMigrationNaming(validateMigrationNaming);

    if (StringUtils.hasText(baselineVersion)) {
      config.baselineVersion(baselineVersion);
    }
    if (StringUtils.hasText(targetVersion)) {
      config.target(targetVersion);
    }

    config.placeholders(Map.of(
        "ADMIN_USERNAME", adminUsername,
        "ADMIN_PASSWORD_HASH", Objects.requireNonNull(passwordEncoder.encode(adminPassword))
    ));

    config.load().migrate();

  }

}
