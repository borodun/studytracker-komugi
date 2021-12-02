package ru.nsu.ccfit.komugi;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class User extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "userSequence",
            sequenceName = "user_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequence")
    public Long id;

    public String login;

    public String password;

    public String email;
}
