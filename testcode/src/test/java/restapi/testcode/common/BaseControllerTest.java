package restapi.testcode.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Ignore
public class BaseControllerTest {
    @Autowired
    protected ObjectMapper objectMapper; // 객체를 Json으로 바꿔주는 메서드.
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected MockMvc mockMvc;
    //    @MockBean
    //    EventRepository eventRepository;
}
