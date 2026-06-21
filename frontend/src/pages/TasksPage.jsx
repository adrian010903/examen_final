import { Check, Plus } from 'lucide-react';
import { useMemo, useState } from 'react';
import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import StatusMessage from '../components/StatusMessage';
import { mockEmployees, mockHorses, mockShifts, mockTasks } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { employeeService } from '../services/employeeService';
import { horseService } from '../services/horseService';
import { shiftService } from '../services/shiftService';
import { taskService } from '../services/taskService';

const emptyTask = { descripcion: '', fecha: '', empleadoId: '', caballoId: '', completada: false };
const emptyShift = { fecha: '', horaInicio: '', horaFin: '', empleadoId: '' };

export default function TasksPage() {
  const tasks = useAsyncData(taskService.list, mockTasks);
  const shifts = useAsyncData(shiftService.list, mockShifts);
  const employees = useAsyncData(employeeService.list, mockEmployees);
  const horses = useAsyncData(horseService.list, mockHorses);
  const [form, setForm] = useState(emptyTask);
  const [shiftForm, setShiftForm] = useState(emptyShift);
  const [errors, setErrors] = useState({});
  const [shiftErrors, setShiftErrors] = useState({});

  const pendingCount = useMemo(() => tasks.data.filter((task) => !task.completada).length, [tasks.data]);

  function validate() {
    const nextErrors = {};
    if (!form.descripcion.trim()) nextErrors.descripcion = 'La descripcion es obligatoria';
    if (!form.fecha) nextErrors.fecha = 'La fecha es obligatoria';
    if (!form.empleadoId) nextErrors.empleadoId = 'Seleccione empleado';
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!validate()) return;
    const selectedEmployee = employees.data.find((employee) => String(employee.id) === String(form.empleadoId));
    const selectedHorse = horses.data.find((horse) => String(horse.id) === String(form.caballoId));
    const payload = { ...form, empleadoId: Number(form.empleadoId), caballoId: form.caballoId ? Number(form.caballoId) : null };
    try {
      const saved = await taskService.create(payload);
      tasks.setData((current) => [saved, ...current]);
    } catch {
      tasks.setData((current) => [{ ...payload, id: Date.now(), nombreEmpleado: selectedEmployee?.nombre, nombreCaballo: selectedHorse?.nombre }, ...current]);
    }
    setForm(emptyTask);
  }

  function validateShift() {
    const nextErrors = {};
    if (!shiftForm.fecha) nextErrors.fecha = 'La fecha es obligatoria';
    if (!shiftForm.horaInicio) nextErrors.horaInicio = 'Indique hora de inicio';
    if (!shiftForm.horaFin) nextErrors.horaFin = 'Indique hora de fin';
    if (!shiftForm.empleadoId) nextErrors.empleadoId = 'Seleccione empleado';
    setShiftErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function handleShiftSubmit(event) {
    event.preventDefault();
    if (!validateShift()) return;

    const selectedEmployee = employees.data.find((employee) => String(employee.id) === String(shiftForm.empleadoId));
    const payload = { ...shiftForm, empleadoId: Number(shiftForm.empleadoId) };

    try {
      const saved = await shiftService.create(payload);
      shifts.setData((current) => [saved, ...current]);
    } catch {
      shifts.setData((current) => [{ ...payload, id: Date.now(), nombreEmpleado: selectedEmployee?.nombre }, ...current]);
    }

    setShiftForm(emptyShift);
  }

  async function toggleTaskStatus(task) {
    const updatedTask = { ...task, completada: !task.completada };

    tasks.setData((current) => current.map((item) => (item.id === task.id ? updatedTask : item)));

    try {
      await taskService.update(task.id, {
        descripcion: updatedTask.descripcion,
        fecha: updatedTask.fecha,
        completada: updatedTask.completada,
        empleadoId: updatedTask.empleadoId,
        caballoId: updatedTask.caballoId || null
      });
    } catch {
      // Si el backend no responde, se conserva el cambio visual para no bloquear la demostracion.
    }
  }

  return (
    <div className="stack">
      <PageHeader title="Turnos y tareas" description={`${pendingCount} tareas pendientes en la operacion actual.`} />
      <section className="dashboardGrid">
        <form className="panel" onSubmit={handleSubmit}>
          <div className="panelHeader"><h3>Nueva tarea</h3></div>
          <div className="formGrid single">
            <FormField label="Descripcion" error={errors.descripcion}><input value={form.descripcion} onChange={(event) => setForm({ ...form, descripcion: event.target.value })} /></FormField>
            <FormField label="Fecha" error={errors.fecha}><input type="date" value={form.fecha} onChange={(event) => setForm({ ...form, fecha: event.target.value })} /></FormField>
            <FormField label="Empleado" error={errors.empleadoId}>
              <select value={form.empleadoId} onChange={(event) => setForm({ ...form, empleadoId: event.target.value })}>
                <option value="">Seleccione</option>
                {employees.data.map((employee) => <option key={employee.id} value={employee.id}>{employee.nombre}</option>)}
              </select>
            </FormField>
            <FormField label="Caballo">
              <select value={form.caballoId} onChange={(event) => setForm({ ...form, caballoId: event.target.value })}>
                <option value="">Opcional</option>
                {horses.data.map((horse) => <option key={horse.id} value={horse.id}>{horse.nombre}</option>)}
              </select>
            </FormField>
          </div>
          <button className="primaryButton" type="submit"><Plus size={18} />Agregar</button>
        </form>
        <form className="panel" onSubmit={handleShiftSubmit}>
          <div className="panelHeader"><h3>Nuevo turno</h3></div>
          <div className="formGrid single">
            <FormField label="Empleado" error={shiftErrors.empleadoId}>
              <select value={shiftForm.empleadoId} onChange={(event) => setShiftForm({ ...shiftForm, empleadoId: event.target.value })}>
                <option value="">Seleccione</option>
                {employees.data.map((employee) => <option key={employee.id} value={employee.id}>{employee.nombre}</option>)}
              </select>
            </FormField>
            <FormField label="Fecha" error={shiftErrors.fecha}>
              <input type="date" value={shiftForm.fecha} onChange={(event) => setShiftForm({ ...shiftForm, fecha: event.target.value })} />
            </FormField>
            <FormField label="Hora inicio" error={shiftErrors.horaInicio}>
              <input type="time" value={shiftForm.horaInicio} onChange={(event) => setShiftForm({ ...shiftForm, horaInicio: event.target.value })} />
            </FormField>
            <FormField label="Hora fin" error={shiftErrors.horaFin}>
              <input type="time" value={shiftForm.horaFin} onChange={(event) => setShiftForm({ ...shiftForm, horaFin: event.target.value })} />
            </FormField>
          </div>
          <button className="primaryButton" type="submit"><Plus size={18} />Crear turno</button>
        </form>
      </section>

      <section className="panel">
        <div className="panelHeader"><h3>Turnos asignados</h3></div>
        <div className="calendarBoard compact">
          {shifts.data.map((shift) => (
            <div className="calendarItem" key={shift.id}>
              <div>
                <strong>{shift.nombreEmpleado}</strong>
                <span>{shift.fecha}</span>
                <p>{shift.horaInicio} - {shift.horaFin}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      <StatusMessage loading={tasks.loading} error={tasks.error} empty={!tasks.data.length}>
        <div className="tableWrap">
          <table>
            <thead><tr><th>Tarea</th><th>Fecha</th><th>Responsable</th><th>Caballo</th><th>Estado</th><th>Accion</th></tr></thead>
            <tbody>
              {tasks.data.map((task) => (
                <tr key={task.id}>
                  <td>{task.descripcion}</td><td>{task.fecha}</td><td>{task.nombreEmpleado}</td><td>{task.nombreCaballo || 'General'}</td>
                  <td><span className={`tag ${task.completada ? 'tagSuccess' : 'tagPending'}`}>{task.completada ? 'Completada' : 'Pendiente'}</span></td>
                  <td>
                    <button className={task.completada ? 'secondaryButton' : 'primaryButton'} type="button" onClick={() => toggleTaskStatus(task)}>
                      <Check size={17} />
                      {task.completada ? 'Reabrir' : 'Completar'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </StatusMessage>
    </div>
  );
}
