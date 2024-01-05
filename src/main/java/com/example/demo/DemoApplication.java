package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.softleader.data.jpa.spec.annotation.Or;
import tw.com.softleader.data.jpa.spec.annotation.Spec;
import tw.com.softleader.data.jpa.spec.domain.GreaterThanEqual;
import tw.com.softleader.data.jpa.spec.domain.Like;
import tw.com.softleader.data.jpa.spec.repository.QueryBySpecExecutor;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(DemoApplication.class, args);

        var repository = context.getBean(PersonRepository.class);
        repository.saveAll(List.of(
                Person.builder().name("matt").age(10).build(),
                Person.builder().name("rhys").age(20).build()
        ));

        var adults = repository.findBySpec(new PersonCriteria("m", 18));
        System.out.println(adults);
    }

}

interface PersonRepository
        extends JpaRepository<Person, Long>, QueryBySpecExecutor<Person> {

}

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Person {

    @Id
    @GeneratedValue
    private Long id;
    String name;
    int age;

}
@Or
record PersonCriteria(
        @Spec(Like.class)
        String name,
        @Spec(GreaterThanEqual.class)
        int age) {

}