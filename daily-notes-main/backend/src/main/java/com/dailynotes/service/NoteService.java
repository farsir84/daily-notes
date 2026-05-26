package com.dailynotes.service;

import com.dailynotes.dto.NoteCreateRequest;
import com.dailynotes.dto.NoteDTO;
import com.dailynotes.entity.Note;
import com.dailynotes.entity.User;
import com.dailynotes.repository.NoteRepository;
import com.dailynotes.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public List<NoteDTO> getAllNotes(String username) {
        User user = getUserByUsername(username);
        return noteRepository.findByUserOrderByDateDesc(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NoteDTO> getNotesByDateRange(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        return noteRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public NoteDTO createNote(String username, NoteCreateRequest request) {
        User user = getUserByUsername(username);
        
        Note note = new Note();
        note.setUser(user);
        note.setDate(request.getDate());
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        
        Note savedNote = noteRepository.save(note);
        return convertToDTO(savedNote);
    }

    public NoteDTO updateNote(String username, Long noteId, NoteCreateRequest request) {
        User user = getUserByUsername(username);
        Note note = noteRepository.findByIdAndUser(noteId, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        
        note.setDate(request.getDate());
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        
        Note updatedNote = noteRepository.save(note);
        return convertToDTO(updatedNote);
    }

    public void deleteNote(String username, Long noteId) {
        User user = getUserByUsername(username);
        Note note = noteRepository.findByIdAndUser(noteId, user)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        noteRepository.delete(note);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private NoteDTO convertToDTO(Note note) {
        return new NoteDTO(
                note.getId(),
                note.getDate(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}
