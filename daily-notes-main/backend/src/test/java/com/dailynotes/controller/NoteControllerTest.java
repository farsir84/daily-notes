package com.dailynotes.controller;

import com.dailynotes.dto.NoteCreateRequest;
import com.dailynotes.dto.NoteDTO;
import com.dailynotes.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService;

    @Test
    void getAllNotes_returnsNotesForAuthenticatedUser() throws Exception {
        NoteDTO note = new NoteDTO(1L, LocalDate.of(2026, 5, 26), "Daily standup", "Shipped tests", null, null);
        when(noteService.getAllNotes("alice")).thenReturn(List.of(note));

        mockMvc.perform(get("/api/notes").with(user("alice")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Daily standup"))
                .andExpect(jsonPath("$[0].content").value("Shipped tests"));

        verify(noteService).getAllNotes("alice");
    }

    @Test
    void createNote_returnsCreatedNote() throws Exception {
        NoteCreateRequest request = new NoteCreateRequest(
                LocalDate.of(2026, 5, 26),
                "New note",
                "Body"
        );
        NoteDTO created = new NoteDTO(2L, request.getDate(), request.getTitle(), request.getContent(), null, null);
        when(noteService.createNote(eq("alice"), eq(request))).thenReturn(created);

        mockMvc.perform(post("/api/notes")
                        .with(user("alice"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("New note"));

        verify(noteService).createNote("alice", request);
    }

    @Test
    void deleteNote_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/notes/5").with(user("alice")))
                .andExpect(status().isNoContent());

        verify(noteService).deleteNote("alice", 5L);
    }
}
