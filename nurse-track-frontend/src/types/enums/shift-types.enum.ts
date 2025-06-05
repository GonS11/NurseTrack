export enum ShiftType {
  MORNING = 'MORNING',
  AFTERNOON = 'AFTERNOON',
  NIGHT = 'NIGHT',
  HALF_MORNING = 'HALF_MORNING',
  HALF_NIGHT = 'HALF_NIGHT',
}

export interface ShiftTypeDetails {
  displayName: string;
  defaultStartTime: string;
  defaultEndTime: string;
  fixedTime: boolean;
  overnight: boolean;
}

export const ShiftTypeData: Record<ShiftType, ShiftTypeDetails> = {
  [ShiftType.MORNING]: {
    displayName: 'Morning Shift',
    defaultStartTime: '08:00',
    defaultEndTime: '15:00',
    fixedTime: true,
    overnight: false,
  },
  [ShiftType.AFTERNOON]: {
    displayName: 'Afternoon Shift',
    defaultStartTime: '15:00',
    defaultEndTime: '22:00',
    fixedTime: true,
    overnight: false,
  },
  [ShiftType.NIGHT]: {
    displayName: 'Night Shift',
    defaultStartTime: '22:00',
    defaultEndTime: '08:00',
    fixedTime: true,
    overnight: true,
  },
  [ShiftType.HALF_MORNING]: {
    displayName: '12 hours Morning',
    defaultStartTime: '08:00',
    defaultEndTime: '20:00',
    fixedTime: true,
    overnight: false,
  },
  [ShiftType.HALF_NIGHT]: {
    displayName: '12 hours Night',
    defaultStartTime: '20:00',
    defaultEndTime: '08:00',
    fixedTime: true,
    overnight: true,
  },
};
