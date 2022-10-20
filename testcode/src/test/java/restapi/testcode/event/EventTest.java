package restapi.testcode.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder().name("Rest API").description("Description").build();
        assertThat(event).isNotNull();
    }
    @Test
    public void javaBean() {
        String name = "Rest API";
        String description = "Description";
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    public void TestFree() {
        //  given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //  when
        event.update();

        //  then
        assertThat(event.isFree()).isTrue();

        //  given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //  when
        event.update();

        //  then
        assertThat(event.isFree()).isFalse();

        //  given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //  when
        event.update();

        //  then
        assertThat(event.isFree()).isFalse();

    }

    @Test
    public void TestOffline() {
        //  given
        Event event = Event.builder()
                .location("anywhere")
                .build();
        //  when
        event.update();

        //  then
        assertThat(event.isOffline()).isTrue();

        //  given
        event = Event.builder()
                .location("")
                .build();
        //  when
        event.update();

        //  then
        assertThat(event.isOffline()).isFalse();
    }





}