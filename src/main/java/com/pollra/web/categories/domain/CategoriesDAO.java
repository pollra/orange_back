package com.pollra.web.categories.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@ToString
@Table(name = "categories")
public class CategoriesDAO {
    @Id
    @GeneratedValue
    private Long num;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private String name;
}
