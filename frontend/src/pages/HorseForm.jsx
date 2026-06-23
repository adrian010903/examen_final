import { Save } from 'lucide-react';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import FormField from '../components/FormField';
import PageHeader from '../components/PageHeader';
import { mockHorses } from '../data/mockData';
import { authService } from '../services/authService';
import { horseService } from '../services/horseService';
import { getUserRole } from '../services/roleAccess';

const emptyHorse = { nombre: '', identificador: '', edad: '', raza: '', sexo: '', peso: '', fotoUrl: '' };

export default function HorseForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isClient = getUserRole(authService.getUser()) === 'CLIENTE';
  const [form, setForm] = useState(emptyHorse);
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (!id) return;

    horseService.get(id)
      .then((horse) => setForm({ ...emptyHorse, ...horse }))
      .catch(() => {
        const fallback = mockHorses.find((horse) => String(horse.id) === String(id));
        if (fallback) setForm({ ...emptyHorse, ...fallback });
      });
  }, [id]);

  function validate() {
    const nextErrors = {};
    if (!form.nombre.trim()) nextErrors.nombre = 'El nombre es obligatorio';
    if (!form.identificador.trim()) nextErrors.identificador = 'El identificador es obligatorio';
    if (!form.raza.trim()) nextErrors.raza = 'La raza es obligatoria';
    if (!form.sexo.trim()) nextErrors.sexo = 'El sexo es obligatorio';
    if (form.edad === '' || Number(form.edad) < 0) nextErrors.edad = 'La edad debe ser 0 o mayor';
    if (form.peso === '' || Number(form.peso) <= 0) nextErrors.peso = 'El peso debe ser mayor a 0';
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    if (!validate()) return;

    const payload = { ...form, edad: Number(form.edad), peso: Number(form.peso) };

    try {
      if (id) await horseService.update(id, payload);
      else await horseService.create(payload);
      navigate('/caballos');
    } catch (exception) {
      setMessage(exception.message);
    }
  }

  return (
    <div className="stack">
      <PageHeader
        title={id ? 'Editar caballo' : isClient ? 'Registrar caballo' : 'Formulario de caballo'}
        description="Complete los datos obligatorios para mantener el registro actualizado."
      />

      <form className="formPanel" onSubmit={handleSubmit}>
        {message && <div className="stateBox stateError">{message}</div>}
        <div className="formGrid">
          <FormField label="Nombre" error={errors.nombre}>
            <input value={form.nombre} onChange={(event) => setForm({ ...form, nombre: event.target.value })} />
          </FormField>
          <FormField label="Identificador" error={errors.identificador}>
            <input value={form.identificador} onChange={(event) => setForm({ ...form, identificador: event.target.value })} />
          </FormField>
          <FormField label="Edad" error={errors.edad}>
            <input type="number" min="0" value={form.edad} onChange={(event) => setForm({ ...form, edad: event.target.value })} />
          </FormField>
          <FormField label="Peso kg" error={errors.peso}>
            <input type="number" min="1" step="0.1" value={form.peso} onChange={(event) => setForm({ ...form, peso: event.target.value })} />
          </FormField>
          <FormField label="Raza" error={errors.raza}>
            <input value={form.raza} onChange={(event) => setForm({ ...form, raza: event.target.value })} />
          </FormField>
          <FormField label="Sexo" error={errors.sexo}>
            <select value={form.sexo} onChange={(event) => setForm({ ...form, sexo: event.target.value })}>
              <option value="">Seleccione</option>
              <option value="Macho">Macho</option>
              <option value="Hembra">Hembra</option>
            </select>
          </FormField>
          <FormField label="Foto URL">
            <input value={form.fotoUrl || ''} onChange={(event) => setForm({ ...form, fotoUrl: event.target.value })} />
          </FormField>
        </div>
        <div className="formActions">
          <button className="secondaryButton" type="button" onClick={() => navigate('/caballos')}>Cancelar</button>
          <button className="primaryButton" type="submit">
            <Save size={18} />
            Guardar
          </button>
        </div>
      </form>
    </div>
  );
}
