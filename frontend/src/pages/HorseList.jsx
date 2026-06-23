import { Eye, Pencil, Plus, Trash2 } from 'lucide-react';
import { Link } from 'react-router-dom';
import PageHeader from '../components/PageHeader';
import StatusMessage from '../components/StatusMessage';
import { mockHorses } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { authService } from '../services/authService';
import { horseService } from '../services/horseService';
import { getUserRole } from '../services/roleAccess';

const emptyHorseList = [];

export default function HorseList() {
  const isClient = getUserRole(authService.getUser()) === 'CLIENTE';
  const { data: horses, setData, loading, error } = useAsyncData(
    horseService.list,
    isClient ? emptyHorseList : mockHorses
  );

  async function handleDelete(id) {
    try {
      await horseService.remove(id);
    } catch {
      // Mantiene la interaccion de la vista aun si el backend no responde.
    }
    setData((current) => current.filter((horse) => horse.id !== id));
  }

  return (
    <div className="stack">
      <PageHeader
        title={isClient ? 'Mis caballos' : 'Lista de caballos'}
        description={
          isClient
            ? 'Registre y mantenga actualizados los datos de sus caballos.'
            : 'Registro general con acceso rapido al detalle y edicion.'
        }
        actions={
          <Link className="primaryButton" to="/caballos/nuevo">
            <Plus size={18} />
            Nuevo
          </Link>
        }
      />

      <StatusMessage loading={loading} error={error} empty={!horses.length}>
        <div className="horseGrid">
          {horses.map((horse) => (
            <article className="horseCard" key={horse.id}>
              <img src={horse.fotoUrl || mockHorses[0].fotoUrl} alt={horse.nombre} />
              <div>
                <span>{horse.identificador}</span>
                <h3>{horse.nombre}</h3>
                <p>{horse.raza} · {horse.edad} anos · {horse.peso} kg</p>
              </div>
              <div className="rowActions">
                <Link className="iconButton" aria-label="Ver detalle" to={`/caballos/${horse.id}`}>
                  <Eye size={18} />
                </Link>
                <Link className="iconButton" aria-label="Editar" to={`/caballos/${horse.id}/editar`}>
                  <Pencil size={18} />
                </Link>
                <button className="iconButton danger" type="button" aria-label="Eliminar" onClick={() => handleDelete(horse.id)}>
                  <Trash2 size={18} />
                </button>
              </div>
            </article>
          ))}
        </div>
      </StatusMessage>
    </div>
  );
}
