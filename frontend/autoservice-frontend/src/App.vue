<template>
  <div id="app">
    <nav class="main-nav">
      <div class="nav-container">
        <div class="logo">
          <router-link to="/">
            <span class="logo-icon">🚗</span>
            <span class="logo-text">AutoService</span>
          </router-link>
        </div>
        <div class="nav-links">
          <router-link to="/" class="nav-link">
            <span class="nav-icon">🏠</span>
            Главная
          </router-link>
          <router-link to="/database-queries" class="nav-link">
            <span class="nav-icon">🔍</span>
            Запросы к БД
          </router-link>
        </div>
      </div>
    </nav>
    <main class="main-content">
      <router-view />
    </main>
    <footer class="main-footer">
      <p>&copy; 2024 AutoService. Все права защищены.</p>
    </footer>
  </div>
</template>

<style>
/* CSS Variables for Design System */
:root {
  --primary-color: #3b82f6;
  --primary-hover: #2563eb;
  --primary-dark: #1e40af;
  --secondary-color: #10b981;
  --secondary-hover: #059669;
  --danger-color: #ef4444;
  --danger-hover: #dc2626;
  --warning-color: #f59e0b;
  --text-primary: #1f2937;
  --text-secondary: #6b7280;
  --text-light: #9ca3af;
  --bg-primary: #ffffff;
  --bg-secondary: #f9fafb;
  --bg-tertiary: #f3f4f6;
  --border-color: #e5e7eb;
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1),
    0 2px 4px -1px rgba(0, 0, 0, 0.06);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1),
    0 4px 6px -2px rgba(0, 0, 0, 0.05);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);
  --radius-sm: 0.25rem;
  --radius-md: 0.375rem;
  --radius-lg: 0.5rem;
  --radius-xl: 0.75rem;
  --transition-fast: 150ms ease-in-out;
  --transition-normal: 250ms ease-in-out;
  --transition-slow: 350ms ease-in-out;
}

/* Global Resets and Base Styles */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: "Inter", "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: var(--text-primary);
  background-color: var(--bg-secondary);
  line-height: 1.6;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* Navigation Styles */
.main-nav {
  background: linear-gradient(
    135deg,
    var(--primary-color) 0%,
    var(--primary-dark) 100%
  );
  box-shadow: var(--shadow-md);
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: box-shadow var(--transition-normal);
}

.main-nav:hover {
  box-shadow: var(--shadow-lg);
}

.nav-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
}

.logo a {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: white;
  font-weight: 700;
  font-size: 1.5rem;
  transition: transform var(--transition-fast);
}

.logo a:hover {
  transform: scale(1.05);
}

.logo-icon {
  font-size: 2rem;
  margin-right: 0.5rem;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

.logo-text {
  background: linear-gradient(to right, #ffffff, #e0e7ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-links {
  display: flex;
  gap: 0.5rem;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.25rem;
  text-decoration: none;
  color: rgba(255, 255, 255, 0.9);
  font-weight: 500;
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
  position: relative;
  overflow: hidden;
}

.nav-link::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.1);
  transition: left var(--transition-normal);
}

.nav-link:hover::before {
  left: 0;
}

.nav-link:hover {
  color: white;
  transform: translateY(-2px);
}

.nav-link.router-link-active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  box-shadow: var(--shadow-sm);
}

.nav-icon {
  font-size: 1.25rem;
}

/* Main Content */
.main-content {
  flex: 1;
  max-width: 1280px;
  width: 100%;
  margin: 2rem auto;
  padding: 0 2rem;
  animation: fadeIn var(--transition-slow);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Footer */
.main-footer {
  background: var(--text-primary);
  color: var(--text-light);
  text-align: center;
  padding: 2rem;
  margin-top: 4rem;
}

.main-footer p {
  font-size: 0.875rem;
}

/* Utility Classes */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: var(--radius-lg);
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all var(--transition-fast);
  text-decoration: none;
  box-shadow: var(--shadow-sm);
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.btn:active {
  transform: translateY(0);
}

.btn-primary {
  background: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-hover);
}

.btn-secondary {
  background: var(--secondary-color);
  color: white;
}

.btn-secondary:hover {
  background: var(--secondary-hover);
}

.btn-danger {
  background: var(--danger-color);
  color: white;
}

.btn-danger:hover {
  background: var(--danger-hover);
}

.card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: 1.5rem;
  box-shadow: var(--shadow-md);
  transition: all var(--transition-normal);
}

.card:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-4px);
}

.input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  transition: all var(--transition-fast);
  background: var(--bg-primary);
}

.input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.input:hover {
  border-color: var(--primary-color);
}

/* Responsive Design */
@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    height: auto;
    padding: 1rem;
    gap: 1rem;
  }

  .nav-links {
    flex-direction: column;
    width: 100%;
  }

  .nav-link {
    justify-content: center;
    width: 100%;
  }

  .main-content {
    padding: 0 1rem;
    margin: 1rem auto;
  }

  .logo-text {
    font-size: 1.25rem;
  }
}

@media (max-width: 480px) {
  .nav-link {
    padding: 0.5rem 1rem;
    font-size: 0.875rem;
  }

  .nav-icon {
    font-size: 1rem;
  }

  .btn {
    padding: 0.5rem 1rem;
    font-size: 0.75rem;
  }
}
</style>
