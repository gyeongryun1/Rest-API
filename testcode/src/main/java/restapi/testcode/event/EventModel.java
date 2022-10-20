package restapi.testcode.event;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

// 응답에 링크를 추가하기 위해서 엔티티 모델(리소스)로 감싼다.
public class EventModel extends EntityModel<Event> {
    public EventModel(Event event) {
        super(event);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

//    @JsonUnwrapped
//    private Event event;
//
//    public EventModel(Event event) {
//        this.event = event;
//    }
//    public Event getEvent() {
//        return event;
//    }
}
