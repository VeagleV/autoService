import { createRouter, createWebHistory } from "vue-router";
import HomePage from "../components/HomePage.vue";
import DatabaseQueries from "../components/DatabaseQueries.vue";

const routes = [
  {
    path: "/",
    name: "HomePage",
    component: HomePage,
  },
  {
    path: "/database-queries",
    name: "DatabaseQueries",
    component: DatabaseQueries,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
