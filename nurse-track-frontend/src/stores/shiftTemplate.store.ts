import { defineStore } from 'pinia';
import type { ShiftTemplateResponse } from '../types/schemas/shifts.schema';
import { useShiftTemplateService } from '../services/shared/shiftTemplate.service';

export const useShiftTemplatesStore = defineStore('shiftTemplates', {
  state: () => ({
    shiftTemplates: [] as ShiftTemplateResponse[],
  }),

  actions: {
    async fetchShiftTemplates() {
      this.shiftTemplates = await useShiftTemplateService.getShiftTemplates();
    },

    async fetchShiftTemplateById(shiftTemplateId: number) {
      const shiftTemplate = await useShiftTemplateService.getShiftTemplateById(
        shiftTemplateId,
      );
      const shiftTemplateIndex = this.shiftTemplates.findIndex(
        (shiftTemplate) => shiftTemplate.id === shiftTemplateId,
      );

      if (shiftTemplateIndex !== -1) {
        this.shiftTemplates[shiftTemplateIndex] = shiftTemplate;
      } else {
        this.shiftTemplates.push(shiftTemplate);
      }
    },
  },
});
