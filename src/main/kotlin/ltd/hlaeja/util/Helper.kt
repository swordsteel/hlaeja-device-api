package ltd.hlaeja.util

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

fun logCall(url: String) {
    log.debug("calling: {}", url)
}
