package sumdu.kvp.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sumdu.kvp.bot.SumduKVPBot;
import sumdu.kvp.bot.handler.BotEventHandler;
import sumdu.kvp.bot.handler.CallbackHandler;

@Configuration
public class BotConfig {
    @Bean
    public SumduKVPBot sumduKVPBot() {
        return new SumduKVPBot();
    }

    @Bean
    public BotEventHandler botEventHandler() {
        return new BotEventHandler();
    }

    @Bean
    public CallbackHandler callbackHandler() {
        return new CallbackHandler();
    }
}
