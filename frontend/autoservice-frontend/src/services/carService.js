// src/services/CarService.js
import axios from "axios";

export default {
  getAllCars() {
    return axios.get("/api/car");
  },
  getCar(id) {
    return axios.get(`/api/car/${id}`);
  },
  createCar(car) {
    return axios.post("/api/car", car);
  },
  deleteCar(id) {
    return axios.delete(`/api/car/${id}`);
  },
};
