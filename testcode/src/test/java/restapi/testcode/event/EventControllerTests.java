package restapi.testcode.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import restapi.testcode.common.BaseControllerTest;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests extends BaseControllerTest {

    // 웹 서버를 띄우지 않고도 스프링 MVC (DispatcherServlet)가 요청을 처리하는 과정을 확인할 수 있기 때문에 컨트롤러 테스트용으로 자주 쓰임.
    @Autowired
    private EventRepository eventRepository;

    @DisplayName("Create Event OK")
    @Test
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest API Description")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .limitOfEnrollment(100)
                .basePrice(100)
                .maxPrice(200)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                ) // perform(요청을 생성한다.)
                .andDo(print()) // 요청과 응답을 보여준다.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.update-events").exists())
                .andExpect(jsonPath("_links.query-events").exists())
//                .andDo(document("create-event"))
                // 문서 생성 (요청, 응답 본문 문서화)
                .andDo(document("create-event",
                        // 링크 정보 문서화
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-events").description("link to update an existing events")
                        ),
                        // 요청헤더 문서화
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        // 요청 필드 문서화
                        requestFields(
                                fieldWithPath("name").description("name of new Event"),
                                fieldWithPath("description").description("description of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new Event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment of new Event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("basePrice of new Event"),
                                fieldWithPath("maxPrice").description("maxPrice of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new Event")
                        ),
                        // 응답 헤더 문서화
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        // 응답 필드 문서화
                        relaxedResponseFields( // "relaxed" prefix를 붙이면 필드의 전체가 아닌 일부만 문서화 할 수 있다. 권장하지는 않음
                                fieldWithPath("id").description("identifier of new Event"),
                                fieldWithPath("name").description("name of new Event"),
                                fieldWithPath("description").description("description of new Event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new Event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new Event"),
                                fieldWithPath("location").description("location of new Event"),
                                fieldWithPath("basePrice").description("basePrice of new Event"),
                                fieldWithPath("maxPrice").description("maxPrice of new Event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new Event"),
                                fieldWithPath("offline").description("it tells is this event is offline or not"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-events.href").description("link to update existing event")
//                                ,fieldWithPath("_links.profile.href").description("link to profile")
                        )));
    }



    @DisplayName("Extra input ignore")
    @Test
    public void createEvent_Extra_input_ignore() throws Exception {

        EventDto event = EventDto.builder()
//        Event event = Event.builder()
//                        .id(100)
                .name("Spring")
                .description("Rest API Description")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200)
                .location("강남역")
//                        .free(true)
//                        .offline(false)
//                        .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                ) // perform(요청을 생성한다.)
                .andDo(print()) // 요청과 응답을 보여준다.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
//                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT)) 이게 왜 실패라고 뜨는지
        ;
    }

    @DisplayName("Bad Request")
    @Test
    public void createEvent_BadRequest() throws Exception {

        Event event = Event.builder()
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                ) // perform(요청을 생성한다.)
                .andDo(print()) // 요청과 응답을 보여준다.
                .andExpect(status().isBadRequest())
        ;
    }

    @DisplayName("createEvent_Bad_Request_Empty_Input")
    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("crateEvent_Bad_Request_Wrong_Input")
    public void crateEvent_Bad_Request_Wrong_Input() throws Exception {
        Event event = generateEvent(1);
        EventDto eventDto = modelMapper.map(event,EventDto.class);
        eventDto.setBasePrice(1000);
        eventDto.setMaxPrice(100);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.index").exists())

        ;
    }

    @Test
    @DisplayName("Paging Query Event")
    public void queryEvents() throws Exception {
        // Given
        IntStream.range(0,30).forEach(i->{
            generateEvent(i);
        });

        // When
        mockMvc.perform(get("/api/events")
                        .param("page","1")
                        .param("size","10")
                        .param("sort","name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
//                .andDo(document("query-events")) // profile 추가
        ;
        // Then
    }

    @Test
    @DisplayName("Find Existing Event")
    public void getEvent() throws Exception {
        // given
        Event event = generateEvent(100);
        // when & then
        mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("_links.self").exists())
//                .andExpect(jsonPath("_links.profile").exists())
//                .andDo(document("get-an-event"))
        ;
    }

    @Test
    @DisplayName("Finding Non-existing Event")
    public void getEvent404() throws Exception {
        // given
        generateEvent(100);
        // when & then
        mockMvc.perform(get("/api/events/45678"))
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event "+index)
                .description("Rest API Description")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200)
                .location("강남역")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .limitOfEnrollment(100)
                .build();

       return eventRepository.save(event);
    }

    @Test
    @DisplayName("Update Event OK")
    public void updateEvent() throws Exception {
        // given
        Event event = generateEvent(100);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        String updated_name = "Updated Name";
        eventDto.setName(updated_name);
        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                        .andDo(print())

                        .andExpect(status().isOk())
                        .andExpect(jsonPath("name").value(updated_name))
                        .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @Test
    @DisplayName("Update Event Empty Input")
    public void updateEvent_Empty_Input() throws Exception {
        // given
        Event event = generateEvent(100);
        EventDto eventDto = new EventDto();
        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Event wrong Input")
    public void updateEvent_Wrong_Input() throws Exception {
        // given
        Event event = generateEvent(1);
        EventDto eventDto = modelMapper.map(event,EventDto.class);
        eventDto.setBasePrice(10000);
        eventDto.setMaxPrice(100);


        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Non-Existent Event")
    public void updateEvent_NotFound() throws Exception {
        // given
        Event event = generateEvent(100);
        EventDto eventDto = modelMapper.map(event, EventDto.class);

        mockMvc.perform(put("/api/events/1348")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                        .andDo(print())
                        .andExpect(status().isNotFound());
    }

}

//@SpringBootTest
