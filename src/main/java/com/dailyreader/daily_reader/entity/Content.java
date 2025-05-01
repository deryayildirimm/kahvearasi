package com.dailyreader.daily_reader.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="contents")
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

}
