import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class Person {

    private String name;
    private int birthDate;

    public Person(String name, int birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }
}
