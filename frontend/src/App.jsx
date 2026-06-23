import { Navigate, Route, Routes } from 'react-router-dom';

import AppLayout from './components/AppLayout.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import AlertsPage from './pages/AlertsPage.jsx';
import CalendarPage from './pages/CalendarPage.jsx';
import Dashboard from './pages/Dashboard.jsx';
import EmployeeForm from './pages/EmployeeForm.jsx';
import EmployeeList from './pages/EmployeeList.jsx';
import FeedingPage from './pages/FeedingPage.jsx';
import HorseDetail from './pages/HorseDetail.jsx';
import HorseForm from './pages/HorseForm.jsx';
import HorseList from './pages/HorseList.jsx';
import InventoryPage from './pages/InventoryPage.jsx';
import LoginPage from './pages/LoginPage.jsx';
import MedicalHistoryPage from './pages/MedicalHistoryPage.jsx';
import TasksPage from './pages/TasksPage.jsx';
import UsersPage from './pages/UsersPage.jsx';

import { authService } from './services/authService.js';
import {
  getHomePath,
  getUserRole
} from './services/roleAccess.js';

const dashboardRoles = [
  'ADMINISTRADOR',
  'VETERINARIO'
];

const horseRoles = [
  'ADMINISTRADOR',
  'VETERINARIO',
  'CUIDADOR',
  'CLIENTE'
];

const medicalRoles = [
  'ADMINISTRADOR',
  'VETERINARIO'
];

const adminRoles = [
  'ADMINISTRADOR'
];

const taskRoles = [
  'ADMINISTRADOR',
  'CUIDADOR'
];

const calendarRoles = [
  'ADMINISTRADOR',
  'CLIENTE'
];

const feedingRoles = [
  'ADMINISTRADOR',
  'CUIDADOR'
];

const alertsRoles = [
  'ADMINISTRADOR',
  'VETERINARIO',
  'CUIDADOR'
];

function HomeRedirect() {
  const role = getUserRole(authService.getUser());

  return (
    <Navigate
      to={getHomePath(role)}
      replace
    />
  );
}

function RolePage({ allowedRoles, children }) {
  return (
    <ProtectedRoute allowedRoles={allowedRoles}>
      {children}
    </ProtectedRoute>
  );
}

export default function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={<LoginPage />}
      />

      <Route
        element={
          <ProtectedRoute>
            <AppLayout />
          </ProtectedRoute>
        }
      >
        <Route
          index
          element={<HomeRedirect />}
        />

        <Route
          path="/dashboard"
          element={
            <RolePage allowedRoles={dashboardRoles}>
              <Dashboard />
            </RolePage>
          }
        />

        <Route
          path="/caballos"
          element={
            <RolePage allowedRoles={horseRoles}>
              <HorseList />
            </RolePage>
          }
        />

        <Route
          path="/caballos/nuevo"
          element={
            <RolePage allowedRoles={horseRoles}>
              <HorseForm />
            </RolePage>
          }
        />

        <Route
          path="/caballos/:id"
          element={
            <RolePage allowedRoles={horseRoles}>
              <HorseDetail />
            </RolePage>
          }
        />

        <Route
          path="/caballos/:id/editar"
          element={
            <RolePage allowedRoles={horseRoles}>
              <HorseForm />
            </RolePage>
          }
        />

        <Route
          path="/historial-medico"
          element={
            <RolePage allowedRoles={medicalRoles}>
              <MedicalHistoryPage />
            </RolePage>
          }
        />

        <Route
          path="/empleados"
          element={
            <RolePage allowedRoles={adminRoles}>
              <EmployeeList />
            </RolePage>
          }
        />

        <Route
          path="/empleados/nuevo"
          element={
            <RolePage allowedRoles={adminRoles}>
              <EmployeeForm />
            </RolePage>
          }
        />

        <Route
          path="/empleados/:id/editar"
          element={
            <RolePage allowedRoles={adminRoles}>
              <EmployeeForm />
            </RolePage>
          }
        />

        <Route
          path="/usuarios"
          element={
            <RolePage allowedRoles={adminRoles}>
              <UsersPage />
            </RolePage>
          }
        />

        <Route
          path="/turnos-tareas"
          element={
            <RolePage allowedRoles={taskRoles}>
              <TasksPage />
            </RolePage>
          }
        />

        <Route
          path="/calendario"
          element={
            <RolePage allowedRoles={calendarRoles}>
              <CalendarPage />
            </RolePage>
          }
        />

        <Route
          path="/alimentacion"
          element={
            <RolePage allowedRoles={feedingRoles}>
              <FeedingPage />
            </RolePage>
          }
        />

        <Route
          path="/inventario"
          element={
            <RolePage allowedRoles={feedingRoles}>
              <InventoryPage />
            </RolePage>
          }
        />

        <Route
          path="/alertas"
          element={
            <RolePage allowedRoles={alertsRoles}>
              <AlertsPage />
            </RolePage>
          }
        />
      </Route>

      <Route
        path="*"
        element={<HomeRedirect />}
      />
    </Routes>
  );
}

