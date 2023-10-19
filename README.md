# jAmule - Amule client for Java

Note: this library is compatible for Amule 2.3.1 to 2.3.3

## Usage

This library can be imported through maven like this:
(Please check the latest version as tagged)
```gradle
implementation("com.vexdev:jamule:<LATEST_VERSION_HERE>")
```

## Releasing

To release a new version, run the following command:

```bash
./gradlew publish closeAndReleaseStagingRepository
``` 