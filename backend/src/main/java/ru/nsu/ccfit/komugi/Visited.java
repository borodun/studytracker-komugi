package ru.nsu.ccfit.komugi;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Visited  extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "visitedSequence",
            sequenceName = "visited_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visitedSequence")
    public Long id;

    @Column(name = "user_id")
    public Long userId;

    @Column(name = "subject_id")
    public Long subjectId;

    public LocalDate date;

    public boolean visited;
}
