package ru.nsu.ccfit.komugi;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
public class Task extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "taskSequence",
            sequenceName = "task_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskSequence")
    public Long id;

    public String name;

    public String description;

    @Column(name = "user_id")
    public Long userId;

    @Column(name = "subject_id")
    public Long subjectId;

    public String tag;
}
