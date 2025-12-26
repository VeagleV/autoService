import apiClient from "./api";

/**
 * Сервис для работы с запросами к базе данных
 */
export default {
  /**
   * Запрос 1: Поиск клиентов по email
   */
  findClientsByEmail(emailPattern) {
    return apiClient.get("/queries/clients-by-email", {
      params: { email: emailPattern },
    });
  },

  /**
   * Запрос 2: Статистика заказов по периоду
   */
  getOrderStatistics(startDate, endDate) {
    return apiClient.get("/queries/orders-statistics", {
      params: { startDate, endDate },
    });
  },

  /**
   * Запрос 3: Топ самых дорогих услуг
   */
  getTopExpensiveServices(limit) {
    return apiClient.get("/queries/top-services", {
      params: { limit },
    });
  },

  /**
   * Запрос 4: Общая стоимость заказов клиента
   */
  getClientTotalOrders(clientId) {
    return apiClient.get("/queries/client-total", {
      params: { clientId },
    });
  },

  /**
   * Запрос 5: Неоплаченные заказы с суммой выше указанной
   */
  findUnpaidOrdersAboveSum(minSum) {
    return apiClient.get("/queries/unpaid-orders", {
      params: { minSum },
    });
  },
};
