package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

// @RestController = @Controller + @ResponseBody
//  @Controller:
//      1.將class標示為一個controller
//      2.controller用來處理HTTP請求
//      3.在此類的方法上使用@GetMapping或@PostMapping
//        會處理指定的URL路徑的請求(沒指定就是根目錄)
//      4.內部的方法在沒有其他註解的情況下，只能返回String
//        其代表的是view的檔案名稱(Spring必須要有模板引擎的依賴,EX:Thymeleaf)
//  @ResponseBody:
//      1.註解在method上，該方法的"返回值"將直接response給客戶端
//      2.如果方法返回的是String、Primitive type或是Wrapper Class，Spring會將Content-Type設置為text/plain
//        如果方法返回的是class，Spring會將Content-Type設置為application/json
//  @RestController:
//      代表這個class是controller並且內部方法都有@ResponseBody的註解
@RestController
public class SampleController {

    @GetMapping("/test1")
    public String hello() {
        return "HelloWorld";
    }

    //  ResponseEntity 用於構建和返回HTTP response的"整個內容"
    //  包括但不限於:HTTP狀態碼、響應頭、響應體等
    //  當controller內的方法返回的類別是ResponseEntity時
    //  其方法內部的設定，優先級是最高的，即便沒有使用@RestController或@ResponseBody
    @GetMapping("/test2")
    public ResponseEntity<String> hello2() {
        return ResponseEntity.of(Optional.of("HelloWorld234"));
    }
}
