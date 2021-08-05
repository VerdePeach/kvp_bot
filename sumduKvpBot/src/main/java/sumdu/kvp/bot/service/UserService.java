package sumdu.kvp.bot.service;

import sumdu.kvp.bot.model.User;

import java.util.List;

public interface UserService {
    User save(User user);
    User update(User user);
    List<User> findAll();
    User findByUsername(String username);
    User findByTelegramId(Long telegramId);
}
