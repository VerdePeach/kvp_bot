package sumdu.kvp.bot.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class TopicDTORequest {

    private Integer id;

    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "Text can not be blank")
    private String text;

    private Integer fatherId;
//    private List<TopicDTORequest> subTopics;
}
