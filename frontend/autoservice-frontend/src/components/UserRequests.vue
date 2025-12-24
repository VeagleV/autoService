<template>
  <div class="user-requests-page">
    <div class="page-header">
      <h1 class="page-title">📝 Пользовательские запросы</h1>
      <p class="page-subtitle">Управление запросами пользователей в системе</p>
    </div>

    <!-- Filters and Actions -->
    <div class="controls-bar card">
      <div class="filters">
        <div class="filter-group">
          <label class="filter-label">Пользователь:</label>
          <input
            v-model="filterUserId"
            type="number"
            placeholder="ID пользователя"
            class="input filter-input"
          />
        </div>
        <div class="filter-group">
          <label class="filter-label">Статус:</label>
          <select v-model="filterStatus" class="input filter-input">
            <option value="">Все статусы</option>
            <option value="NEW">Новый</option>
            <option value="IN_PROGRESS">В обработке</option>
            <option value="COMPLETED">Выполнен</option>
            <option value="REJECTED">Отклонен</option>
          </select>
        </div>
        <button @click="applyFilters" class="btn btn-primary">
          🔍 Применить фильтры
        </button>
        <button @click="clearFilters" class="btn btn-outline">
          ✕ Сбросить
        </button>
      </div>
      <button @click="showCreateForm = true" class="btn btn-secondary">
        ➕ Создать запрос
      </button>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Загрузка запросов...</p>
    </div>

    <!-- Error Message -->
    <div v-if="error" class="error-message card">
      <span class="error-icon">⚠️</span>
      <span>{{ error }}</span>
    </div>

    <!-- Requests List -->
    <div v-if="!loading && requests.length > 0" class="requests-grid">
      <div
        v-for="request in requests"
        :key="request.requestId"
        class="request-card card"
        @click="viewRequest(request)"
      >
        <div class="request-header">
          <h3 class="request-title">{{ request.title }}</h3>
          <span
            :class="[
              'status-badge',
              `status-${request.status ? request.status.toLowerCase() : 'new'}`,
            ]"
          >
            {{ getStatusLabel(request.status) }}
          </span>
        </div>
        <p class="request-description">
          {{ truncateText(request.description, 120) }}
        </p>
        <div class="request-meta">
          <span class="meta-item">
            <span class="meta-icon">👤</span>
            ID: {{ request.userId }}
          </span>
          <span class="meta-item">
            <span class="meta-icon">📅</span>
            {{ formatDate(request.createdAt) }}
          </span>
          <span v-if="request.priority" class="meta-item">
            <span class="meta-icon">⭐</span>
            {{ request.priority }}
          </span>
          <span v-if="request.category" class="meta-item">
            <span class="meta-icon">🏷️</span>
            {{ request.category }}
          </span>
        </div>
        <div class="request-actions">
          <button
            @click.stop="editRequest(request)"
            class="btn btn-sm btn-primary"
          >
            ✏️ Редактировать
          </button>
          <button
            @click.stop="deleteRequest(request.requestId)"
            class="btn btn-sm btn-danger"
          >
            🗑️ Удалить
          </button>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div
      v-if="!loading && requests.length === 0 && !error"
      class="empty-state card"
    >
      <span class="empty-icon">📭</span>
      <h3>Запросы не найдены</h3>
      <p>Нет запросов, соответствующих вашим критериям фильтрации.</p>
      <button @click="showCreateForm = true" class="btn btn-secondary">
        Создать первый запрос
      </button>
    </div>

    <!-- Create/Edit Form Modal -->
    <div
      v-if="showCreateForm || showEditForm"
      class="modal-overlay"
      @click.self="closeModal"
    >
      <div class="modal-content card">
        <div class="modal-header">
          <h2 class="modal-title">
            {{
              showEditForm
                ? "✏️ Редактировать запрос"
                : "➕ Создать новый запрос"
            }}
          </h2>
          <button @click="closeModal" class="close-btn">✕</button>
        </div>
        <form @submit.prevent="submitForm" class="request-form">
          <div class="form-group">
            <label class="form-label">ID пользователя *</label>
            <input
              v-model.number="formData.userId"
              type="number"
              class="input"
              required
              placeholder="Введите ID пользователя"
            />
          </div>
          <div class="form-group">
            <label class="form-label">Заголовок *</label>
            <input
              v-model="formData.title"
              type="text"
              class="input"
              required
              maxlength="200"
              placeholder="Краткое описание проблемы"
            />
          </div>
          <div class="form-group">
            <label class="form-label">Описание *</label>
            <textarea
              v-model="formData.description"
              class="input textarea"
              required
              rows="5"
              placeholder="Подробное описание запроса"
            ></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Статус</label>
              <select v-model="formData.status" class="input">
                <option value="NEW">Новый</option>
                <option value="IN_PROGRESS">В обработке</option>
                <option value="COMPLETED">Выполнен</option>
                <option value="REJECTED">Отклонен</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Приоритет</label>
              <input
                v-model="formData.priority"
                type="text"
                class="input"
                placeholder="Низкий, Средний, Высокий"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">Категория</label>
            <input
              v-model="formData.category"
              type="text"
              class="input"
              placeholder="Категория запроса"
            />
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-secondary">
              {{ showEditForm ? "Сохранить изменения" : "Создать запрос" }}
            </button>
            <button type="button" @click="closeModal" class="btn btn-outline">
              Отмена
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- View Request Modal -->
    <div
      v-if="showViewModal"
      class="modal-overlay"
      @click.self="showViewModal = false"
    >
      <div class="modal-content card view-modal">
        <div class="modal-header">
          <h2 class="modal-title">{{ viewingRequest.title }}</h2>
          <button @click="showViewModal = false" class="close-btn">✕</button>
        </div>
        <div class="request-details">
          <div class="detail-row">
            <span class="detail-label">Статус:</span>
            <span
              :class="[
                'status-badge',
                `status-${
                  viewingRequest.status
                    ? viewingRequest.status.toLowerCase()
                    : 'new'
                }`,
              ]"
            >
              {{ getStatusLabel(viewingRequest.status) }}
            </span>
          </div>
          <div class="detail-row">
            <span class="detail-label">ID пользователя:</span>
            <span>{{ viewingRequest.userId }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Описание:</span>
            <p class="detail-text">{{ viewingRequest.description }}</p>
          </div>
          <div class="detail-row">
            <span class="detail-label">Дата создания:</span>
            <span>{{ formatDate(viewingRequest.createdAt) }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Дата обновления:</span>
            <span>{{ formatDate(viewingRequest.updatedAt) }}</span>
          </div>
          <div v-if="viewingRequest.priority" class="detail-row">
            <span class="detail-label">Приоритет:</span>
            <span>{{ viewingRequest.priority }}</span>
          </div>
          <div v-if="viewingRequest.category" class="detail-row">
            <span class="detail-label">Категория:</span>
            <span>{{ viewingRequest.category }}</span>
          </div>
        </div>
        <div class="modal-actions">
          <button @click="editRequest(viewingRequest)" class="btn btn-primary">
            ✏️ Редактировать
          </button>
          <button
            @click="deleteRequest(viewingRequest.requestId)"
            class="btn btn-danger"
          >
            🗑️ Удалить
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import requestService from "../services/requestService";

const requests = ref([]);
const loading = ref(false);
const error = ref("");

const filterUserId = ref("");
const filterStatus = ref("");

const showCreateForm = ref(false);
const showEditForm = ref(false);
const showViewModal = ref(false);
const viewingRequest = ref({});

const formData = reactive({
  userId: null,
  title: "",
  description: "",
  status: "NEW",
  priority: "",
  category: "",
});

const editingRequestId = ref(null);

onMounted(() => {
  loadRequests();
});

async function loadRequests() {
  loading.value = true;
  error.value = "";
  try {
    const response = await requestService.getAll();
    requests.value = response.data;
  } catch (err) {
    error.value =
      "Ошибка при загрузке запросов: " + (err.message || "Неизвестная ошибка");
  } finally {
    loading.value = false;
  }
}

async function applyFilters() {
  loading.value = true;
  error.value = "";
  try {
    const params = {};
    if (filterUserId.value) params.userId = filterUserId.value;
    if (filterStatus.value) params.status = filterStatus.value;

    const response = await requestService.getAll(params);
    requests.value = response.data;
  } catch (err) {
    error.value =
      "Ошибка при применении фильтров: " +
      (err.message || "Неизвестная ошибка");
  } finally {
    loading.value = false;
  }
}

function clearFilters() {
  filterUserId.value = "";
  filterStatus.value = "";
  loadRequests();
}

function viewRequest(request) {
  viewingRequest.value = request;
  showViewModal.value = true;
}

function editRequest(request) {
  showViewModal.value = false;
  formData.userId = request.userId;
  formData.title = request.title;
  formData.description = request.description;
  formData.status = request.status;
  formData.priority = request.priority || "";
  formData.category = request.category || "";
  editingRequestId.value = request.requestId;
  showEditForm.value = true;
}

async function deleteRequest(id) {
  if (!confirm("Вы уверены, что хотите удалить этот запрос?")) return;

  try {
    await requestService.delete(id);
    showViewModal.value = false;
    loadRequests();
  } catch (err) {
    error.value =
      "Ошибка при удалении запроса: " + (err.message || "Неизвестная ошибка");
  }
}

async function submitForm() {
  try {
    if (showEditForm.value) {
      await requestService.update(editingRequestId.value, formData);
    } else {
      await requestService.create(formData);
    }
    closeModal();
    loadRequests();
  } catch (err) {
    error.value =
      "Ошибка при сохранении запроса: " + (err.message || "Неизвестная ошибка");
  }
}

function closeModal() {
  showCreateForm.value = false;
  showEditForm.value = false;
  editingRequestId.value = null;
  resetForm();
}

function resetForm() {
  formData.userId = null;
  formData.title = "";
  formData.description = "";
  formData.status = "NEW";
  formData.priority = "";
  formData.category = "";
}

function getStatusLabel(status) {
  const labels = {
    NEW: "Новый",
    IN_PROGRESS: "В обработке",
    COMPLETED: "Выполнен",
    REJECTED: "Отклонен",
  };
  return labels[status] || labels.NEW || "Неизвестно";
}

function formatDate(dateString) {
  if (!dateString) return "-";
  const date = new Date(dateString);
  return date.toLocaleString("ru-RU", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}

function truncateText(text, maxLength) {
  if (!text) return "";
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + "...";
}
</script>

<style scoped>
.user-requests-page {
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

.controls-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.filters {
  display: flex;
  gap: 1rem;
  align-items: flex-end;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-label {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-primary);
}

.filter-input {
  width: 200px;
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

.requests-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.request-card {
  cursor: pointer;
  padding: 1.5rem;
  transition: all var(--transition-normal);
  border: 2px solid transparent;
}

.request-card:hover {
  border-color: var(--primary-color);
}

.request-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
  margin-bottom: 1rem;
}

.request-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  flex: 1;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-md);
  font-size: 0.75rem;
  font-weight: 600;
  white-space: nowrap;
}

.status-new {
  background: #dbeafe;
  color: #1e40af;
}

.status-in_progress {
  background: #fef3c7;
  color: #92400e;
}

.status-completed {
  background: #d1fae5;
  color: #065f46;
}

.status-rejected {
  background: #fee2e2;
  color: #991b1b;
}

.request-description {
  color: var(--text-secondary);
  margin-bottom: 1rem;
  line-height: 1.5;
}

.request-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.meta-icon {
  font-size: 1rem;
}

.request-actions {
  display: flex;
  gap: 0.5rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color);
}

.btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.75rem;
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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  padding: 1rem;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal-content {
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid var(--border-color);
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
}

.close-btn {
  width: 2rem;
  height: 2rem;
  border: none;
  background: var(--bg-tertiary);
  border-radius: 50%;
  font-size: 1.25rem;
  cursor: pointer;
  transition: all var(--transition-fast);
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  background: var(--danger-color);
  color: white;
  transform: rotate(90deg);
}

.request-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
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

.textarea {
  resize: vertical;
  min-height: 100px;
  font-family: inherit;
}

.form-actions {
  display: flex;
  gap: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color);
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

.view-modal {
  max-width: 700px;
}

.request-details {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.detail-row {
  display: flex;
  gap: 1rem;
  align-items: flex-start;
}

.detail-label {
  font-weight: 600;
  color: var(--text-primary);
  min-width: 150px;
}

.detail-text {
  flex: 1;
  color: var(--text-secondary);
  line-height: 1.6;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color);
}

@media (max-width: 768px) {
  .page-title {
    font-size: 1.75rem;
  }

  .controls-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .filters {
    flex-direction: column;
  }

  .filter-input {
    width: 100%;
  }

  .requests-grid {
    grid-template-columns: 1fr;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .modal-content {
    max-height: 95vh;
  }
}
</style>
