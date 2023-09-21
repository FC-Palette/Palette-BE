package fc.server.palette.member.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import fc.server.palette.member.entity.Member;
import fc.server.palette.member.entity.type.Job;
import fc.server.palette.member.entity.type.Position;
import fc.server.palette.member.entity.type.Sex;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDto {
    private String image;
    private String email;
    private String name;
    private String phoneNumber;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate birthday;
    private String nickname;
    private String bio;
    private String sex;
    private String position;
    private String job;

}
