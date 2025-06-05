import { reactive } from 'vue';
import { ZodError, type ZodIssue } from 'zod';

type FormErrors = Record<string, string | null>;

export function useFormErrors(initialErrors: FormErrors) {
  const errors = reactive({ ...initialErrors });

  const mapZodErrors = (zodError: ZodError) => {
    Object.keys(errors).forEach((key) => {
      errors[key] = null;
    });

    zodError.errors.forEach((issue: ZodIssue) => {
      const fieldName = issue.path[0];
      if (typeof fieldName === 'string' && fieldName in errors) {
        errors[fieldName] = issue.message;
      }
    });
  };

  const clearErrors = () => {
    Object.keys(errors).forEach((key) => {
      errors[key] = null;
    });
  };

  return { errors, mapZodErrors, clearErrors };
}
