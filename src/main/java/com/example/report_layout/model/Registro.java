package com.example.report_layout.model;

import java.sql.Date;
import java.time.LocalDate;

public class Registro {
    private Integer id;
    private Integer sequence;
    private LocalDate writeDate;
    private LocalDate createDate;
    private Character name;

    public Registro(Integer id, Integer sequence, LocalDate writeDate, LocalDate createDate, Character name) {
        this.id = id;
        this.sequence = sequence;
        this.writeDate = writeDate;
        this.createDate = createDate;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(LocalDate writeDate) {
        this.writeDate = writeDate;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }
}
