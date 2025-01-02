# Hlæja Device API

Classes and endpoints, to shape and to steer, Devices and sensors, their purpose made clear. Each message exchanged, each packet that waits, Travels through layers, as data translates. API pathways, structured and strong, Link devices to services, where data belongs. Bound by one purpose, data flows onward, Answering each call, steadfast and forward.

## Properties for deployment

| name                                         | required | info                                         |
|----------------------------------------------|:--------:|----------------------------------------------|
| spring.profiles.active                       | &check;  | Spring Boot environment                      |
| server.port                                  | &check;  | HTTP port                                    |
| server.ssl.enabled                           | &check;  | HTTP Enable SSL                              |
| server.ssl.key-store                         | &check;  | HTTP Keystore                                |
| server.ssl.key-store-type                    | &check;  | HTTP Cert Type                               |
| server.ssl.key-store-password                | &cross;  | HTTP Cert Pass                               |
| spring.cache.type                            |          | Cache type (redis)                           |
| spring.data.redis.host                       | &check;  | Redis host                                   |
| spring.data.redis.port                       |          | Redis port                                   |
| spring.data.redis.database                   | &check;  | Redis database                               |
| spring.data.redis.password                   | &cross;  | Redis password                               |
| cache.time-to-live                           |          | Cache time to live (minutes)                 |
| jwt.public-key                               | &check;  | JWT public key                               |
| device-registry.url                          | &check;  | Device Register URL                          |
| device-data.url                              | &check;  | Device Data URL                              |
| device-configuration.url                     | &check;  | Device Configuration URL                     |
| management.influx.metrics.export.api-version |          | InfluxDB API version                         |
| management.influx.metrics.export.enabled     |          | Enable/Disable exporting metrics to InfluxDB |
| management.influx.metrics.export.bucket      | &check;  | InfluxDB bucket name                         |
| management.influx.metrics.export.org         | &check;  | InfluxDB organization                        |
| management.influx.metrics.export.token       | &cross;  | InfluxDB token                               |
| management.influx.metrics.export.uri         | &check;  | InfluxDB URL                                 |
| management.metrics.tags.application          | &check;  | Application instance tag for metrics         |

*Required: &check; can be stored as text, and &cross; need to be stored as secret.*

## Releasing Service

Run `release.sh` script from `master` branch.

## Development Configuration

### Developer Keystore

We use a keystore to enable HTTPS for our API. To set up your developer environment for local development, please refer to [generate keystore](https://github.com/swordsteel/hlaeja-development/blob/master/doc/keystore.md) documentation. When generating and exporting the certificate for local development, please store it in the `./cert/keystore.p12` folder at the project root.

### Public RSA Key

This service uses the public key from **[Hlæja Device Register](https://github.com/swordsteel/hlaeja-device-registry)** to identify devices. To set up device identification for local development, copy the `public_key.pem` file from the `./cert` directory in **Hlæja Device Register** into the `./cert` directory of this project.

*Note: For more information on generating RSA keys, please refer to our [generate RSA key](https://github.com/swordsteel/hlaeja-development/blob/master/doc/rsa_key.md) documentation.*

### Global Settings

This services rely on a set of global settings to configure development environments. These settings, managed through Gradle properties or environment variables.

*Note: For more information on global properties, please refer to our [global settings](https://github.com/swordsteel/hlaeja-development/blob/master/doc/global_settings.md) documentation.*

#### Gradle Properties

```properties
repository.user=your_user
repository.token=your_token_value
influxdb.token=your_token_value
```

#### Environment Variables

```properties
REPOSITORY_USER=your_user
REPOSITORY_TOKEN=your_token_value
INFLUXDB_TOKEN=your_token_value
```
