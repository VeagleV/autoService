import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8082/api", // адрес твоего backend
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
