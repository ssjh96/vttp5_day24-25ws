package vttp5.paf.day24_25ws.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    
    // Load Redis properties from application.properties
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.username}")
    private String redisUserName;

    @Value("${spring.data.redis.password}")
    private String redisPassword;


    // Set up connection to Redis Server
    public RedisConnectionFactory createConnectionFactory()
    {
        // Configure standalone redis
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(0); // use default DB

        // Set username and apssword if provided (for cloud-base Redis like Railway)
        if(!redisUserName.equals("") && !redisPassword.equals(""))
        {
            config.setUsername(redisUserName);
            config.setPassword(redisPassword);
        }

        // Create redis connection factory using Jedis client
        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config, jedisClient);
        jedisConnectionFactory.afterPropertiesSet();

        return jedisConnectionFactory;
    }

    // Create RedisTemplate bean to interact with redis
    @Bean("myredis")
    public RedisTemplate<String, String> redisTemplate()
    {
        // Use connection factory to create the RedisTemplate
        RedisConnectionFactory redisConnectionFactory = createConnectionFactory();

        // Set the connection factory
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // Use String serialisers for key and values
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate; // Return configured redis Template
    }
}
