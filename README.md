# Hlæja Device API

Classes and endpoints, to shape and to steer, Devices and sensors, their purpose made clear. Each message exchanged, each packet that waits, Travels through layers, as data translates. API pathways, structured and strong, Link devices to services, where data belongs. Bound by one purpose, data flows onward, Answering each call, steadfast and forward.

## Properties for deployment

| name                   | required | info                    |
|------------------------|----------|-------------------------|
| spring.profiles.active | *        | Spring Boot environment |

Required: * can be stored as text, and ** need to be stored as secret.

## Releasing Service

Run `release.sh` script from `master` branch.

## Development Configuration

### Global gradle properties

To authenticate with Gradle to access repositories that require authentication, you can set your user and token in the `gradle.properties` file.

Here's how you can do it:

1. Open or create the `gradle.properties` file in your Gradle user home directory:
   - On Unix-like systems (Linux, macOS), this directory is typically `~/.gradle/`.
   - On Windows, this directory is typically `C:\Users\<YourUsername>\.gradle\`.
2. Add the following lines to the `gradle.properties` file:
    ```properties
    repository.user=your_user
    repository.token=your_token_value
    ```
   or use environment variables `REPOSITORY_USER` and `REPOSITORY_TOKEN`
