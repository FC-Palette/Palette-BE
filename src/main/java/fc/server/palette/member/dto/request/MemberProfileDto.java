package fc.server.palette.member.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import java.time.LocalDate;

@Getter
public class MemberProfileDto {
    private String email;
    private String name;
    private String phoneNumber;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate birthday;
    private String nickname;
    private String bio;
    private String sex;
    private String position = null;
    private String job = null;

}
