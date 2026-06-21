import { Pencil, Plus, Trash2 } from 'lucide-react';
import { Link } from 'react-router-dom';
import PageHeader from '../components/PageHeader';
import StatusMessage from '../components/StatusMessage';
import { mockEmployees } from '../data/mockData';
import { useAsyncData } from '../hooks/useAsyncData';
import { employeeService } from '../services/employeeService';

export default function EmployeeList() {
  const { data: employees, setData, loading, error } = useAsyncData(employeeService.list, mockEmployees);

  async function handleDelete(id) {
    try {
      await employeeService.remove(id);
    } catch {
      // Vista local.
    }
    setData((current) => current.filter((employee) => employee.id !== id));
  }

  return (
    <div className="stack">
      <PageHeader
        title="Lista de empleados"
        description="Equipo asignado a cuidado, veterinaria, entrenamiento y administracion."
        actions={<Link className="primaryButton" to="/empleados/nuevo"><Plus size={18} />Nuevo</Link>}
      />

      <StatusMessage loading={loading} error={error} empty={!employees.length}>
        <div className="tableWrap">
          <table>
            <thead>
              <tr><th>Nombre</th><th>Rol</th><th>Contacto</th><th>Acciones</th></tr>
            </thead>
            <tbody>
              {employees.map((employee) => (
                <tr key={employee.id}>
                  <td>{employee.nombre}</td>
                  <td><span className="tag">{employee.rol}</span></td>
                  <td>{employee.contacto}</td>
                  <td>
                    <div className="rowActions">
                      <Link className="iconButton" aria-label="Editar empleado" to={`/empleados/${employee.id}/editar`}><Pencil size={18} /></Link>
                      <button className="iconButton danger" type="button" aria-label="Eliminar empleado" onClick={() => handleDelete(employee.id)}><Trash2 size={18} /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </StatusMessage>
    </div>
  );
}
