<template>
  <div class="monthly-shift-calendar">
    <div class="calendar-header">
      <button @click="previousMonth" class="nav-button">&lt;</button>

      <h2>{{ currentMonthName }} {{ currentYear }}</h2>

      <button @click="nextMonth" class="nav-button">&gt;</button>
    </div>

    <div v-if="isLoading" class="loading-overlay">
      <p>Loading shifts...</p>
    </div>

    <div v-else class="calendar-grid">
      <div class="week-days">
        <div v-for="day in weekDays" :key="day" class="day-name">
          {{ day }}
        </div>
      </div>

      <div class="days-of-month">
        <div
          v-for="(day, index) in calendarDays"
          :key="index"
          :class="[
            'day-cell',

            {
              'is-current-month': day.isCurrentMonth,

              'has-shifts': day.shifts.length > 0,

              'is-clickable': isInteractive && day.isCurrentMonth,
            },
          ]"
          @click="
            isInteractive &&
              day.isCurrentMonth &&
              emit('date-selected', day.date)
          "
        >
          <template v-if="day.isCurrentMonth">
            <span class="day-number">{{ day.dayNumber }}</span>

            <div v-if="day.shifts.length > 0" class="shift-list">
              <div
                v-for="shift in day.shifts"
                :key="shift.id"
                class="shift-item"
                :class="getShiftTypeClass(shift.shiftTemplate.type)"
                @click.stop="isInteractive && emit('shift-selected', shift)"
              >
                <span class="shift-time">
                  {{ formatShiftTime(shift.shiftStart, shift.shiftEnd) }}
                </span>

                <span class="shift-nurse">
                  {{ shift.nurse.firstname }} {{ shift.nurse.lastname }}
                </span>
              </div>
            </div>

            <p v-else class="no-shifts-text">No shifts</p>
          </template>

          <template v-else>
            <span class="day-number">&nbsp;</span>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import type { ShiftResponse } from '../../types/schemas/shifts.schema';
import type { CalendarDay } from '../../types/common';
import type { ShiftType } from '../../types/enums/shift-types.enum';

interface Props {
  shifts: ShiftResponse[];
  isLoading: boolean;
  isInteractive?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  isInteractive: true,
});

const emit = defineEmits<{
  (e: 'date-selected', date: string): void;
  (e: 'shift-selected', shift: ShiftResponse): void;
}>();

const currentMonth = ref(new Date().getMonth());
const currentYear = ref(new Date().getFullYear());

const weekDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

const currentMonthName = computed(() => {
  return new Date(currentYear.value, currentMonth.value).toLocaleString(
    'en-US',
    { month: 'long' },
  );
});

const calendarDays = computed<CalendarDay[]>(() => {
  const year = currentYear.value;
  const month = currentMonth.value;
  const firstOfMonth = new Date(year, month, 1);
  const lastOfMonth = new Date(year, month + 1, 0);

  const daysInMonth = lastOfMonth.getDate();
  let startDayOfWeek = firstOfMonth.getDay();

  startDayOfWeek = startDayOfWeek === 0 ? 6 : startDayOfWeek - 1;

  const daysArray: CalendarDay[] = [];

  // Dias vacios del mes
  for (let i = 0; i < startDayOfWeek; i++) {
    daysArray.push({
      date: '',
      dayNumber: 0,
      isCurrentMonth: false,
      shifts: [],
    });
  }

  // Dias del mes actual
  for (let dayNum = 1; dayNum <= daysInMonth; dayNum++) {
    const dateObj = new Date(year, month, dayNum);
    const dateString = dateObj.toISOString().split('T')[0];
    const shiftsForDay = props.shifts.filter(
      (s) => s.shiftDate.split('T')[0] === dateString,
    );

    daysArray.push({
      date: dateString,
      dayNumber: dayNum,
      isCurrentMonth: true,
      shifts: shiftsForDay,
    });
  }

  // Dias vacios al final de la semana
  const totalCells = daysArray.length;
  const remainder = totalCells % 7;

  if (remainder !== 0) {
    const emptyToAdd = 7 - remainder;

    for (let i = 0; i < emptyToAdd; i++) {
      daysArray.push({
        date: '',
        dayNumber: 0,
        isCurrentMonth: false,
        shifts: [],
      });
    }
  }

  return daysArray;
});

const previousMonth = () => {
  if (currentMonth.value === 0) {
    currentMonth.value = 11;
    currentYear.value -= 1;
  } else {
    currentMonth.value -= 1;
  }
};

const nextMonth = () => {
  if (currentMonth.value === 11) {
    currentMonth.value = 0;
    currentYear.value += 1;
  } else {
    currentMonth.value += 1;
  }
};

const formatTime = (timeString: string) => {
  const date = new Date(timeString);

  return date.toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  });
};

const formatShiftTime = (start: string, end: string) => {
  return `${formatTime(start)} - ${formatTime(end)}`;
};

function getShiftTypeClass(shiftType: ShiftType): string {
  let cls = shiftType.toLowerCase();
  cls = cls.replace(/_/g, '-');

  return cls;
}
</script>

<style lang="scss" scoped>
@use 'MonthlyCalendar.scss';
</style>
