package restapi.testcode.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;
import restapi.testcode.index.IndexController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Slf4j
public class ErrorsResource extends EntityModel<Errors> {



    public ErrorsResource(Errors errors) {
        super(errors);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index")); // .index를 빼먹으면 널포인트 예외 발생
    }
}
