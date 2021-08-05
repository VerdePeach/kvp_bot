package sumdu.kvp.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sumdu.kvp.bot.config.BotState;
import sumdu.kvp.bot.handler.BotEventHandler;
import sumdu.kvp.bot.model.User;

public class SumduKVPBot extends TelegramLongPollingBot {

    private static final String DEFAULT_ERROR_RESPONSE = "Щось пішло не так, спробуйте ще раз.";

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

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

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();
        if (!botEventHandler.isRequestValid(message)) {
            execute(new SendMessage(message.getChatId().toString(), DEFAULT_ERROR_RESPONSE));
        }
        var user = botEventHandler.getUser(message);
        var botState = botEventHandler.getBotState(message.getText());
        sendResponse(botState, user, message.getText());
    }

    public void sendResponse(BotState botState, User user, String message) throws TelegramApiException {
        switch (botState) {
            case ENTRY_POINT -> execute(botEventHandler.getStartAnswer(user));
            case HELP_MENU -> execute(botEventHandler.getHelp(user));
            case BACK -> response(user, botEventHandler.stepBack(user.getState()));
            case DOWNLOAD_PDF -> execute(botEventHandler.getFile(user));
            case SOME_TOPIC -> response(user, message);
        };
    }

    public void response(User user, String message) throws TelegramApiException {
        var topic = botEventHandler.getTopicByMessage(message);
        if (topic == null) {
            execute(botEventHandler.getStartAnswer(user));
        } else {
            switch (topic.getType()) {
                case MESSAGE -> execute(botEventHandler.getTopicsByFatherTopicAnswer(message, user));
                case PHOTO -> execute(botEventHandler.getTopicsByFatherTopicWithImageAnswer(message, user));
                case DOCUMENT -> execute(botEventHandler.getTopicsByFatherTopicAnswer(message, user));
                default -> execute(new SendMessage(user.getChatId().toString(), DEFAULT_ERROR_RESPONSE));
            };
        }
    }
}
