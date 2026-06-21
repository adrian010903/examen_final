import { Save } from 'lucide-react';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import { mockEmployees } from '../data/mockData';
import { employeeService } from '../services/employeeService';

const emptyEmployee = { nombre: '', rol: '', contacto: '' };

export default function EmployeeForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState(emptyEmployee);
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (!id) return;
    employeeService.get(id)
      .then((employee) => setForm({ ...emptyEmployee, ...employee }))
      .catch(() => {
        const fallback = mockEmployees.find((employee) => String(employee.id) === String(id));
        if (fallback) setForm({ ...emptyEmployee, ...fallback });
      });
  }, [id]);

  function validate() {
    const nextErrors = {};
    if (!form.nombre.trim()) nextErrors.nombre = 'El nombre es obligatorio';
    if (!form.rol) nextErrors.rol = 'El rol es obligatorio';
    if (!form.contacto.trim()) nextErrors.contacto = 'El contacto es obligatorio';
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!validate()) return;
    try {
      if (id) await employeeService.update(id, form);
      else await employeeService.create(form);
      navigate('/empleados');
    } catch (exception) {
      setMessage(exception.message);
    }
  }

  return (
    <div className="stack">
      <PageHeader title={id ? 'Editar empleado' : 'Formulario de empleado'} description="Mantenga roles y contacto actualizados." />
      <form className="formPanel" onSubmit={handleSubmit}>
        {message && <div className="stateBox stateError">{message}</div>}
        <div className="formGrid">
          <FormField label="Nombre" error={errors.nombre}>
            <input value={form.nombre} onChange={(event) => setForm({ ...form, nombre: event.target.value })} />
          </FormField>
          <FormField label="Rol" error={errors.rol}>
            <select value={form.rol} onChange={(event) => setForm({ ...form, rol: event.target.value })}>
              <option value="">Seleccione</option>
              <option value="ADMINISTRADOR">Administrador</option>
              <option value="VETERINARIO">Veterinario</option>
              <option value="CUIDADOR">Cuidador</option>
              <option value="ENTRENADOR">Entrenador</option>
            </select>
          </FormField>
          <FormField label="Contacto" error={errors.contacto}>
            <input value={form.contacto} onChange={(event) => setForm({ ...form, contacto: event.target.value })} />
          </FormField>
        </div>
        <div className="formActions">
          <button className="secondaryButton" type="button" onClick={() => navigate('/empleados')}>Cancelar</button>
          <button className="primaryButton" type="submit"><Save size={18} />Guardar</button>
        </div>
      </form>
    </div>
  );
}
