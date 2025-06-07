import type { NurseDepartmentResponse } from './schemas/assignments.schema';
import type {
  ShiftResponse,
  ShiftTemplateResponse,
} from './schemas/shifts.schema';

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

export interface ShiftFormModalProps {
  isOpen: boolean;
  isEditMode: boolean;
  initialShiftDate: string;
  shiftToEdit?: ShiftResponse | null;
  availableNurses: NurseDepartmentResponse[];
  shiftTemplates: ShiftTemplateResponse[];
}
