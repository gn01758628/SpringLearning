package com.example.demo;

import com.example.demo.jpa.*;
import com.example.demo.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


// @Service：
// @Component的一種特化註解，一樣會被視為Spring Bean由Spring來管理
// 重點在語意上的區別，本身沒有額外的特性

// @Transactional：
// 用於聲明事務管理的一個註解
// 事務管理：方法內對於資料庫的操作只有全部成功或異常回滾兩種結果。
// 如果用在class上，代表該class的所有public非static方法都有@Transactional。

// @Transactional可以設定的參數與參數值：
//  1.propagation：用於定義事務的傳播行為。
//  事務的傳播行為：事務被另一個事務調用時，"被調用的事務"如何反應。
//      a.Propagation.REQUIRED 默認值：   當前存在事務，則加入該事務；沒有事務存在，則創建一個新的。
//      b.Propagation.SUPPORTS：         當前存在事務，則加入該事務；沒有事務存在，則以非事務方式執行。
//      c.Propagation.MANDATORY：        當前存在事務，則加入該事務；沒有事務存在，則拋出異常。
//      d.Propagation.NESTED：           當前存在事務，則在該事務內"嵌套執行"。沒有事務存在，則創建一個新的。
//          嵌套執行：
//          在現有事務內部啟動一個子事務。這個子事務是一個獨立的事務片段，它擁有自己的提交和回滾設置。
//          在子事務中所做的更改可以獨立於外層事務提交或回滾。但是，如果外層事務回滾，嵌套事務也會被回滾。
//      e.Propagation.REQUIRES_NEW：     創建一個新的事務，並暫停當前事務（如果存在）。
//      f.Propagation.NOT_SUPPORTED：    以非事務方式執行，如果當前存在事務，則暫停該事務。
//      g.Propagation.NEVER：            以非事務方式執行，如果當前存在事務，則拋出異常。

//  2.isolation：控制事務的隔離級別，及事務與其他事務的隔離程度。
//  事務的隔離級別是為了避免事務併發執行時會出現的以下三種問題：
//      A.Dirty Reads(髒讀)：指對Dirty Data(髒數據)讀取的行為。髒數據就是未被commit的數據。
//        當髒數據被其他事務讀取，該事務就會與未提交的數據產生依賴關係，這就是Dirty Read(髒讀)。
//      B.Non-repeatable reads(不可重複讀)：在同一個事務內，多次讀取同一數據集合。
//        在兩次讀取間，另一個事務對這些數據進行了"更新"並提交，導致第一個事務在兩次讀取的結果不一致。
//      C.Phantom read(幻讀)：幻讀關注於數據集合的整體一致性。在同一個事務內，多次查詢某個範圍的數據。
//        在兩次讀取間，另一個事務在這個範圍內"插入或刪除"了一些數據並提交，導致第一個事務在兩次查詢的結果不一致。
//  為了解決上述三個問題，分別有以下4種值代表不同級別：
//      a.Isolation.READ_UNCOMMITTED：允許事務讀取未被其他事務提交的更改。最低的設置，什麼問題都避免不了。
//      b.Isolation.READ_COMMITTED：允許事務讀取並僅讀取已被其他事務提交的更改。避免髒讀。
//      c.Isolation.REPEATABLE_READ：確保在事務期間多次讀取相同數據的結果是一致的，即防止不可重覆讀以及髒讀。
//          該事務一旦讀取了數據，被讀取的數據在事務提交前，其他事務都無法"更改"。
//      d.Isolation.SERIALIZABLE：最嚴格的隔離級別，完全隔離事務，以避免髒讀、不可重覆讀和幻讀。通過完全串行化事務操作來實現的，但會導致並發性能顯著下降。
//          該事務一旦讀取了數據，被讀取的數據在事務提交前，其他事務都無法"新增或刪除"。
//      e.Isolation.DEFAULT：指示Spring使用數據庫自身的默認隔離級別。不同的數據庫可能有不同的默認隔離級別。

//  3.readOnly：boolean值(預設false)，表示事務是否為只讀事務。
//  標記事務為readOnly，數據庫可以采取一些優化措施，如減少鎖的使用和避免不必要的髒檢查，從而提高查詢性能。

//  4.timeout：定義了事務可以執行的最長時間(秒)。如果超過這個時間限制，事務將被回滾。\

//  還有其他屬性，之後再說。

@Service
@Transactional
@RequiredArgsConstructor
public class SampleService {

    private final SampleDao sampleDao;

    private final SampleMapper sampleMapper;

    public void init() {
        sampleDao.saveAll(List.of(
                SampleEntity.builder()
                        .name("name1")
                        .amount(BigDecimal.valueOf(1))
                        .type(SampleType.A)
                        .time(LocalDateTime.now())
                        .date(LocalDate.now())
                        .year(Year.now())
                        .yearMonth(YearMonth.now())
                        .build(),
                SampleEntity.builder()
                        .name("name2")
                        .amount(BigDecimal.valueOf(2))
                        .type(SampleType.B)
                        .time(LocalDateTime.now())
                        .date(LocalDate.now())
                        .year(Year.now())
                        .yearMonth(YearMonth.now())
                        .build(),
                SampleEntity.builder()
                        .name("name3")
                        .amount(BigDecimal.valueOf(3))
                        .type(SampleType.C)
                        .time(LocalDateTime.now())
                        .date(LocalDate.now())
                        .year(Year.now())
                        .yearMonth(YearMonth.now())
                        .build()
        ));
    }


    public Sample insert(@RequestBody Sample dto) {
        var entity = sampleMapper.toJpa(dto);
        var saved = sampleDao.save(entity);
        return sampleMapper.fromJpa(saved);
    }

    public List<Sample> query() {
        return sampleDao.findAll().stream().map(sampleMapper::fromJpa).collect(Collectors.toList());
    }

    public List<SampleNameTypeVO> queryProjection1() {
        return sampleDao.findByProjection1();
    }

    public List<SampleNameTypeDTO> queryProjection2() {
        return sampleDao.findByProjection2();
    }

    public Optional<Sample> query(Long id) {
        return sampleDao.findById(id).map(sampleMapper::fromJpa);
    }

    public Page<Sample> query(Specification<SampleEntity> spec, Pageable pageable) {
        return sampleDao.findAll(spec, pageable).map(sampleMapper::fromJpa);
    }
}
