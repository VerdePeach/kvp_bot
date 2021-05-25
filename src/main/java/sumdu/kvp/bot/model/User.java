package sumdu.kvp.bot.model;

import lombok.Data;
import sumdu.kvp.bot.config.BotState;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "chat_id", unique = true, nullable = false)
    private Long chatId;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private BotState state;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;
}
