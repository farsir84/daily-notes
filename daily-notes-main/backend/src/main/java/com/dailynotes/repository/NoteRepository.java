package com.dailynotes.repository;

import com.dailynotes.entity.Note;
import com.dailynotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserOrderByDateDesc(User user);
    List<Note> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);
    Optional<Note> findByIdAndUser(Long id, User user);
}
