//package com.ddiring.BackEnd_Product.controller;
//
//import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
//import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
//import com.ddiring.BackEnd_Product.service.RequestService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/request")
//public class RequestController {
//
//    private final RequestService rs;
//
//    @GetMapping
//    public ResponseEntity<List<RequestListDto>> getAllRequest() {
//        List<RequestListDto> requestList = rs.getAllRequest();
//        return ResponseEntity.ok(requestList);
//    }
//
//    @GetMapping("/{requestId}")
//    public ResponseEntity<RequestDetailDto> getRequest(@PathVariable String requestId) {
//        RequestDetailDto rdd = rs.getRequestByRequestId(requestId);
//        return ResponseEntity.ok(rdd);
//    }
//}
