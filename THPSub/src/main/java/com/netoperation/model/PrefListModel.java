package com.netoperation.model;

import java.util.ArrayList;
import java.util.List;

public class PrefListModel {
    private PersonaliseDetails topics = new PersonaliseDetails();
    private PersonaliseDetails cities = new PersonaliseDetails();
    private PersonaliseDetails authors= new PersonaliseDetails();

    public PersonaliseDetails getTopics() {
        return topics;
    }

    public void addTopics(PersonaliseDetails topics) {
        this.topics = topics;
    }

    public PersonaliseDetails getCities() {
        return cities;
    }

    public void addCities(PersonaliseDetails cities) {
        this.cities = cities;
    }

    public PersonaliseDetails getAuthors() {
        return authors;
    }

    public void addAuthors(PersonaliseDetails authors) {
        this.authors = authors;
    }
}
