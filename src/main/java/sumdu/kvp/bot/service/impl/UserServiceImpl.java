package sumdu.kvp.bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sumdu.kvp.bot.model.User;
import sumdu.kvp.bot.repository.UserRepository;
import sumdu.kvp.bot.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        } else {
            return user.get();
        }
    }
}
