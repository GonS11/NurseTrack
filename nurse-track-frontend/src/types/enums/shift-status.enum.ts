export enum ShiftStatus {
  SCHEDULED = 'SCHEDULED',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  SWAPPED = 'SWAPPED',
}

export interface ShiftStatusConfig {
  displayName: string;
  badgeStyle: string;
}

export const ShiftStatusData: Record<ShiftStatus, ShiftStatusConfig> = {
  [ShiftStatus.SCHEDULED]: {
    displayName: 'Scheduled',
    badgeStyle: 'primary',
  },
  [ShiftStatus.COMPLETED]: {
    displayName: 'Completed',
    badgeStyle: 'success',
  },
  [ShiftStatus.CANCELLED]: {
    displayName: 'Cancelled',
    badgeStyle: 'danger',
  },
  [ShiftStatus.SWAPPED]: {
    displayName: 'Swapped',
    badgeStyle: 'swapped',
  },
};
