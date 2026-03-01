package com.dvaults.recipecatalogue.configs;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
public class FlywayConfigs {

  public FlywayConfigs(
      DataSource dataSource,
      @Value("${flyway.locations}") String locations,
      @Value("${flyway.baseline-on-migrate}") boolean baselineOnMigrate,
      @Value("${flyway.validate-migration-naming}") boolean validateMigrationNaming,
      @Value("${flyway.baseline-version:}") String baselineVersion,
      @Value("${flyway.target-version:}") String targetVersion
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

    config.load().migrate();

  }

}
