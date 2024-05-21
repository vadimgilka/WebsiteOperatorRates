package ru.kozarez.websiteoperatorrates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="rates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "provider_name")
    private String providerName;
    @Column(name = "link")
    private String link;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @Column(name = "messages")
    private int messages;
    @Column(name = "minutes_of_call")
    private int minutesOfCall;
    @Column(name = "gigabytes_of_internet")
    private int gigabytesOfInternet;

    public RateEntity(String providerName, String link, String name, int price, int messages, int minutesOfCall, int gigabytesOfInternet) {
        this.providerName = providerName;
        this.link = link;
        this.name = name;
        this.price = price;
        this.messages = messages;
        this.minutesOfCall = minutesOfCall;
        this.gigabytesOfInternet = gigabytesOfInternet;
    }
}
