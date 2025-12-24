import apiClient from "./api";

export default {
  getAll(params = {}) {
    return apiClient.get("/requests", { params });
  },

  getById(id) {
    return apiClient.get(`/requests/${id}`);
  },

  create(data) {
    return apiClient.post("/requests", data);
  },

  update(id, data) {
    return apiClient.put(`/requests/${id}`, data);
  },

  delete(id) {
    return apiClient.delete(`/requests/${id}`);
  },
};
