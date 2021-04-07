package sumdu.kvp.bot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", unique = true,nullable = false)
    private String name;
}
