package restapi.testcode.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import restapi.testcode.common.ErrorsResource;
import restapi.testcode.index.IndexController;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE) // 모든 응답이 HAL_Json 타입으로 나간다.
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Validated EventDto eventDto, Errors errors) {
        log.info("** create event");
        // 바인딩 검증
        if (errors.hasErrors()) {
//            return ResponseEntity.badRequest().body(errors);
            return badRequest(errors);// 여기서 널포인트익셉션
        }

        // 비지니스 로직에 맞지 않는 데이터 검증
        eventValidator.validate(eventDto,errors);
        if (errors.hasErrors()) {
//             return ResponseEntity.badRequest().body(errors);
            return badRequest(errors);// 여기서 널포인트익셉션

        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = eventRepository.save(event);

        // 링크 추가 : 엔티티를 엔티티 모델로 감싸고 거기에 링크를 add 한다.
        URI createdUri = getSelfLink().toUri();
        EventModel eventModel = new EventModel(event);
        eventModel.add(linkTo(EventController.class).withRel("query-events"));
        eventModel.add(getSelfLink().withRel("update-events"));
//        eventModel.add(new Link("/docs/index.html#resource-events-create").withRel("profile")); // 프로필 링크 추가
        return ResponseEntity.created(createdUri).body(eventModel);
    }

    @GetMapping // pageable : 컨트롤러가 페이징 관련 파라미터를 받아올 수 있다.
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> pages = eventRepository.findAll(pageable);
        var pagedResource = assembler.toModel(pages, e-> new EventModel(e));
//        pagedResource.add(new Link("/docs/index.html#resource-events-list").withRel("profile")); // 프로필 링크 추가

        return ResponseEntity.ok().body(pagedResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        log.info("** getEvent : " + id);
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventModel eventModel = new EventModel(event);
//        eventModel.add(new Link("/docs/index.html#resource-events-get").withRel("profile")); // 프로필 링크 추가

        return ResponseEntity.ok(eventModel);

    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Validated EventDto eventDto, Errors errors) {
        log.info("** update test :" + id);

        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        Event targetEvent = event.get();
        modelMapper.map(eventDto, targetEvent);
        Event savedEvent = eventRepository.save(targetEvent);

        EventModel eventModel = new EventModel(savedEvent);
//        eventModel.add(new Link("/docs/index.html#resource-events-get").withRel("profile")); // 프로필 링크 추가

        return ResponseEntity.ok(eventModel);

    }

    private ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

    private WebMvcLinkBuilder getSelfLink() {
        return linkTo(EventController.class).slash("{id}");
    }

}
