package sumdu.kvp.bot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Lob
    @Column(name="text", nullable = false)
    private String text;

//    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name="father_id")
//    private Topic fatherTopic;

//    @OneToMany(mappedBy="fatherTopic")
    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="father_id")
    private List<Topic> subTopics;
}
