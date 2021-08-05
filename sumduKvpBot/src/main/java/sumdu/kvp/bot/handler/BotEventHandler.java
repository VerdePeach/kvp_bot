package sumdu.kvp.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sumdu.kvp.bot.config.BotState;
import sumdu.kvp.bot.model.Topic;
import sumdu.kvp.bot.model.User;
import sumdu.kvp.bot.service.TopicService;
import sumdu.kvp.bot.service.UserService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotEventHandler {

    private TopicService topicService;
    private UserService userService;

    @Autowired
    public void setTopicService(TopicService topicService) {
        this.topicService = topicService;
    }

    @Autowired
    void setUserService(UserService userService) {
        this.userService = userService;
    }

    //TODO: if callback
    public boolean isRequestValid(Message message) {
        return message != null && message.hasText();
    }

    public User getUser(Message message) {
        var telegramUser = message.getFrom();
        var user = userService.findByUsername(telegramUser.getUserName());
        if (user == null) {
            var newUser = new User();
            newUser.setUsername(telegramUser.getUserName());
            newUser.setChatId(message.getChatId());
            newUser.setFirstName(telegramUser.getFirstName());
            //TODO: state could be incorrect. if user did not use keyboard.
            newUser.setState(message.getText());
            newUser.setTelegramId(telegramUser.getId());
            user = userService.save(newUser);
        } else {
            user.setTelegramId(telegramUser.getId());
            user.setUsername(telegramUser.getUserName());
            user.setChatId(message.getChatId());
            user.setFirstName(telegramUser.getFirstName());
        }
        return user;
    }

//    private CallbackHandler callbackHandler;

//    @Autowired
//    public void setCallbackHandler(CallbackHandler callbackHandler) {
//        this.callbackHandler = callbackHandler;
//    }

    public BotState getBotState(String strState) {
        return switch (strState.toUpperCase()) {
            case "/START" -> BotState.ENTRY_POINT;
            case "/HELP" -> BotState.HELP_MENU;
            case "ЗАВАНТАЖИТИ .PDF ФАЙЛ" -> BotState.DOWNLOAD_PDF;
            case "НАЗАД" -> BotState.BACK;
            default -> BotState.SOME_TOPIC;
        };
    }

    public SendMessage getStartAnswer(User user) {
        updateUserData(user);
        var topics = topicService.findBasicTopics();
        var sendMessage = new SendMessage(user.getChatId().toString(), "Використовуй клавіатуру для вибору розділу.");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(getTopicReplyKeyboard(topics));
        return sendMessage;
    }

    public String stepBack(String lastUserState) throws TelegramApiException {
        var topicByName = topicService.findTopicByName(lastUserState);
        if (topicByName == null) {
            return "";
        }
        var fatherTopic = topicService.findFatherByChild(topicByName.getId());

        return fatherTopic == null ? "" : fatherTopic.getName();
    }

    public SendMediaGroup getHelp(User user) {
        var firstPhoto = new InputMediaPhoto();
        firstPhoto.setMedia(new File("src/main/resources/help1.png"), "help1");
        firstPhoto.setCaption("1 - Поле вводу. При вводі символу \"/\" з'являються сталі комади боту.(/start)" +
                "\n2 - Команди боту." +
                "\n3 - Кнопка згортання та розгортання клавіатури Бота." +
                "\n4 - Теми до вивчення. При написканні бот відправить вам матеріали." +
                "\n5 - Кнопка для завантаження pdf файлу з вибраною тамою." +
                "\n6 - Кнопка повернення до попередньої теми.");
        var secondPhoto = new InputMediaPhoto();

        secondPhoto.setMedia(new File("src/main/resources/help2.png"), "help2");

        return new SendMediaGroup(user.getChatId().toString(), Arrays.asList(firstPhoto, secondPhoto));
    }


    public SendDocument getFile(User user) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(user.getChatId().toString());
        var dbUser = userService.findByTelegramId(user.getTelegramId());
        if (dbUser == null) {
            //TODO: default response. file can not be empty!
            sendDocument.setCaption("Something goes wrong!");
            return sendDocument;
        }
        var lastTopic = topicService.findTopicByName(dbUser.getState().toString());
        if (lastTopic == null) {
            //TODO: default response. file can not be empty!
            sendDocument.setCaption("Something goes wrong!");
            return sendDocument;
        }
        sendDocument.setDocument(new InputFile(new File("src/main/resources/" + lastTopic.getFile().getFileName())));
        sendDocument.setCaption(lastTopic.getName());
        sendDocument.setReplyMarkup(getTopicReplyKeyboardWithBackStepAndFile(topicService.findByFatherId(lastTopic.getId())));
        return sendDocument;
    }

    public SendMessage getTopicsByFatherTopicAnswer(String state, User user) {
        user.setState(state);
        updateUserData(user);
        var topicDTOResponse = topicService.findTopicByName(state.toString());
        if (topicDTOResponse == null) {
            return null; //TODO: return default state of previous list of buttons.
        }
        var topicDTOs = topicService.findByFatherId(topicDTOResponse.getId());
        if (topicDTOs == null) {
            return null; //TODO: return default state.
        }
        var sendMessage = new SendMessage(user.getChatId().toString(), topicDTOResponse.getText());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(getTopicReplyKeyboardWithBackStepAndFile(topicDTOs));
        return sendMessage;
    }

    public SendPhoto getTopicsByFatherTopicWithImageAnswer(String state, User user) {
        user.setState(state);
        updateUserData(user);
        var topicDTOResponse = topicService.findTopicByName(state.toString());
        if (topicDTOResponse == null) {
            return null; //TODO: return default state of previous list of buttons.
        }
        var topicDTOs = topicService.findByFatherId(topicDTOResponse.getId());
        if (topicDTOs == null) {
            return null; //TODO: return default state.
        }
        var sendPhoto = new SendPhoto(user.getChatId().toString(), new InputFile(new File("src/main/resources/" + topicDTOResponse.getImage().getFileName())));
        sendPhoto.setCaption(topicDTOResponse.getText());
        sendPhoto.setReplyMarkup(getTopicReplyKeyboardWithBackStepAndFile(topicDTOs));

        return sendPhoto;
    }

    private ReplyKeyboardMarkup getTopicReplyKeyboard(List<Topic> topics) {
        return buildTopicReplyKeyboard(topics);
    }

    private ReplyKeyboardMarkup getTopicReplyKeyboardWithBackStepAndFile(List<Topic> topics) {
        var replyKeyboard = buildTopicReplyKeyboard(topics);
        var keyboard = replyKeyboard.getKeyboard();

        var keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(BotState.DOWNLOAD_PDF.toString()));
        keyboardRow.add(new KeyboardButton(BotState.BACK.toString()));
        keyboard.add(keyboardRow);

        replyKeyboard.setKeyboard(keyboard);
        return replyKeyboard;
    }

    private ReplyKeyboardMarkup buildTopicReplyKeyboard(List<Topic> topics) {
        var replyKeyboard = new ReplyKeyboardMarkup();

        replyKeyboard.setSelective(true);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(true);

        var keyboard = new ArrayList<KeyboardRow>();
        topics.forEach(t -> {
            var keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton(t.getName()));
            keyboard.add(keyboardRow);
        });
        replyKeyboard.setKeyboard(keyboard);
        return replyKeyboard;
    }

//    private InlineKeyboardMarkup buildTopicReplyKeyboard() {
//        var replyKeyboard = new InlineKeyboardMarkup();
//        replyKeyboard.setKeyboard(Arrays.asList(Arrays.asList(new InlineKeyboardButton("hi"))));
//        return replyKeyboard;
//    }

    private void updateUserData(User user) {
        userService.update(user);
    }

    public Topic getTopicByMessage(String message) {
        return topicService.findTopicByName(message);
    }
}
