package sumdu.kvp.bot.dto;

import lombok.Data;
import sumdu.kvp.bot.model.Topic;

import java.util.List;

@Data
public class TopicDTOResponse {
    private Integer id;
    private String name;
    private String text;
    private List<TopicDTOResponse> subTopics;
}
