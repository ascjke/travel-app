import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestJackson {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person("Zakhar", 1990);
        try {
            String json = mapper.writeValueAsString(person);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
