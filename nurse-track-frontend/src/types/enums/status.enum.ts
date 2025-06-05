export enum RequestStatus {
  CANCELLED = 'CANCELLED',
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
}

export interface StatusConfig {
  displayName: string;
  badgeStyle: string;
}

export const RequestStatusData: Record<RequestStatus, StatusConfig> = {
  [RequestStatus.CANCELLED]: {
    displayName: 'Cancelled',
    badgeStyle: 'danger',
  },
  [RequestStatus.PENDING]: {
    displayName: 'Pending',
    badgeStyle: 'warning',
  },
  [RequestStatus.APPROVED]: {
    displayName: 'Approved',
    badgeStyle: 'info',
  },
  [RequestStatus.REJECTED]: {
    displayName: 'Rejected',
    badgeStyle: 'secondary',
  },
};
