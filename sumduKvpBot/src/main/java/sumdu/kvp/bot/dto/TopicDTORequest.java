package sumdu.kvp.bot.dto;

import lombok.Data;
import sumdu.kvp.bot.config.TopicResponseType;
import sumdu.kvp.bot.model.File;
import sumdu.kvp.bot.model.Image;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class TopicDTORequest {

    private Integer id;

    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "Text can not be blank")
    private String text;

//    //TODO: validation if not exists.
    @NotBlank(message = "Type can not be blank")
    private String type;
//
//    //TODO: DTO
//    private Image image;
//
//    //TODO: DTO
//    private File file;

    private Integer fatherId;
//    private List<TopicDTORequest> subTopics;
}
