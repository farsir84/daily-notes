import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { NoteService } from '../../services/note.service';
import { Note } from '../../models/note.model';

@Component({
  selector: 'app-note-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule
  ],
  templateUrl: './note-dialog.component.html',
  styleUrls: ['./note-dialog.component.scss']
})
export class NoteDialogComponent implements OnInit {
  title = '';
  content = '';
  isEditMode = false;
  noteId?: number;

  constructor(
    public dialogRef: MatDialogRef<NoteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { date: string; note?: Note },
    private noteService: NoteService
  ) {}

  ngOnInit(): void {
    if (this.data.note) {
      this.isEditMode = true;
      this.title = this.data.note.title;
      this.content = this.data.note.content;
      this.noteId = this.data.note.id;
    }
  }

  save(): void {
    if (!this.title || !this.content) {
      return;
    }

    const note: Note = {
      date: this.data.date,
      title: this.title,
      content: this.content
    };

    if (this.isEditMode && this.noteId) {
      this.noteService.updateNote(this.noteId, note).subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: (err) => {
          console.error('Error updating note:', err);
        }
      });
    } else {
      this.noteService.createNote(note).subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: (err) => {
          console.error('Error creating note:', err);
        }
      });
    }
  }

  delete(): void {
    if (this.isEditMode && this.noteId) {
      this.noteService.deleteNote(this.noteId).subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: (err) => {
          console.error('Error deleting note:', err);
        }
      });
    }
  }

  cancel(): void {
    this.dialogRef.close(false);
  }
}
