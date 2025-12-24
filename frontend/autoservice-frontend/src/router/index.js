import { createRouter, createWebHistory } from "vue-router";
import CarList from "../components/CarList.vue";
import HomePage from "../components/HomePage.vue";

const routes = [
  {
    path: "/cars",
    name: "Cars",
    component: CarList,
  },
  {
    path: "/",
    name: "HomePage",
    component: HomePage,
  },
  // другие маршруты
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
