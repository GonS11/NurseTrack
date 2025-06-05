import { RequestStatus, RequestStatusData } from '../types/enums/status.enum';

export function getStatusConfig(status: RequestStatus) {
  return (
    RequestStatusData[status] || {
      displayName: status,
      badgeStyle: 'secondary',
    }
  );
}
