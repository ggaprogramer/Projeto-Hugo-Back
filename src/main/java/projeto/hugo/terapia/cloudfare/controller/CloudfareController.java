package projeto.hugo.terapia.cloudfare.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.hugo.terapia.cloudfare.service.CloudfareService;
import projeto.hugo.terapia.profile.dto.ProfileInfo;

@RestController
@RequestMapping("/cloudfare")
@RequiredArgsConstructor
public class CloudfareController {

    private final CloudfareService cloudfareService;

    @PostMapping("create-bucket/{bucketName}")
    public ResponseEntity<?> createBucket(@PathVariable String bucketName){
        return cloudfareService.createBucket(bucketName);
    }

    @PostMapping("bucket-exists/{bucketName}")
    public Boolean doesBucketExist(@PathVariable String bucketName){
        return cloudfareService.doesBucketExist(bucketName);
    }

    @PostMapping("delete-bucket/{bucketName}")
    public ResponseEntity<?> deleteBucket(@PathVariable String bucketName){
        return cloudfareService.deleteBucket(bucketName);
    }

}
