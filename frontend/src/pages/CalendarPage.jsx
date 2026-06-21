import { CalendarPlus, XCircle } from 'lucide-react';
import { useState } from 'react';
import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import StatusMessage from '../components/StatusMessage';
import { mockEmployees, mockHorses, mockShifts } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { employeeService } from '../services/employeeService';
import { horseService } from '../services/horseService';
import { reservationService } from '../services/reservationService';

const emptyReservation = {
  tipo: 'PASEO',
  fecha: '',
  horaInicio: '',
  horaFin: '',
  cliente: '',
  caballoId: '',
  empleadoId: '',
  observaciones: ''
};

const fallbackReservations = mockShifts.map((shift) => ({
  ...shift,
  tipo: 'ENTRENAMIENTO',
  estado: 'PROGRAMADA',
  cliente: 'Cliente demo',
  nombreCaballo: 'Caballo demo'
}));

export default function CalendarPage() {
  const reservations = useAsyncData(reservationService.list, fallbackReservations);
  const horses = useAsyncData(horseService.list, mockHorses);
  const employees = useAsyncData(employeeService.list, mockEmployees);
  const [form, setForm] = useState(emptyReservation);
  const [errors, setErrors] = useState({});

  function validate() {
    const nextErrors = {};
    if (!form.tipo) nextErrors.tipo = 'Seleccione tipo';
    if (!form.fecha) nextErrors.fecha = 'La fecha es obligatoria';
    if (!form.horaInicio) nextErrors.horaInicio = 'Indique hora de inicio';
    if (!form.horaFin) nextErrors.horaFin = 'Indique hora de fin';
    if (!form.cliente.trim()) nextErrors.cliente = 'El cliente es obligatorio';
    if (!form.caballoId) nextErrors.caballoId = 'Seleccione caballo';
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!validate()) return;

    const selectedHorse = horses.data.find((horse) => String(horse.id) === String(form.caballoId));
    const selectedEmployee = employees.data.find((employee) => String(employee.id) === String(form.empleadoId));
    const payload = {
      ...form,
      caballoId: Number(form.caballoId),
      empleadoId: form.empleadoId ? Number(form.empleadoId) : null,
      estado: 'PROGRAMADA'
    };

    try {
      const saved = await reservationService.create(payload);
      reservations.setData((current) => [saved, ...current]);
    } catch {
      reservations.setData((current) => [{
        ...payload,
        id: Date.now(),
        nombreCaballo: selectedHorse?.nombre,
        nombreEmpleado: selectedEmployee?.nombre
      }, ...current]);
    }

    setForm(emptyReservation);
  }

  async function cancelReservation(reservation) {
    const updated = { ...reservation, estado: 'CANCELADA' };
    reservations.setData((current) => current.map((item) => (item.id === reservation.id ? updated : item)));

    try {
      await reservationService.cancel(reservation.id);
    } catch {
      // Mantiene la accion visual si el backend no esta disponible durante una demo.
    }
  }

  return (
    <div className="stack">
      <PageHeader title="Calendario y reservas" description="Agenda de citas veterinarias, montas, paseos y entrenamientos." />

      <section className="dashboardGrid">
        <form className="panel" onSubmit={handleSubmit}>
          <div className="panelHeader"><h3>Nueva reserva</h3></div>
          <div className="formGrid single">
            <FormField label="Tipo" error={errors.tipo}>
              <select value={form.tipo} onChange={(event) => setForm({ ...form, tipo: event.target.value })}>
                <option value="CITA_VETERINARIA">Cita veterinaria</option>
                <option value="MONTA">Monta</option>
                <option value="PASEO">Paseo</option>
                <option value="ENTRENAMIENTO">Entrenamiento</option>
              </select>
            </FormField>
            <FormField label="Cliente" error={errors.cliente}>
              <input value={form.cliente} onChange={(event) => setForm({ ...form, cliente: event.target.value })} />
            </FormField>
            <FormField label="Caballo" error={errors.caballoId}>
              <select value={form.caballoId} onChange={(event) => setForm({ ...form, caballoId: event.target.value })}>
                <option value="">Seleccione</option>
                {horses.data.map((horse) => <option key={horse.id} value={horse.id}>{horse.nombre}</option>)}
              </select>
            </FormField>
            <FormField label="Responsable">
              <select value={form.empleadoId} onChange={(event) => setForm({ ...form, empleadoId: event.target.value })}>
                <option value="">Sin asignar</option>
                {employees.data.map((employee) => <option key={employee.id} value={employee.id}>{employee.nombre}</option>)}
              </select>
            </FormField>
            <FormField label="Fecha" error={errors.fecha}>
              <input type="date" value={form.fecha} onChange={(event) => setForm({ ...form, fecha: event.target.value })} />
            </FormField>
            <FormField label="Hora inicio" error={errors.horaInicio}>
              <input type="time" value={form.horaInicio} onChange={(event) => setForm({ ...form, horaInicio: event.target.value })} />
            </FormField>
            <FormField label="Hora fin" error={errors.horaFin}>
              <input type="time" value={form.horaFin} onChange={(event) => setForm({ ...form, horaFin: event.target.value })} />
            </FormField>
            <FormField label="Observaciones">
              <textarea value={form.observaciones} onChange={(event) => setForm({ ...form, observaciones: event.target.value })} />
            </FormField>
          </div>
          <button className="primaryButton" type="submit"><CalendarPlus size={18} />Crear reserva</button>
        </form>

        <StatusMessage loading={reservations.loading} error={reservations.error} empty={!reservations.data.length}>
          <section className="calendarBoard">
            {reservations.data.map((reservation) => (
              <article className="calendarItem" key={reservation.id}>
                <CalendarPlus size={20} />
                <div>
                  <strong>{reservation.tipo?.replaceAll('_', ' ')}</strong>
                  <span>{reservation.fecha} · {reservation.horaInicio} - {reservation.horaFin}</span>
                  <p>{reservation.nombreCaballo || 'Caballo'} · {reservation.cliente}</p>
                  <p>{reservation.nombreEmpleado || 'Sin responsable'} · {reservation.estado}</p>
                  {reservation.estado !== 'CANCELADA' && (
                    <button className="secondaryButton" type="button" onClick={() => cancelReservation(reservation)}>
                      <XCircle size={16} />
                      Cancelar
                    </button>
                  )}
                </div>
              </article>
            ))}
          </section>
        </StatusMessage>
      </section>
    </div>
  );
}
