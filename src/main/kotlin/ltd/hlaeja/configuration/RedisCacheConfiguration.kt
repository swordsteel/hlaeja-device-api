package ltd.hlaeja.configuration

import java.time.Duration
import ltd.hlaeja.exception.CacheException
import ltd.hlaeja.property.CacheProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisCacheConfiguration(
    private val cacheProperty: CacheProperty,
) {

    @Bean
    fun cacheManager(
        redisTemplate: RedisTemplate<String, String>,
    ): RedisCacheManager = redisTemplate.connectionFactory
        ?.let { RedisCacheManager.builder(it).cacheDefaults(getRedisCacheConfiguration()).build() }
        ?: throw CacheException("Redis connection factory is not set")

    private fun getRedisCacheConfiguration(): RedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(cacheProperty.timeToLive))
}
