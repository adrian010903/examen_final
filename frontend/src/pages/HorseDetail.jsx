import { Pencil } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import PageHeader from '../components/PageHeader';
import { mockHorses, mockMedicalRecords, mockTasks } from '../data/mockData';
import { horseService } from '../services/horseService';
import { medicalService } from '../services/medicalService';
import { taskService } from '../services/taskService';

export default function HorseDetail() {
  const { id } = useParams();
  const [horse, setHorse] = useState(null);
  const [records, setRecords] = useState([]);
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    horseService.get(id)
      .then(setHorse)
      .catch(() => setHorse(mockHorses.find((item) => String(item.id) === String(id)) || mockHorses[0]));

    medicalService.listByHorse(id)
      .then(setRecords)
      .catch(() => setRecords(mockMedicalRecords.filter((record) => String(record.caballoId) === String(id))));

    taskService.listByHorse(id)
      .then(setTasks)
      .catch(() => setTasks(mockTasks.filter((task) => String(task.caballoId) === String(id))));
  }, [id]);

  if (!horse) return <div className="stateBox">Cargando detalle...</div>;

  return (
    <div className="stack">
      <PageHeader
        title={horse.nombre}
        description={`${horse.identificador} · ${horse.raza}`}
        actions={
          <Link className="primaryButton" to={`/caballos/${horse.id}/editar`}>
            <Pencil size={18} />
            Editar
          </Link>
        }
      />

      <section className="detailGrid">
        <article className="detailHero">
          <img src={horse.fotoUrl || mockHorses[0].fotoUrl} alt={horse.nombre} />
        </article>
        <article className="panel">
          <div className="infoGrid">
            <div><span>Edad</span><strong>{horse.edad} anos</strong></div>
            <div><span>Peso</span><strong>{horse.peso} kg</strong></div>
            <div><span>Sexo</span><strong>{horse.sexo}</strong></div>
            <div><span>Raza</span><strong>{horse.raza}</strong></div>
          </div>
        </article>
      </section>

      <section className="dashboardGrid">
        <article className="panel">
          <div className="panelHeader"><h3>Historial medico</h3></div>
          {records.map((record) => (
            <div className="listRow" key={record.id}>
              <strong>{record.tipo}</strong>
              <span>{record.fecha}</span>
              <p>{record.descripcion}</p>
            </div>
          ))}
        </article>
        <article className="panel">
          <div className="panelHeader"><h3>Tareas</h3></div>
          {tasks.map((task) => (
            <div className="listRow" key={task.id}>
              <strong>{task.descripcion}</strong>
              <span>{task.fecha}</span>
              <p>{task.completada ? 'Completada' : 'Pendiente'}</p>
            </div>
          ))}
        </article>
      </section>
    </div>
  );
}
