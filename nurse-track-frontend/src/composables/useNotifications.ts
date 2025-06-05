import { ref, watch } from 'vue';
import type { Ref } from 'vue';

export type NotificationType = 'info' | 'success' | 'warning' | 'error';

export function useNotifications() {
  const showNotification: Ref<boolean> = ref(false);
  const notificationMessage: Ref<string> = ref('');
  const notificationType: Ref<NotificationType> = ref('info');
  const notificationAutoClose: Ref<boolean | number> = ref(3000);

  let autoCloseTimeout: ReturnType<typeof setTimeout> | null = null;

  function showMessage(
    message: string,
    type: NotificationType = 'info',
    autoClose: boolean | number = 3000,
  ) {
    if (autoCloseTimeout) {
      clearTimeout(autoCloseTimeout);
      autoCloseTimeout = null;
    }

    notificationMessage.value = message;
    notificationType.value = type;
    notificationAutoClose.value = autoClose;
    showNotification.value = true;

    if (typeof autoClose === 'number' && autoClose > 0) {
      autoCloseTimeout = setTimeout(() => {
        dismiss();
      }, autoClose);
    }
  }

  function showSuccess(message: string, autoClose: boolean | number = 3000) {
    showMessage(message, 'success', autoClose);
  }

  function showInfo(message: string, autoClose: boolean | number = 3000) {
    showMessage(message, 'info', autoClose);
  }

  function showWarning(message: string, autoClose: boolean | number = 5000) {
    showMessage(message, 'warning', autoClose);
  }

  function showError(
    error: string | Error,
    autoClose: boolean | number = false,
  ) {
    const message = error instanceof Error ? error.message : error;
    showMessage(message, 'error', autoClose);
  }

  function dismiss() {
    if (autoCloseTimeout) {
      clearTimeout(autoCloseTimeout);
      autoCloseTimeout = null;
    }
    showNotification.value = false;
    notificationMessage.value = '';
    notificationType.value = 'info';
    notificationAutoClose.value = 3000;
  }

  watch(showNotification, (newValue) => {
    if (!newValue && autoCloseTimeout) {
      clearTimeout(autoCloseTimeout);
      autoCloseTimeout = null;
    }
  });

  return {
    showNotification,
    notificationMessage,
    notificationType,
    notificationAutoClose,
    showMessage,
    showSuccess,
    showInfo,
    showWarning,
    showError,
    dismiss,
  };
}
