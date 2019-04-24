package cli.config;

import cli.ssh.JSchClientFactory;
import cli.ssh.JSchClientPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
public class SshConfiguration {

    @Bean
    public JSchClientFactory jSchClientFactory() {
        return new JSchClientFactory();
    }

    @Bean
    public JSchClientPool jSchClientPool(JSchClientFactory factory) {
        GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
        config.setMaxTotal(10);
        config.setMaxTotalPerKey(5);
        config.setMaxIdlePerKey(5);
        config.setMinIdlePerKey(1);
        config.setJmxEnabled(false);

        return new JSchClientPool(factory, config);
    }
}
