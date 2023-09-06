package fc.server.palette.purchase.entity;

import fc.server.palette.purchase.entity.type.Category;
import fc.server.palette.purchase.entity.type.ClosingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, name = "end_date")
    private Date endDate;

    @Column(nullable = false, name = "end_time")
    private Time endTime;

    @Column(nullable = false, name = "head_count")
    private Integer headCount;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "closing_type")
    private ClosingType closingType;

    @Column(nullable = false)
    private Boolean isClosing;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false, name = "account_number")
    private String accountNumber;

    @Column(nullable = false, name = "account_owner")
    private String accountOwner;

    @Column(nullable = false)
    private Integer hits;
}
