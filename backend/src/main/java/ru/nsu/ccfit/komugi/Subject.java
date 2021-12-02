package ru.nsu.ccfit.komugi;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
public class Subject extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "subjectSequence",
            sequenceName = "subject_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjectSequence")
    public Long id;

    public String name;

    @Column(name = "day_of_week")
    public Integer dayOfWeek;

    @Column(name = "time_of_day")
    public LocalTime timeOfDay;

    public String classroom;

    public String type;

    @Column(name = "user_id")
    public Long userId;
}
