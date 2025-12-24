<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">Выберите таблицу для просмотра</h1>

    <select v-model="selectedTable" @change="fetchData" class="border p-2 mb-4">
      <option disabled value="">-- Выберите таблицу --</option>
      <option v-for="table in tables" :key="table.value" :value="table.value">
        {{ table.label }}
      </option>
    </select>

    <div v-if="loading" class="mb-4">Загрузка данных...</div>
    <div v-if="error" class="mb-4 text-red-600">Ошибка: {{ error }}</div>

    <table
      v-if="data.length"
      class="table-auto border-collapse border border-gray-300 w-full"
    >
      <thead>
        <tr>
          <th
            v-for="col in columns"
            :key="col"
            class="border border-gray-300 px-2 py-1 bg-gray-100"
          >
            {{ col }}
          </th>
          <th class="border border-gray-300 px-2 py-1 bg-gray-100">Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="(row, idx) in data"
          :key="row[getIdField()]"
          class="hover:bg-gray-50"
        >
          <td
            v-for="col in columns"
            :key="col"
            class="border border-gray-300 px-2 py-1"
          >
            <template v-if="editIndex === idx">
              <input
                v-model="editRow[col]"
                class="border p-1 w-full"
                :type="getInputType(col)"
                :readonly="col === getIdField()"
              />
            </template>
            <template v-else>
              {{ row[col] }}
            </template>
          </td>
          <td class="border border-gray-300 px-2 py-1">
            <template v-if="editIndex === idx">
              <button
                @click="saveEdit(row[getIdField()])"
                class="bg-green-500 text-white px-2 py-1 mr-2 rounded"
              >
                Сохранить
              </button>
              <button
                @click="cancelEdit"
                class="bg-gray-400 text-white px-2 py-1 rounded"
              >
                Отмена
              </button>
            </template>
            <template v-else>
              <button
                @click="startEdit(idx)"
                class="bg-blue-500 text-white px-2 py-1 mr-2 rounded"
              >
                Редактировать
              </button>
              <button
                @click="deleteRow(row[getIdField()])"
                class="bg-red-500 text-white px-2 py-1 rounded"
              >
                Удалить
              </button>
            </template>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else-if="selectedTable && !loading" class="mt-4 text-gray-600">
      Данные отсутствуют
    </div>

    <!-- Форма добавления -->
    <div v-if="selectedTable" class="mt-8 border-t pt-4">
      <h2 class="text-xl font-semibold mb-2">Добавить новую запись</h2>
      <form @submit.prevent="addRow" class="space-y-2 max-w-3xl">
        <div v-for="col in columns" :key="col" class="flex items-center">
          <label class="w-48 font-medium" :for="'new-' + col">{{ col }}:</label>
          <input
            v-model="newRow[col]"
            :id="'new-' + col"
            :type="getInputType(col)"
            :readonly="col === getIdField()"
            class="border p-1 flex-1"
            :placeholder="col"
            :required="col !== getIdField()"
          />
        </div>
        <button
          type="submit"
          class="bg-green-600 text-white px-4 py-2 rounded mt-2"
        >
          Добавить
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from "vue";

const tables = [
  { label: "Автомобили (Cars)", value: "cars" },
  { label: "Клиенты (Clients)", value: "clients" },
  { label: "Заказы (Orders)", value: "orders" },
  { label: "Заказы-услуги (OrderServices)", value: "orderservices" },
  { label: "Детали (Parts)", value: "parts" },
  { label: "Услуги (Services)", value: "services" },
  { label: "Услуги-детали (ServiceParts)", value: "serviceparts" },
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

function getIdField() {
  // Здесь предполагается что ID поля заканчиваются на 'Id'
  // или можно сделать мапу для каждого типа таблицы, если надо точнее
  if (!columns.value.length) return "";
  return (
    columns.value.find((col) => col.toLowerCase().endsWith("id")) ||
    columns.value[0]
  );
}

function getInputType(col) {
  // Простая логика для типа input
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

    // Обновляем форму добавления пустыми значениями
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

// Редактирование
function startEdit(idx) {
  editIndex.value = idx;
  const row = data.value[idx];
  Object.keys(editRow).forEach((k) => delete editRow[k]);
  Object.assign(editRow, row);
}

function cancelEdit() {
  editIndex.value = -1;
}

// Сохраняем редактирование
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

    // Обновляем таблицу
    await fetchData();
    editIndex.value = -1;
  } catch (e) {
    error.value = e.message || "Ошибка при сохранении";
  }
}

// Удаление
async function deleteRow(id) {
  if (!confirm("Удалить запись?")) return;
  error.value = "";
  try {
    const endpoint = `/${selectedTable.value}/${id}`;
    const res = await fetch(baseUrl + endpoint, { method: "DELETE" });
    if (!res.ok) throw new Error(`Ошибка при удалении: ${res.status}`);

    // Обновляем таблицу
    await fetchData();
  } catch (e) {
    error.value = e.message || "Ошибка при удалении";
  }
}

// Добавление новой записи
async function addRow() {
  error.value = "";
  try {
    // Удаляем id из тела, если оно есть и null
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

    // Обновляем таблицу
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
/* Простые стили для таблицы */
table {
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ccc;
  padding: 8px 12px;
}

th {
  background-color: #f0f0f0;
}

input[type="number"] {
  -moz-appearance: textfield;
}

input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>
