package sumdu.kvp.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sumdu.kvp.bot.mapper.TopicMapper;

@Configuration
public class ServiceConfig {

    @Bean
    public TopicMapper topicMapper() {
        return new TopicMapper();
    }
}
