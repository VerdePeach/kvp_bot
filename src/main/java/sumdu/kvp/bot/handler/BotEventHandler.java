package sumdu.kvp.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import sumdu.kvp.bot.dto.TopicDTOResponse;
import sumdu.kvp.bot.config.BotState;
import sumdu.kvp.bot.model.User;
import sumdu.kvp.bot.service.TopicService;
import sumdu.kvp.bot.service.UserService;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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


//    private CallbackHandler callbackHandler;

//    @Autowired
//    public void setCallbackHandler(CallbackHandler callbackHandler) {
//        this.callbackHandler = callbackHandler;
//    }

    public BotApiMethod handleUpdate(Update update) {
//        BotApiMethod<?> reply = null;

//        if (update.hasCallbackQuery()) {
//            var callback = update.getCallbackQuery();
//            return callbackHandler.prosesCallbackQuery(callback);
//        }

        Message message = update.getMessage();

        if (message != null && message.hasText()) {
//            log.info("New message from User:{}, chatId: {},  with text: {}",
//                    message.getFrom().getUserName(), message.getChatId(), message.getText());

            return handleInputMessage(update, new SendMessage());
        }

//        SendDocument sendDocumentRequest = new SendDocument();
//        sendDocumentRequest.setChatId(message.getChatId().toString());
//        var f = new InputFile();
//        f.setMedia(new File("/s.png"), "hk");
//        sendDocumentRequest.setDocument(f);
//        sendDocumentRequest.setCaption("caption");
//        return sendDocumentRequest;

//        return new SendPhoto(message.getChatId().toString(), new InputFile(new File("/s.png")));
        return null;
    }

    private BotApiMethod<?> handleInputMessage(Update update, SendMessage sendMessage) {
        var message = update.getMessage();
        sendMessage.setChatId(message.getChatId().toString());

        var telegramUser = update.getMessage().getFrom();
        var user = userService.findByUsername(telegramUser.getUserName());

        if (user == null) {
            var newUser = new User();
            newUser.setUsername(telegramUser.getUserName());
            newUser.setChatId(message.getChatId());
            newUser.setFirstName(telegramUser.getFirstName());
            newUser.setState(BotState.ENTRY_POINT);
            user = userService.save(newUser);
        } else {
            user.setUsername(telegramUser.getUserName());
            user.setChatId(message.getChatId());
            user.setFirstName(telegramUser.getFirstName());
        }

        var botState = getBotState(message.getText());
        //TODO: Save bot state for user;
        //TODO: Get proper answer (build it);


        return setupResponse(botState, sendMessage, user);
    }

    private BotState getBotState(String strState) {
        return switch (strState.toUpperCase()) {
            case "/START" -> BotState.ENTRY_POINT;
            case "ТАКТИЧНА ПІДГОТОВКА" -> BotState.A;
            case "ОБОВ`ЯЗКИ КОМАНДИРА ВІДДІЛЕННЯ" -> BotState.A1;

            case "МЕХАНІЗОВАНЕ ВІДДІЛЕННЯ" -> BotState.A2;
            case "МЕХАНІЗОВАНЕ ВІДДІЛЕННЯ НА БМП" -> BotState.A2B1;
            case "МЕХАНІЗОВАНЕ ВІДДІЛЕННЯ НА БТР" -> BotState.A2B2;

            case "РОЗТАШУВАННЯ ВІДДІЛЕННЯ НА МІСЦІ" -> BotState.A3;
            case "ДІЇ ВІДДІЛЕННЯ У СТОРОЖОВІЙ ОХОРОНІ" -> BotState.A4;
            //TODO: too much text
            case "ДІЇ ВІДДІЛЕННЯ У ПОХІДНІЙ ОХОРОНІ" -> BotState.A5;
            case "ВІДДІЛЕННЯ НА МАРШІ" -> BotState.A6;
            case "ОСНОВНІ ВИДИ ЗАГАЛЬНОВІЙСЬКОВОГО БОЮ" -> BotState.A7;
            case "/HELP" -> BotState.HELP_MENU;
            case "НАЗАД" -> BotState.BACK;
            default -> BotState.HELP_MENU;
        };
    }


    public BotApiMethod<?> setupResponse(BotState botState, SendMessage sendMessage, User user) {

        var response = switch (botState) {
            case ENTRY_POINT -> getStartAnswer(sendMessage, user);
            case A -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A, user);
            case A1 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A1, user);

            case A2 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A2, user);
            case A2B1 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A2B1, user);
            case A2B2 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A2B2, user);

            case A3 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A3, user);
            case A4 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A4, user);
            case A5 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A5, user);
            case A6 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A6, user);
            case A7 -> getTopicsByFatherTopicAnswer(sendMessage, BotState.A7, user);
            case HELP_MENU -> getStartAnswer(sendMessage, user);
            case BACK -> stepBack(sendMessage, user);
            case BASIC_TOPIC -> getStartAnswer(sendMessage, user);
            default -> getStartAnswer(sendMessage, user);
        };
        return response;
    }

    private SendMessage getStartAnswer(SendMessage sendMessage, User user) {
        updateUserData(user);
        var topics = topicService.findBasicTopics();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(getTopicReplyKeyboard(topics));
        sendMessage.setText("Використовуй клавіатуру для вибору розділу.");
        return sendMessage;
    }

//    private SendMessage errorStepBack(SendMessage sendMessage) {
//        sendMessage.setText("Неможливо повернутися назад, так як це одн з почткових розділів.");
//        return sendMessage;
//    }

    private BotApiMethod<?> stepBack(SendMessage sendMessage, User user) {
        var topicByName = topicService.findTopicByName(user.getState().toString());
        if (topicByName == null) {
            return setupResponse(BotState.BASIC_TOPIC, sendMessage, user);
        }

        var fatherTopic = topicService.findFatherByChild(topicByName.getId());
        if (fatherTopic == null) {
            return setupResponse(BotState.BASIC_TOPIC, sendMessage, user);
        }

        return setupResponse(getBotState(fatherTopic.getName()), sendMessage, user);
    }

    private SendMessage getTopicsByFatherTopicAnswer(SendMessage sendMessage, BotState state, User user) {
        //TODO: set proper state for each case.
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
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(getTopicReplyKeyboardWithBackStep(topicDTOs));
        sendMessage.setText(topicDTOResponse.getText());
        return sendMessage;
    }

    private ReplyKeyboardMarkup getTopicReplyKeyboard(List<TopicDTOResponse> topics) {
        return  buildTopicReplyKeyboard(topics);
    }

    private ReplyKeyboardMarkup getTopicReplyKeyboardWithBackStep(List<TopicDTOResponse> topics) {
        var replyKeyboard = buildTopicReplyKeyboard(topics);
        var keyboard = replyKeyboard.getKeyboard();

        var keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton(BotState.BACK.toString()));
        keyboard.add(keyboardRow);

        replyKeyboard.setKeyboard(keyboard);
        return replyKeyboard;
    }

    private ReplyKeyboardMarkup buildTopicReplyKeyboard(List<TopicDTOResponse> topics) {
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

    private void updateUserData(User user) {
        userService.update(user);
    }
}
