package sumdu.kvp.bot.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "file_name", unique = true, nullable = false)
    private String fileName;
}
