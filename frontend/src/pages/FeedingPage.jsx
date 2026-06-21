import { Plus } from 'lucide-react';
import { useState } from 'react';
import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import { mockFeedingPlans, mockHorses } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { feedingService } from '../services/feedingService';
import { horseService } from '../services/horseService';

const emptyPlan = { tipoAlimento: '', cantidad: '', frecuencia: '', observaciones: '', caballoId: '' };

export default function FeedingPage() {
  const plans = useAsyncData(feedingService.listPlans, mockFeedingPlans);
  const horses = useAsyncData(horseService.list, mockHorses);
  const [form, setForm] = useState(emptyPlan);
  const [errors, setErrors] = useState({});

  function validate() {
    const nextErrors = {};
    if (!form.tipoAlimento.trim()) nextErrors.tipoAlimento = 'El alimento es obligatorio';
    if (!form.cantidad.trim()) nextErrors.cantidad = 'La cantidad es obligatoria';
    if (!form.frecuencia.trim()) nextErrors.frecuencia = 'La frecuencia es obligatoria';
    if (!form.caballoId) nextErrors.caballoId = 'Seleccione un caballo';
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!validate()) return;
    const selectedHorse = horses.data.find((horse) => String(horse.id) === String(form.caballoId));
    const payload = { ...form, caballoId: Number(form.caballoId), nombreCaballo: selectedHorse?.nombre };
    try {
      const saved = await feedingService.createPlan(payload);
      plans.setData((current) => [saved, ...current]);
    } catch {
      plans.setData((current) => [{ ...payload, id: Date.now() }, ...current]);
    }
    setForm(emptyPlan);
  }

  return (
    <div className="stack">
      <PageHeader title="Alimentacion" description="Planes de alimentacion por caballo y frecuencia." />
      <section className="dashboardGrid">
        <form className="panel" onSubmit={handleSubmit}>
          <div className="panelHeader"><h3>Nuevo plan</h3></div>
          <div className="formGrid single">
            <FormField label="Caballo" error={errors.caballoId}>
              <select value={form.caballoId} onChange={(event) => setForm({ ...form, caballoId: event.target.value })}>
                <option value="">Seleccione</option>
                {horses.data.map((horse) => <option key={horse.id} value={horse.id}>{horse.nombre}</option>)}
              </select>
            </FormField>
            <FormField label="Tipo de alimento" error={errors.tipoAlimento}><input value={form.tipoAlimento} onChange={(event) => setForm({ ...form, tipoAlimento: event.target.value })} /></FormField>
            <FormField label="Cantidad" error={errors.cantidad}><input value={form.cantidad} onChange={(event) => setForm({ ...form, cantidad: event.target.value })} /></FormField>
            <FormField label="Frecuencia" error={errors.frecuencia}><input value={form.frecuencia} onChange={(event) => setForm({ ...form, frecuencia: event.target.value })} /></FormField>
            <FormField label="Observaciones"><textarea value={form.observaciones} onChange={(event) => setForm({ ...form, observaciones: event.target.value })} /></FormField>
          </div>
          <button className="primaryButton" type="submit"><Plus size={18} />Agregar</button>
        </form>
        <article className="panel">
          <div className="panelHeader"><h3>Planes activos</h3></div>
          {plans.data.map((plan) => (
            <div className="listRow" key={plan.id}><strong>{plan.nombreCaballo}</strong><span>{plan.frecuencia}</span><p>{plan.tipoAlimento} · {plan.cantidad}</p></div>
          ))}
        </article>
      </section>
    </div>
  );
}
