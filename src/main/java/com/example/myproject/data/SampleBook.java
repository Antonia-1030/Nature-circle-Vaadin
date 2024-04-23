package com.example.myproject.data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class SampleBook extends AbstractEntity {

    @Lob
    @Column(length = 1000000)
    private byte[] image;
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Event> event;
    private LocalDate publicationDate;
    private String isbn;

    private Double price;

    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvent() {
        return event;
    }
    public void setEvent(List<Event> event) {
        this.event = event;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public Double getPrice(){return price;}
    public void setPrice(Double price) {
        this.price = price;
    }
}
