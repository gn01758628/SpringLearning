package com.example.demo.jpa;

import com.example.demo.SampleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;

@Entity
@Getter
@Setter
public class SampleEntity {

    @Id
    private Long id;

    @Column(length = 20)
    private String name;

    // precision:整數+小數點的位數；sale:小數的位數
    @Column(name = "amt", precision = 16, scale = 6)
    private BigDecimal amount;

    @Column(length = 5)
    // @Enumerated JPA的一個註解
    // 告訴JPA如何將java的enum映射到資料庫中
    // EnumType.ORDINAL:儲存在enum裡聲明的順序(index從0開始)
    // EnumType.STRING:儲存在enum裡聲明的名稱
    // 沒有使用@Enumerated時，預設為EnumType.ORDINAL
    // 基本上都會使用EnumType.STRING，如果再enum中插入新的值，其他值的索引也會被改變
    @Enumerated(EnumType.STRING)
    private SampleType type;

    private LocalDateTime time;

    private LocalDate date;

    private YearMonth yearMonth;

    private Year year;

}
