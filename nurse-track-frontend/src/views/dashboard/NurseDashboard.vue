<template>
  <h1>Nurse Dashboard</h1>
</template>

<script lang="ts">
import { storeToRefs } from 'pinia';
import { useNurseStore } from '../../stores/nurse.store';
import { onMounted } from 'vue';

const nurse = useNurseStore();
</script>
