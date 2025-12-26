<template>
  <div class="database-queries-page">
    <div class="page-header">
      <h1 class="page-title">🔍 Запросы к базе данных</h1>
      <p class="page-subtitle">
        Выполнение SQL-запросов для анализа данных автосервиса
      </p>
    </div>

    <!-- Query Tabs -->
    <div class="query-tabs">
      <button
        v-for="(query, index) in queries"
        :key="index"
        :class="['tab-button', { active: activeTab === index }]"
        @click="activeTab = index"
      >
        {{ query.title }}
      </button>
    </div>

    <!-- Query Content -->
    <div class="query-content card">
      <!-- Query 1: Поиск клиентов по email -->
      <div v-if="activeTab === 0" class="query-section">
        <h2 class="query-title">{{ queries[0].title }}</h2>
        <p class="query-description">{{ queries[0].description }}</p>

        <div class="query-form">
          <div class="form-group">
            <label class="form-label">Часть email для поиска:</label>
            <input
              v-model="query1.emailPattern"
              type="text"
              class="input"
              placeholder="Например: gmail"
            />
          </div>
          <button @click="executeQuery1" class="btn btn-primary">
            ▶️ Выполнить запрос
          </button>
        </div>

        <div v-if="query1.sql" class="sql-display">
          <strong>SQL запрос:</strong>
          <code>{{ query1.sql }}</code>
        </div>

        <div v-if="query1.loading" class="loading-state">
          <div class="spinner"></div>
          <p>Выполнение запроса...</p>
        </div>

        <div v-if="query1.error" class="error-message">
          ⚠️ {{ query1.error }}
        </div>

        <div v-if="query1.results" class="results-section">
          <h3 class="results-title">
            Результаты ({{ query1.results.length }} записей):
          </h3>
          <div v-if="query1.results.length > 0" class="table-container">
            <table class="results-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Фамилия</th>
                  <th>Имя</th>
                  <th>Отчество</th>
                  <th>Email</th>
                  <th>Телефон</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="client in query1.results" :key="client.clientId">
                  <td>{{ client.clientId }}</td>
                  <td>{{ client.surname }}</td>
                  <td>{{ client.name }}</td>
                  <td>{{ client.middleName || "-" }}</td>
                  <td>{{ client.email }}</td>
                  <td>{{ client.phone }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-else class="empty-results">📭 Клиенты не найдены</div>
        </div>
      </div>

      <!-- Query 2: Статистика заказов по периоду -->
      <div v-if="activeTab === 1" class="query-section">
        <h2 class="query-title">{{ queries[1].title }}</h2>
        <p class="query-description">{{ queries[1].description }}</p>

        <div class="query-form">
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Дата начала:</label>
              <input
                v-model="query2.startDate"
                type="date"
                class="input"
                required
              />
            </div>
            <div class="form-group">
              <label class="form-label">Дата окончания:</label>
              <input
                v-model="query2.endDate"
                type="date"
                class="input"
                required
              />
            </div>
          </div>
          <button @click="executeQuery2" class="btn btn-primary">
            ▶️ Выполнить запрос
          </button>
        </div>

        <div v-if="query2.sql" class="sql-display">
          <strong>SQL запрос:</strong>
          <code>{{ query2.sql }}</code>
        </div>

        <div v-if="query2.loading" class="loading-state">
          <div class="spinner"></div>
          <p>Выполнение запроса...</p>
        </div>

        <div v-if="query2.error" class="error-message">
          ⚠️ {{ query2.error }}
        </div>

        <div v-if="query2.results" class="results-section">
          <h3 class="results-title">Статистика заказов:</h3>
          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon">📊</div>
              <div class="stat-value">{{ query2.results.orderCount }}</div>
              <div class="stat-label">Количество заказов</div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">💰</div>
              <div class="stat-value">
                {{ formatCurrency(query2.results.totalSum) }}
              </div>
              <div class="stat-label">Общая сумма</div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">📈</div>
              <div class="stat-value">
                {{ formatCurrency(query2.results.averageSum) }}
              </div>
              <div class="stat-label">Средняя сумма</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Query 3: Топ самых дорогих услуг -->
      <div v-if="activeTab === 2" class="query-section">
        <h2 class="query-title">{{ queries[2].title }}</h2>
        <p class="query-description">{{ queries[2].description }}</p>

        <div class="query-form">
          <div class="form-group">
            <label class="form-label">Количество услуг (топ-N):</label>
            <input
              v-model.number="query3.limit"
              type="number"
              class="input"
              min="1"
              max="100"
              placeholder="Например: 5"
            />
          </div>
          <button @click="executeQuery3" class="btn btn-primary">
            ▶️ Выполнить запрос
          </button>
        </div>

        <div v-if="query3.sql" class="sql-display">
          <strong>SQL запрос:</strong>
          <code>{{ query3.sql }}</code>
        </div>

        <div v-if="query3.loading" class="loading-state">
          <div class="spinner"></div>
          <p>Выполнение запроса...</p>
        </div>

        <div v-if="query3.error" class="error-message">
          ⚠️ {{ query3.error }}
        </div>

        <div v-if="query3.results" class="results-section">
          <h3 class="results-title">
            Топ-{{ query3.limit }} самых дорогих услуг:
          </h3>
          <div v-if="query3.results.length > 0" class="services-grid">
            <div
              v-for="(service, index) in query3.results"
              :key="service.serviceId"
              class="service-card"
            >
              <div class="service-rank">{{ index + 1 }}</div>
              <div class="service-info">
                <h4 class="service-name">{{ service.name }}</h4>
                <div class="service-price">
                  {{ formatCurrency(service.price) }}
                </div>
              </div>
            </div>
          </div>
          <div v-else class="empty-results">📭 Услуги не найдены</div>
        </div>
      </div>

      <!-- Query 4: Общая стоимость заказов клиента -->
      <div v-if="activeTab === 3" class="query-section">
        <h2 class="query-title">{{ queries[3].title }}</h2>
        <p class="query-description">{{ queries[3].description }}</p>

        <div class="query-form">
          <div class="form-group">
            <label class="form-label">ID клиента:</label>
            <input
              v-model.number="query4.clientId"
              type="number"
              class="input"
              min="1"
              placeholder="Введите ID клиента"
            />
          </div>
          <button @click="executeQuery4" class="btn btn-primary">
            ▶️ Выполнить запрос
          </button>
        </div>

        <div v-if="query4.sql" class="sql-display">
          <strong>SQL запрос:</strong>
          <code>{{ query4.sql }}</code>
        </div>

        <div v-if="query4.loading" class="loading-state">
          <div class="spinner"></div>
          <p>Выполнение запроса...</p>
        </div>

        <div v-if="query4.error" class="error-message">
          ⚠️ {{ query4.error }}
        </div>

        <div v-if="query4.results" class="results-section">
          <h3 class="results-title">Информация о клиенте:</h3>
          <div class="client-total-card">
            <div class="client-info">
              <div class="info-row">
                <span class="info-label">ID клиента:</span>
                <span class="info-value">{{ query4.results.clientId }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Фамилия:</span>
                <span class="info-value">{{ query4.results.surname }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Имя:</span>
                <span class="info-value">{{ query4.results.name }}</span>
              </div>
              <div class="info-row total-row">
                <span class="info-label">Общая сумма заказов:</span>
                <span class="info-value total-value">
                  {{ formatCurrency(query4.results.totalSum) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Query 5: Неоплаченные заказы с суммой выше указанной -->
      <div v-if="activeTab === 4" class="query-section">
        <h2 class="query-title">{{ queries[4].title }}</h2>
        <p class="query-description">{{ queries[4].description }}</p>

        <div class="query-form">
          <div class="form-group">
            <label class="form-label">Минимальная сумма:</label>
            <input
              v-model.number="query5.minSum"
              type="number"
              class="input"
              min="0"
              step="0.01"
              placeholder="Например: 5000"
            />
          </div>
          <button @click="executeQuery5" class="btn btn-primary">
            ▶️ Выполнить запрос
          </button>
        </div>

        <div v-if="query5.sql" class="sql-display">
          <strong>SQL запрос:</strong>
          <code>{{ query5.sql }}</code>
        </div>

        <div v-if="query5.loading" class="loading-state">
          <div class="spinner"></div>
          <p>Выполнение запроса...</p>
        </div>

        <div v-if="query5.error" class="error-message">
          ⚠️ {{ query5.error }}
        </div>

        <div v-if="query5.results" class="results-section">
          <h3 class="results-title">
            Неоплаченные заказы ({{ query5.results.length }} записей):
          </h3>
          <div v-if="query5.results.length > 0" class="table-container">
            <table class="results-table">
              <thead>
                <tr>
                  <th>ID заказа</th>
                  <th>Дата заказа</th>
                  <th>Сумма</th>
                  <th>Статус оплаты</th>
                  <th>ID клиента</th>
                  <th>ID машины</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="order in query5.results" :key="order.orderId">
                  <td>{{ order.orderId }}</td>
                  <td>{{ formatDate(order.orderDate) }}</td>
                  <td class="amount">{{ formatCurrency(order.totalSum) }}</td>
                  <td>
                    <span class="status-badge status-unpaid">
                      {{ getPaymentStatus(order.paymentStatus) }}
                    </span>
                  </td>
                  <td>{{ order.clientId }}</td>
                  <td>{{ order.carId }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-else class="empty-results">
            📭 Неоплаченные заказы не найдены
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import queryService from "../services/queryService";

const activeTab = ref(0);

const queries = [
  {
    title: "Поиск клиентов по email",
    description:
      "Найти клиентов, у которых в email содержится указанная подстрока",
  },
  {
    title: "Статистика заказов по периоду",
    description: "Получить статистику заказов за указанный период времени",
  },
  {
    title: "Топ самых дорогих услуг",
    description: "Получить список самых дорогих услуг автосервиса",
  },
  {
    title: "Общая стоимость заказов клиента",
    description: "Вычислить общую сумму всех заказов конкретного клиента",
  },
  {
    title: "Неоплаченные заказы",
    description: "Найти неоплаченные заказы с суммой выше указанной",
  },
];

// Query 1 state
const query1 = reactive({
  emailPattern: "",
  results: null,
  loading: false,
  error: null,
  sql: null,
});

// Query 2 state
const query2 = reactive({
  startDate: "",
  endDate: "",
  results: null,
  loading: false,
  error: null,
  sql: null,
});

// Query 3 state
const query3 = reactive({
  limit: 5,
  results: null,
  loading: false,
  error: null,
  sql: null,
});

// Query 4 state
const query4 = reactive({
  clientId: null,
  results: null,
  loading: false,
  error: null,
  sql: null,
});

// Query 5 state
const query5 = reactive({
  minSum: null,
  results: null,
  loading: false,
  error: null,
  sql: null,
});

// Execute Query 1
async function executeQuery1() {
  if (!query1.emailPattern) {
    query1.error = "Пожалуйста, введите часть email для поиска";
    return;
  }

  query1.loading = true;
  query1.error = null;
  query1.results = null;
  query1.sql = `SELECT * FROM client WHERE email LIKE '%${query1.emailPattern}%'`;

  try {
    const response = await queryService.findClientsByEmail(query1.emailPattern);
    query1.results = response.data;
  } catch (err) {
    query1.error =
      "Ошибка при выполнении запроса: " + (err.message || "Неизвестная ошибка");
  } finally {
    query1.loading = false;
  }
}

// Execute Query 2
async function executeQuery2() {
  if (!query2.startDate || !query2.endDate) {
    query2.error = "Пожалуйста, укажите обе даты";
    return;
  }

  query2.loading = true;
  query2.error = null;
  query2.results = null;
  query2.sql = `SELECT COUNT(*), SUM(total_sum), AVG(total_sum) FROM "order" WHERE order_date BETWEEN '${query2.startDate}' AND '${query2.endDate}'`;

  try {
    const response = await queryService.getOrderStatistics(
      query2.startDate,
      query2.endDate
    );
    query2.results = response.data;
  } catch (err) {
    query2.error =
      "Ошибка при выполнении запроса: " + (err.message || "Неизвестная ошибка");
  } finally {
    query2.loading = false;
  }
}

// Execute Query 3
async function executeQuery3() {
  if (!query3.limit || query3.limit < 1) {
    query3.error = "Пожалуйста, укажите корректное количество услуг";
    return;
  }

  query3.loading = true;
  query3.error = null;
  query3.results = null;
  query3.sql = `SELECT * FROM service ORDER BY price DESC LIMIT ${query3.limit}`;

  try {
    const response = await queryService.getTopExpensiveServices(query3.limit);
    query3.results = response.data;
  } catch (err) {
    query3.error =
      "Ошибка при выполнении запроса: " + (err.message || "Неизвестная ошибка");
  } finally {
    query3.loading = false;
  }
}

// Execute Query 4
async function executeQuery4() {
  if (!query4.clientId || query4.clientId < 1) {
    query4.error = "Пожалуйста, укажите корректный ID клиента";
    return;
  }

  query4.loading = true;
  query4.error = null;
  query4.results = null;
  query4.sql = `SELECT c.name, c.surname, SUM(o.total_sum) FROM client c JOIN "order" o ON c.client_id = o.client_id WHERE c.client_id = ${query4.clientId} GROUP BY c.client_id`;

  try {
    const response = await queryService.getClientTotalOrders(query4.clientId);
    query4.results = response.data;
  } catch (err) {
    query4.error =
      "Ошибка при выполнении запроса: " + (err.message || "Неизвестная ошибка");
  } finally {
    query4.loading = false;
  }
}

// Execute Query 5
async function executeQuery5() {
  if (query5.minSum === null || query5.minSum < 0) {
    query5.error = "Пожалуйста, укажите корректную минимальную сумму";
    return;
  }

  query5.loading = true;
  query5.error = null;
  query5.results = null;
  query5.sql = `SELECT * FROM "order" WHERE payment_status = 'NOT_PAID' AND total_sum >= ${query5.minSum} ORDER BY total_sum DESC`;

  try {
    const response = await queryService.findUnpaidOrdersAboveSum(query5.minSum);
    query5.results = response.data;
  } catch (err) {
    query5.error =
      "Ошибка при выполнении запроса: " + (err.message || "Неизвестная ошибка");
  } finally {
    query5.loading = false;
  }
}

// Helper functions
function formatCurrency(value) {
  if (value === null || value === undefined) return "0.00 ₽";
  return new Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB",
  }).format(value);
}

function formatDate(dateString) {
  if (!dateString) return "-";
  const date = new Date(dateString);
  return date.toLocaleDateString("ru-RU", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
}

function getPaymentStatus(status) {
  const statuses = {
    PAID: "Оплачен",
    NOT_PAID: "Не оплачен",
  };
  return statuses[status] || status;
}
</script>

<style scoped>
.database-queries-page {
  max-width: 100%;
}

.page-header {
  text-align: center;
  margin-bottom: 2rem;
  animation: fadeInDown 0.5s ease-out;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.page-title {
  font-size: 2.5rem;
  font-weight: 800;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.page-subtitle {
  font-size: 1.125rem;
  color: var(--text-secondary);
}

.query-tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  overflow-x: auto;
  padding-bottom: 0.5rem;
}

.tab-button {
  padding: 0.75rem 1.5rem;
  border: 2px solid var(--border-color);
  background: var(--bg-primary);
  color: var(--text-secondary);
  border-radius: var(--radius-lg);
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all var(--transition-fast);
  white-space: nowrap;
}

.tab-button:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.tab-button.active {
  background: var(--primary-color);
  border-color: var(--primary-color);
  color: white;
  box-shadow: var(--shadow-md);
}

.query-content {
  padding: 2rem;
}

.query-section {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.query-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.query-description {
  font-size: 1rem;
  color: var(--text-secondary);
  margin-bottom: 1.5rem;
}

.query-form {
  background: var(--bg-secondary);
  padding: 1.5rem;
  border-radius: var(--radius-lg);
  margin-bottom: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.form-label {
  font-weight: 600;
  font-size: 0.875rem;
  color: var(--text-primary);
}

.sql-display {
  background: #1e293b;
  color: #e2e8f0;
  padding: 1rem;
  border-radius: var(--radius-md);
  margin-bottom: 1.5rem;
  font-family: "Courier New", monospace;
  overflow-x: auto;
}

.sql-display code {
  color: #94a3b8;
  font-size: 0.875rem;
  display: block;
  margin-top: 0.5rem;
}

.loading-state {
  text-align: center;
  padding: 2rem;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid var(--border-color);
  border-top-color: var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.error-message {
  background: #fef2f2;
  border: 1px solid #fecaca;
  color: var(--danger-color);
  padding: 1rem;
  border-radius: var(--radius-md);
  margin-bottom: 1.5rem;
}

.results-section {
  margin-top: 1.5rem;
}

.results-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 1rem;
}

.table-container {
  overflow-x: auto;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.results-table {
  width: 100%;
  border-collapse: collapse;
  background: var(--bg-primary);
}

.results-table th {
  background: var(--primary-color);
  color: white;
  padding: 0.75rem 1rem;
  text-align: left;
  font-weight: 600;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.results-table td {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--border-color);
  color: var(--text-primary);
  font-size: 0.875rem;
}

.results-table tr:last-child td {
  border-bottom: none;
}

.results-table tr:hover {
  background: var(--bg-secondary);
}

.results-table td.amount {
  font-weight: 600;
  color: var(--secondary-color);
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-md);
  font-size: 0.75rem;
  font-weight: 600;
  white-space: nowrap;
}

.status-unpaid {
  background: #fee2e2;
  color: #991b1b;
}

.empty-results {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
  font-size: 1.125rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-top: 1rem;
}

.stat-card {
  background: linear-gradient(
    135deg,
    var(--primary-color),
    var(--primary-dark)
  );
  color: white;
  padding: 1.5rem;
  border-radius: var(--radius-xl);
  text-align: center;
  box-shadow: var(--shadow-lg);
  transition: transform var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-4px);
}

.stat-icon {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
}

.stat-value {
  font-size: 1.75rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  opacity: 0.9;
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1rem;
}

.service-card {
  background: var(--bg-primary);
  border: 2px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: 1rem;
  display: flex;
  gap: 1rem;
  align-items: center;
  transition: all var(--transition-fast);
}

.service-card:hover {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-md);
}

.service-rank {
  width: 50px;
  height: 50px;
  background: var(--primary-color);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  font-weight: 700;
  flex-shrink: 0;
}

.service-info {
  flex: 1;
}

.service-name {
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0.25rem;
}

.service-price {
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--secondary-color);
}

.client-total-card {
  background: var(--bg-primary);
  border: 2px solid var(--border-color);
  border-radius: var(--radius-xl);
  padding: 2rem;
  max-width: 600px;
  margin: 0 auto;
}

.client-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 0.75rem 0;
  border-bottom: 1px solid var(--border-color);
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  font-weight: 600;
  color: var(--text-secondary);
}

.info-value {
  color: var(--text-primary);
  font-weight: 500;
}

.total-row {
  background: var(--bg-secondary);
  padding: 1rem;
  border-radius: var(--radius-md);
  margin-top: 0.5rem;
}

.total-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--secondary-color);
}

@media (max-width: 768px) {
  .page-title {
    font-size: 1.75rem;
  }

  .query-tabs {
    flex-direction: column;
  }

  .tab-button {
    width: 100%;
  }

  .query-content {
    padding: 1rem;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .services-grid {
    grid-template-columns: 1fr;
  }

  .results-table {
    font-size: 0.75rem;
  }

  .results-table th,
  .results-table td {
    padding: 0.5rem;
  }
}
</style>
