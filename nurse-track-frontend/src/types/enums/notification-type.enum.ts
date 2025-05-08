export enum NotificationType {
  SHIFT_CHANGE = 'SHIFT_CHANGE',
  VACATION_REQUEST = 'VACATION_REQUEST',
  GENERAL = 'GENERAL',
  SYSTEM = 'SYSTEM',
  EMERGENCY = 'EMERGENCY',
}

export interface NotificationTypeDetails {
  displayName: string;
  icon: string; // Material icon name
}

export const NotificationTypeData: {
  [key in NotificationType]: NotificationTypeDetails;
} = {
  [NotificationType.SHIFT_CHANGE]: {
    displayName: 'Change of shift',
    icon: 'swap_horiz',
  },
  [NotificationType.VACATION_REQUEST]: {
    displayName: 'Vacation request',
    icon: 'beach_access',
  },
  [NotificationType.GENERAL]: {
    displayName: 'General notification',
    icon: 'info',
  },
  [NotificationType.SYSTEM]: {
    displayName: 'System notification',
    icon: 'settings',
  },
  [NotificationType.EMERGENCY]: {
    displayName: 'Emergency notification',
    icon: 'warning',
  },
};

// Helper para selects
export const NotificationTypeOptions = (
  Object.keys(NotificationTypeData) as NotificationType[]
).map((type) => ({
  value: type,
  label: NotificationTypeData[type].displayName,
  icon: NotificationTypeData[type].icon,
}));
