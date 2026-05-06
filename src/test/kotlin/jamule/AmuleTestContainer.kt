package jamule

import org.slf4j.Logger
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.time.Duration

private const val AMULE_EC_PORT = 4712

internal fun newAmuleContainer(logger: Logger? = null): GenericContainer<*> =
    GenericContainer(DockerImageName.parse("m4dfry/amule-adunanza"))
        .withExposedPorts(AMULE_EC_PORT)
        .withStartupAttempts(3)
        .waitingFor(Wait.forListeningPort())
        .withStartupTimeout(Duration.ofMinutes(2))
        .apply {
            if (logger != null) {
                withLogConsumer(Slf4jLogConsumer(logger))
            }
        }

internal fun GenericContainer<*>.amuleEcPort(): Int = getMappedPort(AMULE_EC_PORT)
