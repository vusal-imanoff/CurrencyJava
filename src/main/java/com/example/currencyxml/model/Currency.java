package com.example.currencyxml.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "code")
    public String code;
    @Column(name = "nominial")
    public String nominial;
    @Column(name = "name")
    public String name;
    @Column(name = "value")
    public String value;
    @Column(name = "date")
    public String date;
}
