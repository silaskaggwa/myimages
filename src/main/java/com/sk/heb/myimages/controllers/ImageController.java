package com.sk.heb.myimages.controllers;

import com.sk.heb.myimages.entity.Image;
import com.sk.heb.myimages.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    @Operation(summary = "Get all Images or only images with specified objects")
    public ResponseEntity<List<Image>> getAllImages(@RequestParam(required = false) String objects){
        List<Image> images;
        if (objects != null) {
            Set<String> objectsSet = Set.of(objects.split(","));
            images = imageService.getImagesByObjectNames(objectsSet);
        } else {
            images = imageService.getAll();
        }
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "Get image by id")
    public ResponseEntity<Image> getImage(@PathVariable("imageId") long id) {
        return ResponseEntity.ok(imageService.getImageById(id).orElse(null));
    }

    @PostMapping
    @Operation(summary = "Upload and save new image")
    public ResponseEntity<Image> saveImage(
            @RequestPart("file") MultipartFile file,
            @RequestPart(name = "label", required = false) String label) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        String fileName = file.getOriginalFilename();
        String imageLabel = Optional.ofNullable(label).orElse(fileName);

        Image newImage = null;
        try {
            byte[] fileContent = file.getBytes();
            newImage = imageService.process(imageLabel, fileContent);
        } catch (IOException e) {
            System.out.println("File upload failed: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
        return ResponseEntity.ok(newImage);
    }

}
