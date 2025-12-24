import { createRouter, createWebHistory } from "vue-router";
import CarList from "../components/CarList.vue";
import HomePage from "../components/HomePage.vue";
import UserRequests from "../components/UserRequests.vue";

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
  {
    path: "/requests",
    name: "UserRequests",
    component: UserRequests,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
