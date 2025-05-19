package com.projectalpha.controller.business;

import com.projectalpha.dto.business.BusinessDTO;
import com.projectalpha.service.business.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping
    public ResponseEntity<List<BusinessDTO>> getAllBusinesses() {
        return ResponseEntity.ok(businessService.getAllBusinesses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDTO> getBusinessById(@PathVariable UUID id) {
        return businessService.getBusinessById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BusinessDTO>> searchBusinesses(@RequestParam String query) {
        return ResponseEntity.ok(businessService.searchBusinesses(query));
    }
}