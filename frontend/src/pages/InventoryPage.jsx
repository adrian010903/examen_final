import { Plus } from 'lucide-react';
import { useState } from 'react';
import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import StatusMessage from '../components/StatusMessage';
import { mockInventory } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { inventoryService } from '../services/inventoryService';

const emptyItem = { nombre: '', tipo: '', cantidad: '', stockMinimo: '', unidad: '' };

export default function InventoryPage() {
  const inventory = useAsyncData(inventoryService.list, mockInventory);
  const [form, setForm] = useState(emptyItem);
  const [errors, setErrors] = useState({});

  function validate() {
    const nextErrors = {};
    if (!form.nombre.trim()) nextErrors.nombre = 'El nombre es obligatorio';
    if (!form.tipo.trim()) nextErrors.tipo = 'El tipo es obligatorio';
    if (form.cantidad === '' || Number(form.cantidad) < 0) nextErrors.cantidad = 'La cantidad debe ser 0 o mayor';
    if (form.stockMinimo === '' || Number(form.stockMinimo) < 0) nextErrors.stockMinimo = 'El minimo debe ser 0 o mayor';
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!validate()) return;
    const payload = { ...form, cantidad: Number(form.cantidad), stockMinimo: Number(form.stockMinimo) };
    try {
      const saved = await inventoryService.create(payload);
      inventory.setData((current) => [saved, ...current]);
    } catch {
      inventory.setData((current) => [{ ...payload, id: Date.now(), stockBajo: payload.cantidad <= payload.stockMinimo }, ...current]);
    }
    setForm(emptyItem);
  }

  return (
    <div className="stack">
      <PageHeader title="Inventario" description="Control de productos, cantidades y stock minimo." />
      <section className="dashboardGrid">
        <form className="panel" onSubmit={handleSubmit}>
          <div className="panelHeader"><h3>Nuevo producto</h3></div>
          <div className="formGrid single">
            <FormField label="Nombre" error={errors.nombre}><input value={form.nombre} onChange={(event) => setForm({ ...form, nombre: event.target.value })} /></FormField>
            <FormField label="Tipo" error={errors.tipo}><input value={form.tipo} onChange={(event) => setForm({ ...form, tipo: event.target.value })} /></FormField>
            <FormField label="Cantidad" error={errors.cantidad}><input type="number" min="0" value={form.cantidad} onChange={(event) => setForm({ ...form, cantidad: event.target.value })} /></FormField>
            <FormField label="Stock minimo" error={errors.stockMinimo}><input type="number" min="0" value={form.stockMinimo} onChange={(event) => setForm({ ...form, stockMinimo: event.target.value })} /></FormField>
            <FormField label="Unidad"><input value={form.unidad} onChange={(event) => setForm({ ...form, unidad: event.target.value })} /></FormField>
          </div>
          <button className="primaryButton" type="submit"><Plus size={18} />Agregar</button>
        </form>
        <StatusMessage loading={inventory.loading} error={inventory.error} empty={!inventory.data.length}>
          <div className="tableWrap">
            <table>
              <thead><tr><th>Producto</th><th>Tipo</th><th>Cantidad</th><th>Minimo</th><th>Estado</th></tr></thead>
              <tbody>
                {inventory.data.map((item) => (
                  <tr key={item.id}><td>{item.nombre}</td><td>{item.tipo}</td><td>{item.cantidad} {item.unidad}</td><td>{item.stockMinimo}</td><td><span className={`tag ${item.stockBajo ? 'tagDanger' : 'tagSuccess'}`}>{item.stockBajo ? 'Stock bajo' : 'Correcto'}</span></td></tr>
                ))}
              </tbody>
            </table>
          </div>
        </StatusMessage>
      </section>
    </div>
  );
}
