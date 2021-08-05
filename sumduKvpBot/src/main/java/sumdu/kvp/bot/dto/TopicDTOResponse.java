package sumdu.kvp.bot.dto;

import lombok.Data;
import sumdu.kvp.bot.model.File;
import sumdu.kvp.bot.model.Image;
import sumdu.kvp.bot.model.Topic;

import java.util.List;

@Data
public class TopicDTOResponse {
    private Integer id;
    private String name;
    private String text;
    private String type;
    //TODO: DTO
//    private Image image;
    //TODO: DTO
//    private File file;
//    private List<TopicDTOResponse> subTopics;
}
