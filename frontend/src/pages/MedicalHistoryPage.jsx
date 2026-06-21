import { Plus } from 'lucide-react';
import { useState } from 'react';
import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import StatusMessage from '../components/StatusMessage';
import { mockHorses, mockMedicalRecords } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { horseService } from '../services/horseService';
import { medicalService } from '../services/medicalService';

const emptyRecord = { tipo: '', descripcion: '', alergias: '', observaciones: '', fecha: '', fechaProxima: '', responsable: '', caballoId: '' };

export default function MedicalHistoryPage() {
  const records = useAsyncData(medicalService.list, mockMedicalRecords);
  const horses = useAsyncData(horseService.list, mockHorses);
  const [form, setForm] = useState(emptyRecord);
  const [errors, setErrors] = useState({});

  function validate() {
    const nextErrors = {};
    if (!form.tipo.trim()) nextErrors.tipo = 'El tipo es obligatorio';
    if (!form.descripcion.trim()) nextErrors.descripcion = 'La descripcion es obligatoria';
    if (!form.fecha) nextErrors.fecha = 'La fecha es obligatoria';
    if (!form.responsable.trim()) nextErrors.responsable = 'El responsable es obligatorio';
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
      const saved = await medicalService.create(payload);
      records.setData((current) => [saved, ...current]);
    } catch {
      records.setData((current) => [{ ...payload, id: Date.now() }, ...current]);
    }
    setForm(emptyRecord);
  }

  return (
    <div className="stack">
      <PageHeader title="Historial medico" description="Controles, tratamientos, alergias y observaciones por caballo." />
      <section className="dashboardGrid">
        <form className="panel" onSubmit={handleSubmit}>
          <div className="panelHeader"><h3>Nuevo registro</h3></div>
          <div className="formGrid single">
            <FormField label="Tipo" error={errors.tipo}><input value={form.tipo} onChange={(event) => setForm({ ...form, tipo: event.target.value })} /></FormField>
            <FormField label="Caballo" error={errors.caballoId}>
              <select value={form.caballoId} onChange={(event) => setForm({ ...form, caballoId: event.target.value })}>
                <option value="">Seleccione</option>
                {horses.data.map((horse) => <option key={horse.id} value={horse.id}>{horse.nombre}</option>)}
              </select>
            </FormField>
            <FormField label="Fecha" error={errors.fecha}><input type="date" value={form.fecha} onChange={(event) => setForm({ ...form, fecha: event.target.value })} /></FormField>
            <FormField label="Proximo control">
              <input type="date" value={form.fechaProxima} onChange={(event) => setForm({ ...form, fechaProxima: event.target.value })} />
            </FormField>
            <FormField label="Responsable" error={errors.responsable}><input value={form.responsable} onChange={(event) => setForm({ ...form, responsable: event.target.value })} /></FormField>
            <FormField label="Descripcion" error={errors.descripcion}><textarea value={form.descripcion} onChange={(event) => setForm({ ...form, descripcion: event.target.value })} /></FormField>
            <FormField label="Alergias"><input value={form.alergias} onChange={(event) => setForm({ ...form, alergias: event.target.value })} /></FormField>
          </div>
          <button className="primaryButton" type="submit"><Plus size={18} />Registrar</button>
        </form>
        <StatusMessage loading={records.loading} error={records.error} empty={!records.data.length}>
          <article className="panel">
            <div className="panelHeader"><h3>Registros recientes</h3></div>
            {records.data.map((record) => (
              <div className="listRow" key={record.id}>
                <strong>{record.tipo} · {record.nombreCaballo}</strong>
                <span>{record.fecha}{record.fechaProxima ? ` · Proximo: ${record.fechaProxima}` : ''}</span>
                <p>{record.descripcion}</p>
              </div>
            ))}
          </article>
        </StatusMessage>
      </section>
    </div>
  );
}
