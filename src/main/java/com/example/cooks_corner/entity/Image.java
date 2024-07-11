package com.example.cooks_corner.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "image_url")
    private String url;

    @Column(nullable = false, name = "image_name")
    private String name;

    public Image(String url, String name) {
        this.url = url;
        this.name = name;
    }
}
