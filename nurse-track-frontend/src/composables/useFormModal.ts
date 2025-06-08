import { reactive, watch } from 'vue';

export function useFormModal<T extends object>(
  initialFormData: T,
  validate: (data: T) => { success: boolean; errors?: any; data?: T }
) {
  const formData = reactive({ ...initialFormData });
  const errors = reactive<Record<string, string | null>>({});

  const resetForm = () => {
    Object.assign(formData, initialFormData);
    Object.keys(errors).forEach((key) => (errors[key] = null));
  };

  const submitForm = (emit: Function) => {
    Object.keys(errors).forEach((key) => (errors[key] = null));
    const cleanData: any = { ...formData };

    // Limpieza de campos vacíos o nulos
    Object.keys(cleanData).forEach((key) => {
      if (cleanData[key] === '' || cleanData[key] === null) {
        cleanData[key] = undefined;
      }
    });

    const result = validate(cleanData);

    if (result.success) {
      emit('submit', result.data);
    } else {
      Object.assign(errors, result.errors);
    }
  };

  return { formData, errors, resetForm, submitForm };
}