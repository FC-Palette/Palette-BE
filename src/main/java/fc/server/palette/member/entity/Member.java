package fc.server.palette.member.entity;

import fc.server.palette.member.entity.type.Job;
import fc.server.palette.member.entity.type.Position;
import fc.server.palette.member.entity.type.Sex;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable= false, length = 50)
    private String email;

    @Column(nullable= false, length = 100)
    private String password;

    @Column(nullable= false, length = 30)
    private String name;

    @Column(name = "phone_number", nullable= false, length = 11)
    private String phoneNumber;

    @Column(nullable= false, length = 50)
    private String bio;

    @Column(nullable= false)
    private LocalDate birthday;

    @Column(nullable= false, length = 50)
    private String office;

    @Column(nullable= false, length = 45)
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private Job job;

    @Column(nullable = false, length=255)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Sex sex;




}
