package restapi.testcode.index;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import restapi.testcode.event.EventController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

// 에러에 리소스를 추가해서 보낸다
@RestController
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel index() {
        var index = new RepresentationModel<>();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }

}
