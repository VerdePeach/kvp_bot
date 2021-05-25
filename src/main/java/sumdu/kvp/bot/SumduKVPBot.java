package sumdu.kvp.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Update;
import sumdu.kvp.bot.handler.BotEventHandler;


@ConfigurationProperties(prefix = "telegrambot")
public class SumduKVPBot extends TelegramWebhookBot {

    private String token;
    private String username;
    private String webHookPath;

    private BotEventHandler botEventHandler;

    @Autowired
    public void setBotEventHandler(BotEventHandler botEventHandler) {
        this.botEventHandler = botEventHandler;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return botEventHandler.handleUpdate(update);
    }

}
