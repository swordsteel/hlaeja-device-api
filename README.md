# Hlæja Device API

Classes and endpoints, to shape and to steer, Devices and sensors, their purpose made clear. Each message exchanged, each packet that waits, Travels through layers, as data translates. API pathways, structured and strong, Link devices to services, where data belongs. Bound by one purpose, data flows onward, Answering each call, steadfast and forward.

## Properties for deployment

| name                          | required | info                    |
|-------------------------------|----------|-------------------------|
| spring.profiles.active        | *        | Spring Boot environment |
| server.port                   | *        | HTTP port               |
| server.ssl.enabled            | *        | HTTP Enable SSL         |
| server.ssl.key-store          | *        | HTTP Keystore           |
| server.ssl.key-store-type     | *        | HTTP Cert Type          |
| server.ssl.key-store-password | **       | HTTP Cert Pass          |

Required: * can be stored as text, and ** need to be stored as secret.

## Releasing Service

Run `release.sh` script from `master` branch.

## Development Configuration

### Developer Keystore

1. Open `hosts` file:
   * On Unix-like systems (Linux, macOS), this directory is typically `/etc/hosts`.
   * On Windows, this directory is typically `%SystemRoot%\System32\drivers\etc\hosts`.

2. Add the following lines to the `hosts` file:
    ```text
    127.0.0.1	deviceapi		# Hlæja Device API
    ```

3. Generate Keystores
    ```shell
    keytool -genkeypair -alias device-api -keyalg RSA -keysize 2048 -validity 3650 -dname "CN=deviceapi" -keypass password -keystore ./certs/keystore.p12 -storetype PKCS12 -storepass password
    ```

4. Export the public certificate
    ```shell
    keytool -export -alias device-api -keystore ./certs/keystore.p12 -storepass password -file ./certs/device-api.cer -rfc
    ```

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
