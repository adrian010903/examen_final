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

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        element={
          <ProtectedRoute>
            <AppLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/caballos" element={<HorseList />} />
        <Route path="/caballos/nuevo" element={<HorseForm />} />
        <Route path="/caballos/:id" element={<HorseDetail />} />
        <Route path="/caballos/:id/editar" element={<HorseForm />} />
        <Route path="/historial-medico" element={<MedicalHistoryPage />} />
        <Route path="/empleados" element={<EmployeeList />} />
        <Route path="/empleados/nuevo" element={<EmployeeForm />} />
        <Route path="/empleados/:id/editar" element={<EmployeeForm />} />
        <Route path="/turnos-tareas" element={<TasksPage />} />
        <Route path="/calendario" element={<CalendarPage />} />
        <Route path="/alimentacion" element={<FeedingPage />} />
        <Route path="/inventario" element={<InventoryPage />} />
        <Route path="/alertas" element={<AlertsPage />} />
      </Route>
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}
