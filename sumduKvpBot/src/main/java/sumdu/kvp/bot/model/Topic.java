package sumdu.kvp.bot.model;

import lombok.Data;
import sumdu.kvp.bot.config.TopicResponseType;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TopicResponseType type;

    @Lob
    @Column(name="text", nullable = false)
    private String text;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="father_id")
    private List<Topic> subTopics;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private File file;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "video_id")
    private Video video;
}
