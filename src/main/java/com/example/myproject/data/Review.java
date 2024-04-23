package com.example.myproject.data;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "REVIEWS")
public class Review extends AbstractEntity{
    @OneToMany(cascade = CascadeType.ALL)
    private List<Event> event;
    @OneToMany(cascade = CascadeType.ALL)
    private List<SampleBook> product;
    private String title;
    private String comment;

    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public List<Event> getEvent() {
        return event;
    }
    public void setEvent(List<Event> event) {
        this.event = event;
    }
    public List<SampleBook> getProduct() {
        return product;
    }
    public void setProduct(List<SampleBook> product) {
        this.product = product;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String name) {
        this.comment = name;
    }
}
