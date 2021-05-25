package sumdu.kvp.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import sumdu.kvp.bot.SumduKVPBot;

@RestController
@RequestMapping("/")
public class BotController {

    private SumduKVPBot bot;

    @Autowired
    public BotController(SumduKVPBot bot) {
        this.bot = bot;
    }

    @PostMapping
    public PartialBotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
