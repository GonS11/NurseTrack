import { ref } from 'vue';

export type NotificationType = 'info' | 'success' | 'warning' | 'error';

const showNotification = ref(false);
const notificationMessage = ref('');
const notificationType = ref<NotificationType>('info');
const notificationTimeout = ref<ReturnType<typeof setTimeout> | null>(null);
const notificationAutoClose = ref(true); // Controla si se cierra automáticamente

export function useNotifications() {
  /**
   * Muestra una notificación global en la aplicación.
   * @param message El mensaje a mostrar.
   * @param type El tipo de notificación (info, success, warning, error).
   * @param autoClose Duración en milisegundos si se cierra automáticamente, o boolean (true para 5s, false para no cerrar).
   */
  const displayNotification = (
    message: string,
    type: NotificationType, // Aquí debe ser un tipo requerido según tus errores
    autoClose: boolean | number | undefined = true, // Acepta boolean, number o undefined
  ) => {
    if (notificationTimeout.value) {
      clearTimeout(notificationTimeout.value);
    }

    notificationMessage.value = message;
    notificationType.value = type;
    showNotification.value = true;

    let actualAutoClose = true;
    let duration = 5000; // Duración por defecto

    if (typeof autoClose === 'boolean') {
      actualAutoClose = autoClose;
      duration = 5000; // Si es boolean, asumimos 5 segundos por defecto
    } else if (typeof autoClose === 'number') {
      actualAutoClose = true; // Si es un número, siempre se auto-cierra
      duration = autoClose;
    } else {
      actualAutoClose = true; // Si es undefined, por defecto se auto-cierra en 5 segundos
    }

    notificationAutoClose.value = actualAutoClose;

    if (actualAutoClose) {
      notificationTimeout.value = setTimeout(() => {
        dismiss();
      }, duration);
    }
  };

  // Las funciones específicas ahora deben pasar el tipo obligatoriamente
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
