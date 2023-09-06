package fc.server.palette.secondhand.entity;

import fc.server.palette.purchase.entity.type.Category;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Secondhand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, name = "transaction_start_time")
    private Time transactionStartTime;

    @Column(nullable = false, name = "transaction_end_time")
    private Time transactionEndTime;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "is_sold_out")
    private Boolean isSoldOut;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false, name = "account_number")
    private String accountNumber;

    @Column(nullable = false, name = "account_owner")
    private String accountOwner;

    @Column(nullable = false)
    private Integer hits;

    @Column(name = "is_free")
    private Boolean isFree;
}
