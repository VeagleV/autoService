<template>
  <div>
    <h2>Список автомобилей</h2>
    <ul>
      <li v-for="car in cars" :key="car.carId">
        {{ car.brand }} {{ car.model }} ({{ car.year }})
      </li>
    </ul>
  </div>
</template>

<script>
import carService from "../services/carService";

export default {
  data() {
    return {
      cars: [],
    };
  },
  created() {
    carService.getAllCars().then((response) => {
      this.cars = response.data;
    });
  },
};
</script>
