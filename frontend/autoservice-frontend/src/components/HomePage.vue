<template>
  <div class="home-page">
    <div class="page-header">
      <h1 class="page-title">Управление базой данных AutoService</h1>
      <p class="page-subtitle">
        Выберите таблицу для просмотра, редактирования и управления данными
      </p>
    </div>

    <div class="table-selector card">
      <label for="table-select" class="select-label">Выберите таблицу:</label>
      <select
        id="table-select"
        v-model="selectedTable"
        @change="fetchData"
        class="table-select input"
      >
        <option disabled value="">-- Выберите таблицу --</option>
        <option v-for="table in tables" :key="table.value" :value="table.value">
          {{ table.label }}
        </option>
      </select>
    </div>

    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Загрузка данных...</p>
    </div>

    <div v-if="error" class="error-message card">
      <span class="error-icon">⚠️</span>
      <span>Ошибка: {{ error }}</span>
    </div>

    <div v-if="data.length && !loading" class="data-table-container card">
      <div class="table-header">
        <h2 class="table-title">{{ getTableLabel() }}</h2>
        <span class="table-count">Всего записей: {{ data.length }}</span>
      </div>

      <div class="table-wrapper">
        <table class="data-table">
          <thead>
            <tr>
              <th v-for="col in columns" :key="col" class="table-th">
                {{ col }}
              </th>
              <th class="table-th actions-column">Действия</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(row, idx) in data"
              :key="row[getIdField()]"
              class="table-row"
            >
              <td v-for="col in columns" :key="col" class="table-td">
                <template v-if="editIndex === idx">
                  <input
                    v-model="editRow[col]"
                    class="input table-input"
                    :type="getInputType(col)"
                    :readonly="col === getIdField()"
                  />
                </template>
                <template v-else>
                  {{ row[col] }}
                </template>
              </td>
              <td class="table-td actions-cell">
                <template v-if="editIndex === idx">
                  <button
                    @click="saveEdit(row[getIdField()])"
                    class="btn btn-sm btn-secondary"
                  >
                    ✓ Сохранить
                  </button>
                  <button @click="cancelEdit" class="btn btn-sm btn-outline">
                    ✕ Отмена
                  </button>
                </template>
                <template v-else>
                  <button
                    @click="startEdit(idx)"
                    class="btn btn-sm btn-primary"
                  >
                    ✏️ Редактировать
                  </button>
                  <button
                    @click="deleteRow(row[getIdField()])"
                    class="btn btn-sm btn-danger"
                  >
                    🗑️ Удалить
                  </button>
                </template>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div
      v-else-if="selectedTable && !loading && !error"
      class="empty-state card"
    >
      <span class="empty-icon">📭</span>
      <h3>Данные отсутствуют</h3>
      <p>В данной таблице пока нет записей. Добавьте первую запись ниже.</p>
    </div>

    <!-- Форма добавления -->
    <div v-if="selectedTable" class="add-form-container card">
      <h2 class="form-title">➕ Добавить новую запись</h2>
      <form @submit.prevent="addRow" class="add-form">
        <div class="form-grid">
          <div v-for="col in columns" :key="col" class="form-group">
            <label class="form-label" :for="'new-' + col">{{ col }}:</label>
            <input
              v-model="newRow[col]"
              :id="'new-' + col"
              :type="getInputType(col)"
              :readonly="col === getIdField()"
              class="input"
              :placeholder="col"
              :required="col !== getIdField()"
            />
          </div>
        </div>
        <button type="submit" class="btn btn-secondary submit-btn">
          Добавить запись
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from "vue";

const tables = [
  { label: "🚗 Автомобили (Cars)", value: "cars" },
  { label: "👤 Клиенты (Clients)", value: "clients" },
  { label: "📋 Заказы (Orders)", value: "orders" },
  { label: "🔧 Заказы-услуги (OrderServices)", value: "orderservices" },
  { label: "⚙️ Детали (Parts)", value: "parts" },
  { label: "🛠️ Услуги (Services)", value: "services" },
  { label: "🔩 Услуги-детали (ServiceParts)", value: "serviceparts" },
];

const selectedTable = ref("");
const data = ref([]);
const columns = ref([]);
const loading = ref(false);
const error = ref("");

const editIndex = ref(-1);
const editRow = reactive({});
const newRow = reactive({});

const baseUrl = "http://localhost:8082/api";

function getTableLabel() {
  const table = tables.find((t) => t.value === selectedTable.value);
  return table ? table.label : "";
}

function getIdField() {
  if (!columns.value.length) return "";
  return (
    columns.value.find((col) => col.toLowerCase().endsWith("id")) ||
    columns.value[0]
  );
}

function getInputType(col) {
  if (col.toLowerCase().includes("date")) return "date";
  if (col.toLowerCase().includes("email")) return "email";
  if (
    col.toLowerCase().includes("price") ||
    col.toLowerCase().includes("sum") ||
    col.toLowerCase().includes("year")
  )
    return "number";
  if (col.toLowerCase().includes("id")) return "number";
  return "text";
}

async function fetchData() {
  if (!selectedTable.value) {
    data.value = [];
    columns.value = [];
    return;
  }

  loading.value = true;
  error.value = "";
  data.value = [];
  columns.value = [];
  editIndex.value = -1;

  try {
    let endpoint = "";
    switch (selectedTable.value) {
      case "cars":
        endpoint = "/cars";
        break;
      case "clients":
        endpoint = "/clients";
        break;
      case "orders":
        endpoint = "/orders";
        break;
      case "orderservices":
        endpoint = "/orderservices";
        break;
      case "parts":
        endpoint = "/parts";
        break;
      case "services":
        endpoint = "/services";
        break;
      case "serviceparts":
        endpoint = "/serviceparts";
        break;
      default:
        endpoint = "";
    }

    if (!endpoint) {
      throw new Error("Неверный выбор таблицы");
    }

    const res = await fetch(baseUrl + endpoint);
    if (!res.ok) throw new Error(`Ошибка сервера: ${res.status}`);

    const json = await res.json();
    data.value = json;

    if (data.value.length > 0) {
      columns.value = Object.keys(data.value[0]);
    } else {
      columns.value = [];
    }

    resetNewRow();
  } catch (e) {
    error.value = e.message || "Ошибка при загрузке данных";
  } finally {
    loading.value = false;
  }
}

function resetNewRow() {
  columns.value.forEach((col) => {
    newRow[col] = col.toLowerCase().endsWith("id") ? null : "";
  });
}

function startEdit(idx) {
  editIndex.value = idx;
  const row = data.value[idx];
  Object.keys(editRow).forEach((k) => delete editRow[k]);
  Object.assign(editRow, row);
}

function cancelEdit() {
  editIndex.value = -1;
}

async function saveEdit(id) {
  error.value = "";
  try {
    const endpoint = `/${selectedTable.value}/${id}`;
    const res = await fetch(baseUrl + endpoint, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(editRow),
    });
    if (!res.ok) throw new Error(`Ошибка при сохранении: ${res.status}`);

    await fetchData();
    editIndex.value = -1;
  } catch (e) {
    error.value = e.message || "Ошибка при сохранении";
  }
}

async function deleteRow(id) {
  if (!confirm("Вы уверены, что хотите удалить эту запись?")) return;
  error.value = "";
  try {
    const endpoint = `/${selectedTable.value}/${id}`;
    const res = await fetch(baseUrl + endpoint, { method: "DELETE" });
    if (!res.ok) throw new Error(`Ошибка при удалении: ${res.status}`);

    await fetchData();
  } catch (e) {
    error.value = e.message || "Ошибка при удалении";
  }
}

async function addRow() {
  error.value = "";
  try {
    const body = { ...newRow };
    const idField = getIdField();
    if (idField in body) delete body[idField];

    const endpoint = `/${selectedTable.value}`;
    const res = await fetch(baseUrl + endpoint, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });
    if (!res.ok) throw new Error(`Ошибка при добавлении: ${res.status}`);

    await fetchData();
  } catch (e) {
    error.value = e.message || "Ошибка при добавлении";
  }
}

watch(selectedTable, () => {
  data.value = [];
  columns.value = [];
  error.value = "";
  editIndex.value = -1;
  resetNewRow();
});
</script>

<style scoped>
.home-page {
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
  background: linear-gradient(
    135deg,
    var(--primary-color),
    var(--secondary-color)
  );
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  font-size: 1.125rem;
  color: var(--text-secondary);
}

.table-selector {
  margin-bottom: 2rem;
  padding: 2rem;
}

.select-label {
  display: block;
  font-weight: 600;
  font-size: 1rem;
  color: var(--text-primary);
  margin-bottom: 0.75rem;
}

.table-select {
  font-size: 1rem;
  padding: 1rem;
  cursor: pointer;
}

.loading-state {
  text-align: center;
  padding: 3rem;
}

.spinner {
  width: 50px;
  height: 50px;
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
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.error-icon {
  font-size: 1.5rem;
}

.data-table-container {
  margin-bottom: 2rem;
  padding: 0;
  overflow: hidden;
}

.table-header {
  padding: 1.5rem;
  background: var(--bg-tertiary);
  border-bottom: 2px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.table-count {
  color: var(--text-secondary);
  font-size: 0.875rem;
  font-weight: 500;
}

.table-wrapper {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.table-th {
  background: var(--bg-tertiary);
  color: var(--text-primary);
  font-weight: 600;
  text-align: left;
  padding: 1rem;
  border-bottom: 2px solid var(--border-color);
  white-space: nowrap;
}

.actions-column {
  text-align: center;
  min-width: 200px;
}

.table-row {
  transition: background-color var(--transition-fast);
}

.table-row:hover {
  background: var(--bg-secondary);
}

.table-td {
  padding: 1rem;
  border-bottom: 1px solid var(--border-color);
}

.actions-cell {
  display: flex;
  gap: 0.5rem;
  justify-content: center;
  align-items: center;
}

.table-input {
  padding: 0.5rem;
  font-size: 0.875rem;
}

.btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.75rem;
}

.btn-outline {
  background: white;
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

.btn-outline:hover {
  background: var(--bg-tertiary);
  border-color: var(--text-secondary);
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: var(--text-secondary);
}

.empty-icon {
  font-size: 4rem;
  display: block;
  margin-bottom: 1rem;
}

.empty-state h3 {
  font-size: 1.5rem;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.add-form-container {
  padding: 2rem;
}

.form-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 1.5rem;
}

.add-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-weight: 600;
  font-size: 0.875rem;
  color: var(--text-primary);
}

.submit-btn {
  align-self: flex-start;
  padding: 0.875rem 2rem;
  font-size: 1rem;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 1.75rem;
  }

  .page-subtitle {
    font-size: 1rem;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .table-header {
    flex-direction: column;
    gap: 0.5rem;
    align-items: flex-start;
  }

  .actions-cell {
    flex-direction: column;
  }
}
</style>
