import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions, EventClickArg } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import { AuthService } from '../../services/auth.service';
import { NoteService } from '../../services/note.service';
import { Note } from '../../models/note.model';
import { NoteDialogComponent } from '../note-dialog/note-dialog.component';

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    FullCalendarModule
  ],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit {
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin, interactionPlugin],
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,dayGridWeek'
    },
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    weekends: true,
    dateClick: this.handleDateClick.bind(this),
    eventClick: this.handleEventClick.bind(this),
    events: []
  };

  constructor(
    private authService: AuthService,
    private noteService: NoteService,
    private router: Router,
    private dialog: MatDialog
  ) {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {
    this.loadNotes();
  }

  loadNotes(): void {
    this.noteService.getAllNotes().subscribe({
      next: (notes) => {
        this.calendarOptions.events = notes.map(note => ({
          id: note.id?.toString(),
          title: note.title,
          date: note.date,
          extendedProps: {
            content: note.content,
            noteData: note
          }
        }));
      },
      error: (err) => {
        console.error('Error loading notes:', err);
      }
    });
  }

  handleDateClick(arg: DateClickArg): void {
    this.openNoteDialog(arg.dateStr);
  }

  handleEventClick(arg: EventClickArg): void {
    const note = arg.event.extendedProps['noteData'] as Note;
    this.openNoteDialog(note.date, note);
  }

  openNoteDialog(date: string, note?: Note): void {
    const dialogRef = this.dialog.open(NoteDialogComponent, {
      width: '600px',
      data: { date, note }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadNotes();
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
