import { ref } from 'vue';

export type NotificationType = 'info' | 'success' | 'warning' | 'error';

const showNotification = ref(false);
const notificationMessage = ref('');
const notificationType = ref<NotificationType>('info');
const notificationTimeout = ref<ReturnType<typeof setTimeout> | null>(null);
const notificationAutoClose = ref(true);

export function useNotifications() {
  const displayNotification = (
    message: string,
    type: NotificationType,
    autoClose: boolean | number | undefined = true,
  ) => {
    if (notificationTimeout.value) {
      clearTimeout(notificationTimeout.value);
    }

    notificationMessage.value = message;
    notificationType.value = type;
    showNotification.value = true;

    let actualAutoClose = true;
    let duration = 5000;

    if (typeof autoClose === 'boolean') {
      actualAutoClose = autoClose;
      duration = 5000;
    } else if (typeof autoClose === 'number') {
      actualAutoClose = true;
      duration = autoClose;
    } else {
      actualAutoClose = true;
    }

    notificationAutoClose.value = actualAutoClose;

    if (actualAutoClose) {
      notificationTimeout.value = setTimeout(() => {
        dismiss();
      }, duration);
    }
  };

  const showInfo = (message: string, autoClose?: boolean | number) => {
    displayNotification(message, 'info', autoClose);
  };

  const showSuccess = (message: string, autoClose?: boolean | number) => {
    displayNotification(message, 'success', autoClose);
  };

  const showWarning = (message: string, autoClose?: boolean | number) => {
    displayNotification(message, 'warning', autoClose);
  };

  const showError = (message: string, autoClose?: boolean | number) => {
    displayNotification(message, 'error', autoClose);
  };

  const dismiss = () => {
    showNotification.value = false;
    notificationMessage.value = '';
    notificationType.value = 'info';
    if (notificationTimeout.value) {
      clearTimeout(notificationTimeout.value);
      notificationTimeout.value = null;
    }
  };

  return {
    showNotification,
    notificationMessage,
    notificationType,
    notificationAutoClose,
    displayNotification,
    showInfo,
    showSuccess,
    showWarning,
    showError,
    dismiss,
  };
}
