import { AlertTriangle, CheckCircle2 } from 'lucide-react';
import PageHeader from '../components/PageHeader';
import StatusMessage from '../components/StatusMessage';
import { mockInventory, mockTasks } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { alertService } from '../services/alertService';

const fallbackAlerts = [
  ...mockInventory
    .filter((item) => item.stockBajo)
    .map((item) => ({
      tipo: 'STOCK_BAJO',
      severidad: 'ALTA',
      titulo: `Stock bajo: ${item.nombre}`,
      descripcion: `${item.cantidad} ${item.unidad} disponibles. Minimo requerido: ${item.stockMinimo}.`
    })),
  ...mockTasks
    .filter((task) => !task.completada)
    .map((task) => ({
      tipo: 'TAREA',
      severidad: 'MEDIA',
      titulo: 'Tarea pendiente',
      descripcion: `${task.descripcion} - ${task.fecha}`
    }))
];

export default function AlertsPage() {
  const alerts = useAsyncData(alertService.list, fallbackAlerts);

  return (
    <div className="stack">
      <PageHeader title="Alertas" description="Vacunaciones proximas, tratamientos vencidos y stock bajo." />

      <StatusMessage loading={alerts.loading} error={alerts.error} empty={!alerts.data.length}>
        <section className="alertGrid">
          {alerts.data.map((alert, index) => (
            <article className={`alertCard ${alert.severidad === 'ALTA' ? 'high' : 'medium'}`} key={`${alert.tipo}-${alert.referenciaId || index}`}>
              <AlertTriangle size={22} />
              <div>
                <strong>{alert.titulo}</strong>
                <p>{alert.descripcion}</p>
                {alert.fecha && <span className="tag">{alert.fecha}</span>}
              </div>
            </article>
          ))}
          {!alerts.data.length && (
            <article className="alertCard ok">
              <CheckCircle2 size={22} />
              <div>
                <strong>Sin alertas activas</strong>
                <p>La operacion se encuentra al dia.</p>
              </div>
            </article>
          )}
        </section>
      </StatusMessage>
    </div>
  );
}
