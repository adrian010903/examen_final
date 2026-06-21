import { AlertTriangle, CalendarClock, Package, Shield, Users } from 'lucide-react';
import { useMemo } from 'react';
import MetricCard from '../components/MetricCard';
import PageHeader from '../components/PageHeader';
import { mockEmployees, mockHorses, mockInventory, mockTasks } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { employeeService } from '../services/employeeService';
import { horseService } from '../services/horseService';
import { inventoryService } from '../services/inventoryService';
import { taskService } from '../services/taskService';

export default function Dashboard() {
  const horses = useAsyncData(horseService.list, mockHorses);
  const employees = useAsyncData(employeeService.list, mockEmployees);
  const inventory = useAsyncData(inventoryService.list, mockInventory);
  const tasks = useAsyncData(taskService.list, mockTasks);

  const lowStock = useMemo(() => inventory.data.filter((item) => item.stockBajo), [inventory.data]);
  const pendingTasks = useMemo(() => tasks.data.filter((task) => !task.completada), [tasks.data]);

  return (
    <div className="stack">
      <PageHeader title="Resumen operativo" description="Vista rapida del estado actual de la caballeriza." />

      <section className="metricGrid">
        <MetricCard label="Caballos" value={horses.data.length} icon={Shield} tone="green" />
        <MetricCard label="Empleados" value={employees.data.length} icon={Users} tone="blue" />
        <MetricCard label="Tareas pendientes" value={pendingTasks.length} icon={CalendarClock} tone="amber" />
        <MetricCard label="Stock bajo" value={lowStock.length} icon={Package} tone="red" />
      </section>

      <section className="dashboardGrid">
        <article className="panel">
          <div className="panelHeader">
            <h3>Actividad proxima</h3>
          </div>
          <div className="timeline">
            {pendingTasks.map((task) => (
              <div className="timelineItem" key={task.id}>
                <span>{task.fecha}</span>
                <strong>{task.descripcion}</strong>
                <p>{task.nombreEmpleado || 'Sin empleado asignado'}</p>
              </div>
            ))}
          </div>
        </article>

        <article className="panel">
          <div className="panelHeader">
            <h3>Alertas activas</h3>
            <AlertTriangle size={20} />
          </div>
          <div className="alertList">
            {lowStock.map((item) => (
              <div className="alertRow" key={item.id}>
                <strong>{item.nombre}</strong>
                <span>{item.cantidad} {item.unidad}</span>
              </div>
            ))}
          </div>
        </article>
      </section>
    </div>
  );
}
