package com.dailynotes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteCreateRequest {
    private LocalDate date;
    private String title;
    private String content;
}
