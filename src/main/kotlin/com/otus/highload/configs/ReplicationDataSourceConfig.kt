package com.otus.highload.configs

import org.flywaydb.database.postgresql.PostgreSQLType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

enum class DataSourceType {
  READ_ONLY,
  READ_WRITE
}

@Configuration
class MasterDatasourceConfiguration {
  @Bean
  @ConfigurationProperties("spring.datasource")
  fun writeDataSourceProperties(): DataSourceProperties {
    return DataSourceProperties()
  }
}

@Configuration
class SlaveDatasourceConfiguration {
  @Bean
  @ConfigurationProperties("spring.datasource.slave")
  fun readDataSourceProperties(): DataSourceProperties {
    return DataSourceProperties()
  }
}


class ReplicationRoutingDataSource : AbstractRoutingDataSource() {
  override fun determineCurrentLookupKey(): Any {
    return if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) DataSourceType.READ_ONLY else DataSourceType.READ_WRITE
  }
}

@Configuration
class ReplicationDataSourceConfig {
  @Autowired
  lateinit var env: Environment

  @Primary
  @Bean
  @DependsOn("writeDataSource", "readDataSource", "routingDataSource")
  fun dataSource(): DataSource {
    return LazyConnectionDataSourceProxy(routingDataSource())
  }

  @Bean
  fun routingDataSource(): DataSource {
    val routingDataSource = ReplicationRoutingDataSource()

    val dataSourceMap: MutableMap<Any, Any> = HashMap()

    dataSourceMap[DataSourceType.READ_WRITE] = writeDataSource()
    dataSourceMap[DataSourceType.READ_ONLY] = readDataSource()

    routingDataSource.setTargetDataSources(dataSourceMap)
    routingDataSource.setDefaultTargetDataSource(writeDataSource())

    return routingDataSource
  }

  @Bean
  fun writeDataSource(): DataSource {
    val dataSource = DataSourceBuilder.create()

    dataSource.password( env.getProperty("spring.datasource.password"))
    dataSource.username( env.getProperty("spring.datasource.username"))
    dataSource.driverClassName( env.getProperty("spring.datasource.driverClassName"))
    dataSource.url( env.getProperty("spring.datasource.url"))

    return dataSource.build()
  }

  @Bean
  fun readDataSource(): DataSource {
    val dataSource = DataSourceBuilder.create()

    dataSource.password( env.getProperty("spring.datasource.slave.password"))
    dataSource.username( env.getProperty("spring.datasource.slave.username"))
    dataSource.driverClassName( env.getProperty("spring.datasource.slave.driverClassName"))
    dataSource.url( env.getProperty("spring.datasource.slave.url"))

    return dataSource.build()
  }
}