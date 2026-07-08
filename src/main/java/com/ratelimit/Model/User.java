package com.ratelimit.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_rate")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "request_limit")
    private Integer limit;
}