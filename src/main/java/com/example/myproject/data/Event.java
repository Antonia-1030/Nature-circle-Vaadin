package com.example.myproject.data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EVENT")
public class Event extends AbstractEntity{

    private String name;
    private LocalDate startDate;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getDate() {
        return startDate;
    }
    public void setDate(LocalDate publicationDate) {
        this.startDate = publicationDate;
    }
}
