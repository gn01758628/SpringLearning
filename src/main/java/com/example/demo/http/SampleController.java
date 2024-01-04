package com.example.demo.http;

import com.example.demo.SampleService;
import com.example.demo.jpa.Sample;
import com.example.demo.jpa.SampleEntity;
import com.example.demo.jpa.SampleNameTypeDTO;
import com.example.demo.jpa.SampleNameTypeVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

// @RestController = @Controller + @ResponseBody
// 代表這個class是controller並且內部方法都有@ResponseBody的註解
//  @Controller:
//      @Component的一種特化註解，一樣會被視為Spring Bean由Spring來管理
//      1.該class會"被Spring MVC視為一個controller"，用來處理HTTP請求
//      2.在此類的方法上使用@GetMapping或@PostMapping
//        會處理指定的URL路徑的請求(沒指定就是根目錄)
//      3.內部的方法可以返回多種response類型：
//        a.String：代表view名稱。在這種情況下，需要一個模板引擎（EX:Thymeleaf）來解析view。
//        b.ModelAndView：同時返回model跟view。
//        c.ResponseEntity：請看下面method的說明。
//        在一個前後端完全分離的架構中，a跟b基本上是用不到的。
//  @ResponseBody:
//      1.註解在method上，該方法的"返回值"將直接response給客戶端
//      2.如果方法返回的是String、Primitive type或是Wrapper Class，Spring會將Content-Type設置為text/plain
//        如果方法返回的是class，Spring會將Content-Type設置為application/json
@Slf4j
@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/hello")
    public String hello() {
        return "HelloWorld";
    }

    //  ResponseEntity 用於構建和返回HTTP response的"整個內容"
    //  包括但不限於:HTTP狀態碼、響應頭、響應體等
    //  當controller內的方法返回的類別是ResponseEntity時
    //  其方法內部的設定，優先級是最高的，即便沒有使用@RestController或@ResponseBody
    @GetMapping("/test")
    public ResponseEntity<String> hello2() {
        return ResponseEntity.of(Optional.of("Test"));
    }

    @PostConstruct
    public void init() {
        sampleService.init();
    }

    @GetMapping
    public List<Sample> getAll() {
        return sampleService.query();
    }

    @GetMapping("/projection/1")
    public List<SampleNameTypeVO> query2() {
        return sampleService.queryProjection1();
    }

    @GetMapping("/projection/2")
    public List<SampleNameTypeDTO> query3() {
        return sampleService.queryProjection2();
    }

    @GetMapping("/query")
    public Page<Sample> query(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            Pageable pageable) {
        log.info("id:{}, name:{}", id, name);

        var spec = Specification.<SampleEntity>where(null);

        if (id != null) {
            spec = spec.and(Specification.where((entity, q, cb) -> cb.equal(entity.get("id"), id)));
        }

        if (name != null && !name.isBlank()) {
            spec = spec.and(Specification.where((entity, q, cb) -> cb.like(entity.get("name"), "%" + name + "%")));
        }

        return sampleService.query(spec, pageable);
    }

    @GetMapping("/{id}")
    public Sample queryOne(@PathVariable Long id) {
        return sampleService.query(id)
                .orElseThrow(() -> new IllegalArgumentException("此ID不存在:" + id));
    }

    @PostMapping
    public Sample save(@RequestBody Sample dto) {
        return sampleService.insert(dto);
    }

}
