import type { ShiftResponse } from './schemas/shifts.schema';

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}

export interface CalendarDay {
  date: string;
  dayNumber: number;
  isCurrentMonth: boolean;
  shifts: ShiftResponse[];
}
