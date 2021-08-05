package sumdu.kvp.bot.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "url")
    private String url;
}
