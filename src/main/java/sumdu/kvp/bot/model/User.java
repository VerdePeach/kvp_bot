package sumdu.kvp.bot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "chatId", nullable = false, unique = true)
    private String chatId;

    @Column(name = "state", nullable = false)
    private String state;
}
