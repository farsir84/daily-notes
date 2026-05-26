package com.dailynotes.controller;

import com.dailynotes.dto.NoteCreateRequest;
import com.dailynotes.dto.NoteDTO;
import com.dailynotes.service.NoteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAllNotes(Authentication authentication) {
        List<NoteDTO> notes = noteService.getAllNotes(authentication.getName());
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/range")
    public ResponseEntity<List<NoteDTO>> getNotesByDateRange(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<NoteDTO> notes = noteService.getNotesByDateRange(authentication.getName(), startDate, endDate);
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(Authentication authentication, @RequestBody NoteCreateRequest request) {
        NoteDTO note = noteService.createNote(authentication.getName(), request);
        return ResponseEntity.ok(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody NoteCreateRequest request) {
        NoteDTO note = noteService.updateNote(authentication.getName(), id, request);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(Authentication authentication, @PathVariable Long id) {
        noteService.deleteNote(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
