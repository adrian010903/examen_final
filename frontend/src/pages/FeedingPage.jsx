import {
  Package,
  Pencil,
  Plus,
  Save,
  Trash2,
  X
} from 'lucide-react';
import { useMemo, useState } from 'react';

import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import {
  mockFeedingPlans,
  mockHorses
} from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { feedingService } from '../services/feedingService';
import { horseService } from '../services/horseService';

const emptyPlan = {
  tipoAlimento: '',
  cantidad: '',
  frecuencia: '',
  observaciones: '',
  caballoId: ''
};

const EMPTY_SUPPLIES = [];

function createEmptySupply() {
  return {
    fecha: new Date().toISOString().split('T')[0],
    tipo: '',
    cantidad: '',
    caballoId: ''
  };
}

export default function FeedingPage() {
  const horses = useAsyncData(
    horseService.list,
    mockHorses
  );

  const plans = useAsyncData(
    feedingService.listPlans,
    mockFeedingPlans
  );

  const supplies = useAsyncData(
    feedingService.listSupplies,
    EMPTY_SUPPLIES
  );

  const [planForm, setPlanForm] = useState({
    ...emptyPlan
  });

  const [supplyForm, setSupplyForm] = useState(
    createEmptySupply
  );

  const [planErrors, setPlanErrors] = useState({});
  const [supplyErrors, setSupplyErrors] = useState({});

  const [editingPlanId, setEditingPlanId] =
    useState(null);

  const [editingSupplyId, setEditingSupplyId] =
    useState(null);

  const [horseFilter, setHorseFilter] = useState('');
  const [pageError, setPageError] = useState('');
  const [successMessage, setSuccessMessage] =
    useState('');

  const filteredPlans = useMemo(() => {
    if (!horseFilter) {
      return plans.data;
    }

    return plans.data.filter(
      (plan) =>
        String(plan.caballoId) === String(horseFilter)
    );
  }, [plans.data, horseFilter]);

  const filteredSupplies = useMemo(() => {
    const result = !horseFilter
      ? [...supplies.data]
      : supplies.data.filter(
          (supply) =>
            String(supply.caballoId) ===
            String(horseFilter)
        );

    return result.sort((a, b) =>
      String(b.fecha).localeCompare(String(a.fecha))
    );
  }, [supplies.data, horseFilter]);

  function validatePlan() {
    const errors = {};

    if (!planForm.caballoId) {
      errors.caballoId = 'Seleccione un caballo';
    }

    if (!planForm.tipoAlimento.trim()) {
      errors.tipoAlimento =
        'El alimento es obligatorio';
    }

    if (!planForm.cantidad.trim()) {
      errors.cantidad =
        'La cantidad es obligatoria';
    }

    if (!planForm.frecuencia.trim()) {
      errors.frecuencia =
        'La frecuencia es obligatoria';
    }

    setPlanErrors(errors);

    return Object.keys(errors).length === 0;
  }

  function validateSupply() {
    const errors = {};

    if (!supplyForm.caballoId) {
      errors.caballoId = 'Seleccione un caballo';
    }

    if (!supplyForm.fecha) {
      errors.fecha = 'La fecha es obligatoria';
    }

    if (!supplyForm.tipo.trim()) {
      errors.tipo = 'El tipo es obligatorio';
    }

    if (!supplyForm.cantidad.trim()) {
      errors.cantidad =
        'La cantidad es obligatoria';
    }

    setSupplyErrors(errors);

    return Object.keys(errors).length === 0;
  }

  async function handlePlanSubmit(event) {
    event.preventDefault();

    if (!validatePlan()) {
      return;
    }

    setPageError('');
    setSuccessMessage('');

    const payload = {
      tipoAlimento: planForm.tipoAlimento.trim(),
      cantidad: planForm.cantidad.trim(),
      frecuencia: planForm.frecuencia.trim(),
      observaciones: planForm.observaciones.trim(),
      caballoId: Number(planForm.caballoId)
    };

    try {
      if (editingPlanId !== null) {
        const updated =
          await feedingService.updatePlan(
            editingPlanId,
            payload
          );

        plans.setData((current) =>
          current.map((plan) =>
            plan.id === editingPlanId
              ? updated
              : plan
          )
        );

        setSuccessMessage(
          'Plan actualizado correctamente'
        );
      } else {
        const created =
          await feedingService.createPlan(payload);

        plans.setData((current) => [
          created,
          ...current
        ]);

        setSuccessMessage(
          'Plan creado correctamente'
        );
      }

      resetPlanForm();
    } catch (error) {
      setPageError(
        getErrorMessage(
          error,
          'No se pudo guardar el plan'
        )
      );
    }
  }

  async function handleSupplySubmit(event) {
    event.preventDefault();

    if (!validateSupply()) {
      return;
    }

    setPageError('');
    setSuccessMessage('');

    const payload = {
      fecha: supplyForm.fecha,
      tipo: supplyForm.tipo.trim(),
      cantidad: supplyForm.cantidad.trim(),
      caballoId: Number(supplyForm.caballoId)
    };

    try {
      if (editingSupplyId !== null) {
        const updated =
          await feedingService.updateSupply(
            editingSupplyId,
            payload
          );

        supplies.setData((current) =>
          current.map((supply) =>
            supply.id === editingSupplyId
              ? updated
              : supply
          )
        );

        setSuccessMessage(
          'Suministro actualizado correctamente'
        );
      } else {
        const created =
          await feedingService.createSupply(payload);

        supplies.setData((current) => [
          created,
          ...current
        ]);

        setSuccessMessage(
          'Suministro registrado correctamente'
        );
      }

      resetSupplyForm();
    } catch (error) {
      setPageError(
        getErrorMessage(
          error,
          'No se pudo guardar el suministro'
        )
      );
    }
  }

  function startEditingPlan(plan) {
    setEditingPlanId(plan.id);
    setPlanErrors({});
    setPageError('');
    setSuccessMessage('');

    setPlanForm({
      tipoAlimento: plan.tipoAlimento || '',
      cantidad: plan.cantidad || '',
      frecuencia: plan.frecuencia || '',
      observaciones: plan.observaciones || '',
      caballoId: String(plan.caballoId || '')
    });

    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  function startEditingSupply(supply) {
    setEditingSupplyId(supply.id);
    setSupplyErrors({});
    setPageError('');
    setSuccessMessage('');

    setSupplyForm({
      fecha: supply.fecha || '',
      tipo: supply.tipo || '',
      cantidad: supply.cantidad || '',
      caballoId: String(supply.caballoId || '')
    });
  }

  function resetPlanForm() {
    setEditingPlanId(null);
    setPlanForm({ ...emptyPlan });
    setPlanErrors({});
  }

  function resetSupplyForm() {
    setEditingSupplyId(null);
    setSupplyForm(createEmptySupply());
    setSupplyErrors({});
  }

  async function deletePlan(plan) {
    const confirmed = window.confirm(
      `¿Desea eliminar el plan de ${plan.nombreCaballo}?`
    );

    if (!confirmed) {
      return;
    }

    setPageError('');
    setSuccessMessage('');

    try {
      await feedingService.removePlan(plan.id);

      plans.setData((current) =>
        current.filter(
          (currentPlan) =>
            currentPlan.id !== plan.id
        )
      );

      if (editingPlanId === plan.id) {
        resetPlanForm();
      }

      setSuccessMessage(
        'Plan eliminado correctamente'
      );
    } catch (error) {
      setPageError(
        getErrorMessage(
          error,
          'No se pudo eliminar el plan'
        )
      );
    }
  }

  async function deleteSupply(supply) {
    const confirmed = window.confirm(
      `¿Desea eliminar este suministro de ${supply.nombreCaballo}?`
    );

    if (!confirmed) {
      return;
    }

    setPageError('');
    setSuccessMessage('');

    try {
      await feedingService.removeSupply(supply.id);

      supplies.setData((current) =>
        current.filter(
          (currentSupply) =>
            currentSupply.id !== supply.id
        )
      );

      if (editingSupplyId === supply.id) {
        resetSupplyForm();
      }

      setSuccessMessage(
        'Suministro eliminado correctamente'
      );
    } catch (error) {
      setPageError(
        getErrorMessage(
          error,
          'No se pudo eliminar el suministro'
        )
      );
    }
  }

  return (
    <div className="stack">
      <PageHeader
        title="Alimentación"
        description="Planes de alimentación y registro de suministros entregados a cada caballo."
      />

      <section className="panel feedingToolbar">
        <div>
          <h3>Filtrar información</h3>
          <p>
            Consulte los planes y suministros de un
            caballo específico.
          </p>
        </div>

        <select
          value={horseFilter}
          onChange={(event) =>
            setHorseFilter(event.target.value)
          }
        >
          <option value="">
            Todos los caballos
          </option>

          {horses.data.map((horse) => (
            <option
              key={horse.id}
              value={horse.id}
            >
              {horse.nombre}
            </option>
          ))}
        </select>
      </section>

      {pageError && (
        <div className="feedingMessage feedingError">
          {pageError}
        </div>
      )}

      {successMessage && (
        <div className="feedingMessage feedingSuccess">
          {successMessage}
        </div>
      )}

      <div className="feedingSectionTitle">
        <h2>Planes de alimentación</h2>
        <p>
          Administración de alimentos, cantidades y
          frecuencias.
        </p>
      </div>

      <section className="dashboardGrid">
        <form
          className="panel"
          onSubmit={handlePlanSubmit}
        >
          <div className="panelHeader">
            <h3>
              {editingPlanId !== null
                ? 'Editar plan'
                : 'Nuevo plan'}
            </h3>

            {editingPlanId !== null && (
              <button
                className="secondaryButton"
                type="button"
                onClick={resetPlanForm}
              >
                <X size={16} />
                Cancelar
              </button>
            )}
          </div>

          <div className="formGrid single">
            <FormField
              label="Caballo"
              error={planErrors.caballoId}
            >
              <select
                value={planForm.caballoId}
                onChange={(event) =>
                  setPlanForm({
                    ...planForm,
                    caballoId: event.target.value
                  })
                }
              >
                <option value="">
                  Seleccione
                </option>

                {horses.data.map((horse) => (
                  <option
                    key={horse.id}
                    value={horse.id}
                  >
                    {horse.nombre}
                  </option>
                ))}
              </select>
            </FormField>

            <FormField
              label="Tipo de alimento"
              error={planErrors.tipoAlimento}
            >
              <input
                value={planForm.tipoAlimento}
                onChange={(event) =>
                  setPlanForm({
                    ...planForm,
                    tipoAlimento: event.target.value
                  })
                }
                placeholder="Ejemplo: Heno"
              />
            </FormField>

            <FormField
              label="Cantidad"
              error={planErrors.cantidad}
            >
              <input
                value={planForm.cantidad}
                onChange={(event) =>
                  setPlanForm({
                    ...planForm,
                    cantidad: event.target.value
                  })
                }
                placeholder="Ejemplo: 5 kg"
              />
            </FormField>

            <FormField
              label="Frecuencia"
              error={planErrors.frecuencia}
            >
              <input
                value={planForm.frecuencia}
                onChange={(event) =>
                  setPlanForm({
                    ...planForm,
                    frecuencia: event.target.value
                  })
                }
                placeholder="Ejemplo: 2 veces al día"
              />
            </FormField>

            <FormField label="Observaciones">
              <textarea
                value={planForm.observaciones}
                onChange={(event) =>
                  setPlanForm({
                    ...planForm,
                    observaciones: event.target.value
                  })
                }
              />
            </FormField>
          </div>

          <button
            className="primaryButton"
            type="submit"
          >
            {editingPlanId !== null ? (
              <>
                <Save size={18} />
                Guardar cambios
              </>
            ) : (
              <>
                <Plus size={18} />
                Agregar plan
              </>
            )}
          </button>
        </form>

        <article className="panel">
          <div className="panelHeader">
            <h3>Planes registrados</h3>
          </div>

          <div className="feedingList">
            {plans.loading && (
              <p>Cargando planes...</p>
            )}

            {!plans.loading &&
              filteredPlans.length === 0 && (
                <p>No hay planes registrados.</p>
              )}

            {filteredPlans.map((plan) => (
              <article
                className="feedingItem"
                key={plan.id}
              >
                <div className="feedingItemHeader">
                  <div>
                    <strong>
                      {plan.nombreCaballo}
                    </strong>

                    <span className="tag">
                      {plan.frecuencia}
                    </span>
                  </div>

                  <div className="feedingItemActions">
                    <button
                      className="secondaryButton"
                      type="button"
                      onClick={() =>
                        startEditingPlan(plan)
                      }
                    >
                      <Pencil size={15} />
                      Editar
                    </button>

                    <button
                      className="secondaryButton dangerButton"
                      type="button"
                      onClick={() =>
                        deletePlan(plan)
                      }
                    >
                      <Trash2 size={15} />
                      Eliminar
                    </button>
                  </div>
                </div>

                <p>
                  <strong>Alimento:</strong>{' '}
                  {plan.tipoAlimento}
                </p>

                <p>
                  <strong>Cantidad:</strong>{' '}
                  {plan.cantidad}
                </p>

                {plan.observaciones && (
                  <p>{plan.observaciones}</p>
                )}
              </article>
            ))}
          </div>
        </article>
      </section>

      <div className="feedingSectionTitle">
        <h2>Suministros</h2>
        <p>
          Registro de alimentos, medicamentos u otros
          suministros entregados.
        </p>
      </div>

      <section className="dashboardGrid">
        <form
          className="panel"
          onSubmit={handleSupplySubmit}
        >
          <div className="panelHeader">
            <h3>
              {editingSupplyId !== null
                ? 'Editar suministro'
                : 'Nuevo suministro'}
            </h3>

            {editingSupplyId !== null && (
              <button
                className="secondaryButton"
                type="button"
                onClick={resetSupplyForm}
              >
                <X size={16} />
                Cancelar
              </button>
            )}
          </div>

          <div className="formGrid single">
            <FormField
              label="Caballo"
              error={supplyErrors.caballoId}
            >
              <select
                value={supplyForm.caballoId}
                onChange={(event) =>
                  setSupplyForm({
                    ...supplyForm,
                    caballoId: event.target.value
                  })
                }
              >
                <option value="">
                  Seleccione
                </option>

                {horses.data.map((horse) => (
                  <option
                    key={horse.id}
                    value={horse.id}
                  >
                    {horse.nombre}
                  </option>
                ))}
              </select>
            </FormField>

            <FormField
              label="Fecha"
              error={supplyErrors.fecha}
            >
              <input
                type="date"
                value={supplyForm.fecha}
                onChange={(event) =>
                  setSupplyForm({
                    ...supplyForm,
                    fecha: event.target.value
                  })
                }
              />
            </FormField>

            <FormField
              label="Tipo de suministro"
              error={supplyErrors.tipo}
            >
              <input
                value={supplyForm.tipo}
                onChange={(event) =>
                  setSupplyForm({
                    ...supplyForm,
                    tipo: event.target.value
                  })
                }
                placeholder="Ejemplo: Concentrado"
              />
            </FormField>

            <FormField
              label="Cantidad"
              error={supplyErrors.cantidad}
            >
              <input
                value={supplyForm.cantidad}
                onChange={(event) =>
                  setSupplyForm({
                    ...supplyForm,
                    cantidad: event.target.value
                  })
                }
                placeholder="Ejemplo: 3 kg"
              />
            </FormField>
          </div>

          <button
            className="primaryButton"
            type="submit"
          >
            {editingSupplyId !== null ? (
              <>
                <Save size={18} />
                Guardar cambios
              </>
            ) : (
              <>
                <Package size={18} />
                Registrar suministro
              </>
            )}
          </button>
        </form>

        <article className="panel">
          <div className="panelHeader">
            <h3>Suministros registrados</h3>
          </div>

          <div className="feedingList">
            {supplies.loading && (
              <p>Cargando suministros...</p>
            )}

            {!supplies.loading &&
              filteredSupplies.length === 0 && (
                <p>No hay suministros registrados.</p>
              )}

            {filteredSupplies.map((supply) => (
              <article
                className="feedingItem"
                key={supply.id}
              >
                <div className="feedingItemHeader">
                  <div>
                    <strong>
                      {supply.nombreCaballo}
                    </strong>

                    <span className="tag">
                      {supply.fecha}
                    </span>
                  </div>

                  <div className="feedingItemActions">
                    <button
                      className="secondaryButton"
                      type="button"
                      onClick={() =>
                        startEditingSupply(supply)
                      }
                    >
                      <Pencil size={15} />
                      Editar
                    </button>

                    <button
                      className="secondaryButton dangerButton"
                      type="button"
                      onClick={() =>
                        deleteSupply(supply)
                      }
                    >
                      <Trash2 size={15} />
                      Eliminar
                    </button>
                  </div>
                </div>

                <p>
                  <strong>Tipo:</strong>{' '}
                  {supply.tipo}
                </p>

                <p>
                  <strong>Cantidad:</strong>{' '}
                  {supply.cantidad}
                </p>
              </article>
            ))}
          </div>
        </article>
      </section>
    </div>
  );
}

function getErrorMessage(error, fallbackMessage) {
  return (
    error?.response?.data?.detail ||
    error?.response?.data?.message ||
    error?.response?.data?.error ||
    fallbackMessage
  );
}
