export enum Status {
  CANCELLED = 'CANCELLED',
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
}

export interface StatusConfig {
  displayName: string;
  badgeStyle: string; // For UI styling
}

export const StatusData: Record<Status, StatusConfig> = {
  [Status.CANCELLED]: {
    displayName: 'Cancelled',
    badgeStyle: 'danger',
  },
  [Status.PENDING]: {
    displayName: 'Pending',
    badgeStyle: 'warning',
  },
  [Status.APPROVED]: {
    displayName: 'Approved',
    badgeStyle: 'info',
  },
  [Status.REJECTED]: {
    displayName: 'Rejected',
    badgeStyle: 'secondary',
  },
};
