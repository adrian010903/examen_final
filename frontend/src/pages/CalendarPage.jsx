import {
  CalendarPlus,
  Pencil,
  Save,
  Users,
  X,
  XCircle
} from 'lucide-react';
import { useEffect, useMemo, useState } from 'react';

import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import { mockEmployees, mockHorses } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { employeeService } from '../services/employeeService';
import { horseService } from '../services/horseService';
import { reservationService } from '../services/reservationService';

const emptyReservation = {
  tipo: 'PASEO',
  estado: 'PROGRAMADA',
  fecha: '',
  horaInicio: '',
  horaFin: '',
  cliente: '',
  caballoId: '',
  empleadoId: '',
  observaciones: '',
  cantidadPersonas: 1,
  cupoMaximo: 10
};

const emptyFilters = {
  tipo: '',
  fecha: '',
  estado: ''
};

export default function CalendarPage() {
  const horses = useAsyncData(horseService.list, mockHorses);
  const employees = useAsyncData(employeeService.list, mockEmployees);

  const [reservations, setReservations] = useState([]);
  const [form, setForm] = useState({ ...emptyReservation });
  const [filters, setFilters] = useState({ ...emptyFilters });
  const [errors, setErrors] = useState({});
  const [editingId, setEditingId] = useState(null);

  const [loading, setLoading] = useState(true);
  const [requestError, setRequestError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    loadReservations();
  }, []);

  async function loadReservations() {
    setLoading(true);
    setRequestError('');

    try {
      const data = await reservationService.list();
      setReservations(Array.isArray(data) ? data : []);
    } catch (error) {
      setRequestError(
        getErrorMessage(error, 'No se pudieron cargar las reservas')
      );
    } finally {
      setLoading(false);
    }
  }

  function validate() {
    const nextErrors = {};

    if (!form.tipo) {
      nextErrors.tipo = 'Seleccione el tipo';
    }

    if (!form.cliente.trim()) {
      nextErrors.cliente = 'El cliente es obligatorio';
    }

    if (!form.caballoId) {
      nextErrors.caballoId = 'Seleccione un caballo';
    }

    if (!form.fecha) {
      nextErrors.fecha = 'La fecha es obligatoria';
    }

    if (!form.horaInicio) {
      nextErrors.horaInicio = 'Indique la hora de inicio';
    }

    if (!form.horaFin) {
      nextErrors.horaFin = 'Indique la hora final';
    }

    if (
      form.horaInicio &&
      form.horaFin &&
      form.horaFin <= form.horaInicio
    ) {
      nextErrors.horaFin =
        'La hora final debe ser posterior a la hora inicial';
    }

    if (form.tipo === 'PASEO') {
      if (Number(form.cantidadPersonas) < 1) {
        nextErrors.cantidadPersonas =
          'La cantidad de personas debe ser mayor a cero';
      }

      if (Number(form.cupoMaximo) < 1) {
        nextErrors.cupoMaximo =
          'El cupo máximo debe ser mayor a cero';
      }
    }

    setErrors(nextErrors);

    return Object.keys(nextErrors).length === 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();

    if (!validate()) {
      return;
    }

    setRequestError('');
    setSuccessMessage('');

    const payload = {
      tipo: form.tipo,
      estado: form.estado || 'PROGRAMADA',
      fecha: form.fecha,
      horaInicio: form.horaInicio,
      horaFin: form.horaFin,
      cliente: form.cliente.trim(),
      observaciones: form.observaciones,
      caballoId: Number(form.caballoId),
      empleadoId: form.empleadoId
        ? Number(form.empleadoId)
        : null,

      cantidadPersonas:
        form.tipo === 'PASEO'
          ? Number(form.cantidadPersonas)
          : 1,

      cupoMaximo:
        form.tipo === 'PASEO'
          ? Number(form.cupoMaximo)
          : null
    };

    try {
      if (editingId !== null) {
        await reservationService.update(editingId, payload);
        setSuccessMessage('Reserva actualizada correctamente');
      } else {
        await reservationService.create(payload);
        setSuccessMessage('Reserva creada correctamente');
      }

      resetForm();
      await loadReservations();
    } catch (error) {
      setRequestError(
        getErrorMessage(error, 'No se pudo guardar la reserva')
      );
    }
  }

  function startEditing(reservation) {
    setEditingId(reservation.id);
    setErrors({});
    setRequestError('');
    setSuccessMessage('');

    setForm({
      tipo: reservation.tipo || 'PASEO',
      estado: reservation.estado || 'PROGRAMADA',
      fecha: reservation.fecha || '',
      horaInicio: normalizeTime(reservation.horaInicio),
      horaFin: normalizeTime(reservation.horaFin),
      cliente: reservation.cliente || '',
      caballoId: reservation.caballoId
        ? String(reservation.caballoId)
        : '',
      empleadoId: reservation.empleadoId
        ? String(reservation.empleadoId)
        : '',
      observaciones: reservation.observaciones || '',
      cantidadPersonas: reservation.cantidadPersonas || 1,
      cupoMaximo: reservation.cupoMaximo || 10
    });

    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  function resetForm() {
    setEditingId(null);
    setForm({ ...emptyReservation });
    setErrors({});
  }

  async function cancelReservation(reservation) {
    const confirmed = window.confirm(
      `¿Desea cancelar la reserva de ${reservation.cliente}?`
    );

    if (!confirmed) {
      return;
    }

    setRequestError('');
    setSuccessMessage('');

    try {
      await reservationService.cancel(reservation.id);
      setSuccessMessage('Reserva cancelada correctamente');
      await loadReservations();
    } catch (error) {
      setRequestError(
        getErrorMessage(error, 'No se pudo cancelar la reserva')
      );
    }
  }

  const filteredReservations = useMemo(() => {
    return reservations.filter((reservation) => {
      const matchesType =
        !filters.tipo || reservation.tipo === filters.tipo;

      const matchesDate =
        !filters.fecha || reservation.fecha === filters.fecha;

      const matchesStatus =
        !filters.estado || reservation.estado === filters.estado;

      return matchesType && matchesDate && matchesStatus;
    });
  }, [reservations, filters]);

  const capacityBySchedule = useMemo(() => {
    const result = {};

    reservations
      .filter(
        (reservation) =>
          reservation.tipo === 'PASEO' &&
          reservation.estado !== 'CANCELADA'
      )
      .forEach((reservation) => {
        const key = getScheduleKey(reservation);

        if (!result[key]) {
          result[key] = {
            occupied: 0,
            maximum: reservation.cupoMaximo || 0
          };
        }

        result[key].occupied += reservation.cantidadPersonas || 1;

        if (!result[key].maximum && reservation.cupoMaximo) {
          result[key].maximum = reservation.cupoMaximo;
        }
      });

    return result;
  }, [reservations]);

  function getCapacity(reservation) {
    if (reservation.tipo !== 'PASEO') {
      return null;
    }

    const capacity =
      capacityBySchedule[getScheduleKey(reservation)];

    if (!capacity) {
      return null;
    }

    return {
      occupied: capacity.occupied,
      maximum: capacity.maximum,
      available: Math.max(
        capacity.maximum - capacity.occupied,
        0
      )
    };
  }

  function clearFilters() {
    setFilters({ ...emptyFilters });
  }

  return (
    <div className="stack">
      <PageHeader
        title="Calendario y reservas"
        description="Agenda de citas veterinarias, montas, paseos y entrenamientos."
      />

      <section className="panel">
        <div className="reservationSectionHeader">
          <div>
            <h3>Filtros de reservas</h3>
            <p>Busque por tipo, fecha o estado.</p>
          </div>

          <button
            className="secondaryButton"
            type="button"
            onClick={clearFilters}
          >
            <X size={16} />
            Limpiar filtros
          </button>
        </div>

        <div className="reservationFilters">
          <FormField label="Tipo">
            <select
              value={filters.tipo}
              onChange={(event) =>
                setFilters({
                  ...filters,
                  tipo: event.target.value
                })
              }
            >
              <option value="">Todos</option>
              <option value="CITA_VETERINARIA">
                Cita veterinaria
              </option>
              <option value="MONTA">Monta</option>
              <option value="PASEO">Paseo</option>
              <option value="ENTRENAMIENTO">
                Entrenamiento
              </option>
            </select>
          </FormField>

          <FormField label="Fecha">
            <input
              type="date"
              value={filters.fecha}
              onChange={(event) =>
                setFilters({
                  ...filters,
                  fecha: event.target.value
                })
              }
            />
          </FormField>

          <FormField label="Estado">
            <select
              value={filters.estado}
              onChange={(event) =>
                setFilters({
                  ...filters,
                  estado: event.target.value
                })
              }
            >
              <option value="">Todos</option>
              <option value="PROGRAMADA">Programada</option>
              <option value="CANCELADA">Cancelada</option>
              <option value="COMPLETADA">Completada</option>
            </select>
          </FormField>
        </div>
      </section>

      {requestError && (
        <div className="reservationMessage reservationError">
          {requestError}
        </div>
      )}

      {successMessage && (
        <div className="reservationMessage reservationSuccess">
          {successMessage}
        </div>
      )}

      <section className="dashboardGrid">
        <form className="panel" onSubmit={handleSubmit}>
          <div className="reservationSectionHeader">
            <h3>
              {editingId !== null
                ? 'Editar reserva'
                : 'Nueva reserva'}
            </h3>

            {editingId !== null && (
              <button
                className="secondaryButton"
                type="button"
                onClick={resetForm}
              >
                <X size={16} />
                Cancelar edición
              </button>
            )}
          </div>

          <div className="formGrid single">
            <FormField label="Tipo" error={errors.tipo}>
              <select
                value={form.tipo}
                onChange={(event) =>
                  setForm({
                    ...form,
                    tipo: event.target.value
                  })
                }
              >
                <option value="CITA_VETERINARIA">
                  Cita veterinaria
                </option>
                <option value="MONTA">Monta</option>
                <option value="PASEO">Paseo</option>
                <option value="ENTRENAMIENTO">
                  Entrenamiento
                </option>
              </select>
            </FormField>

            <FormField label="Cliente" error={errors.cliente}>
              <input
                value={form.cliente}
                onChange={(event) =>
                  setForm({
                    ...form,
                    cliente: event.target.value
                  })
                }
              />
            </FormField>

            <FormField label="Caballo" error={errors.caballoId}>
              <select
                value={form.caballoId}
                onChange={(event) =>
                  setForm({
                    ...form,
                    caballoId: event.target.value
                  })
                }
              >
                <option value="">Seleccione</option>

                {horses.data.map((horse) => (
                  <option key={horse.id} value={horse.id}>
                    {horse.nombre}
                  </option>
                ))}
              </select>
            </FormField>

            <FormField label="Responsable">
              <select
                value={form.empleadoId}
                onChange={(event) =>
                  setForm({
                    ...form,
                    empleadoId: event.target.value
                  })
                }
              >
                <option value="">Sin asignar</option>

                {employees.data.map((employee) => (
                  <option key={employee.id} value={employee.id}>
                    {employee.nombre}
                  </option>
                ))}
              </select>
            </FormField>

            <FormField label="Fecha" error={errors.fecha}>
              <input
                type="date"
                value={form.fecha}
                onChange={(event) =>
                  setForm({
                    ...form,
                    fecha: event.target.value
                  })
                }
              />
            </FormField>

            <FormField
              label="Hora inicio"
              error={errors.horaInicio}
            >
              <input
                type="time"
                value={form.horaInicio}
                onChange={(event) =>
                  setForm({
                    ...form,
                    horaInicio: event.target.value
                  })
                }
              />
            </FormField>

            <FormField label="Hora fin" error={errors.horaFin}>
              <input
                type="time"
                value={form.horaFin}
                onChange={(event) =>
                  setForm({
                    ...form,
                    horaFin: event.target.value
                  })
                }
              />
            </FormField>

            {form.tipo === 'PASEO' && (
              <>
                <FormField
                  label="Cantidad de personas"
                  error={errors.cantidadPersonas}
                >
                  <input
                    type="number"
                    min="1"
                    value={form.cantidadPersonas}
                    onChange={(event) =>
                      setForm({
                        ...form,
                        cantidadPersonas: event.target.value
                      })
                    }
                  />
                </FormField>

                <FormField
                  label="Cupo máximo"
                  error={errors.cupoMaximo}
                >
                  <input
                    type="number"
                    min="1"
                    value={form.cupoMaximo}
                    onChange={(event) =>
                      setForm({
                        ...form,
                        cupoMaximo: event.target.value
                      })
                    }
                  />
                </FormField>
              </>
            )}

            <FormField label="Observaciones">
              <textarea
                value={form.observaciones}
                onChange={(event) =>
                  setForm({
                    ...form,
                    observaciones: event.target.value
                  })
                }
              />
            </FormField>
          </div>

          <button className="primaryButton" type="submit">
            {editingId !== null ? (
              <>
                <Save size={18} />
                Guardar cambios
              </>
            ) : (
              <>
                <CalendarPlus size={18} />
                Crear reserva
              </>
            )}
          </button>
        </form>

        <section className="calendarBoard">
          {loading && <p>Cargando reservas...</p>}

          {!loading && filteredReservations.length === 0 && (
            <p>No hay reservas que coincidan con los filtros.</p>
          )}

          {!loading &&
            filteredReservations.map((reservation) => {
              const capacity = getCapacity(reservation);

              return (
                <article
                  className="calendarItem"
                  key={reservation.id}
                >
                  <CalendarPlus size={20} />

                  <div className="calendarItemContent">
                    <strong>
                      {reservation.tipo?.replaceAll('_', ' ')}
                    </strong>

                    <span>
                      {reservation.fecha}
                      {' · '}
                      {normalizeTime(reservation.horaInicio)}
                      {' - '}
                      {normalizeTime(reservation.horaFin)}
                    </span>

                    <p>
                      {reservation.nombreCaballo || 'Sin caballo'}
                      {' · '}
                      {reservation.cliente}
                    </p>

                    <p>
                      {reservation.nombreEmpleado ||
                        'Sin responsable'}
                      {' · '}
                      {reservation.estado}
                    </p>

                    {reservation.observaciones && (
                      <p>{reservation.observaciones}</p>
                    )}

                    {capacity && (
                      <div
                        className={
                          capacity.available === 0
                            ? 'capacityStatus capacityFull'
                            : 'capacityStatus'
                        }
                      >
                        <Users size={16} />

                        <span>
                          Cupos: {capacity.occupied}/
                          {capacity.maximum}
                          {' · '}
                          Disponibles: {capacity.available}
                        </span>
                      </div>
                    )}

                    <div className="reservationActions">
                      {reservation.estado !== 'CANCELADA' && (
                        <>
                          <button
                            className="secondaryButton"
                            type="button"
                            onClick={() =>
                              startEditing(reservation)
                            }
                          >
                            <Pencil size={16} />
                            Editar
                          </button>

                          <button
                            className="secondaryButton"
                            type="button"
                            onClick={() =>
                              cancelReservation(reservation)
                            }
                          >
                            <XCircle size={16} />
                            Cancelar
                          </button>
                        </>
                      )}
                    </div>
                  </div>
                </article>
              );
            })}
        </section>
      </section>
    </div>
  );
}

function normalizeTime(time) {
  if (!time) {
    return '';
  }

  return time.substring(0, 5);
}

function getScheduleKey(reservation) {
  return [
    reservation.tipo,
    reservation.fecha,
    normalizeTime(reservation.horaInicio),
    normalizeTime(reservation.horaFin)
  ].join('|');
}

function getErrorMessage(error, fallbackMessage) {
  return (
    error?.response?.data?.detail ||
    error?.response?.data?.message ||
    error?.response?.data?.error ||
    fallbackMessage
  );
}