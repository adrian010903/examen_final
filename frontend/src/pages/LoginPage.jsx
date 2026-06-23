import { Lock, LogIn, UserPlus } from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import FormField from '../components/FormField';
import { authService } from '../services/authService';
import { getHomePath, getUserRole } from '../services/roleAccess';

const loginInitial = { email: '', password: '' };
const registerInitial = { nombre: '', email: '', password: '', role: 'CLIENTE' };

export default function LoginPage() {
  const navigate = useNavigate();
  const [mode, setMode] = useState('login');
  const [loginForm, setLoginForm] = useState(loginInitial);
  const [registerForm, setRegisterForm] = useState(registerInitial);
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState('');

  function validateLogin() {
    const nextErrors = {};
    if (!loginForm.email.trim()) nextErrors.email = 'El correo es obligatorio';
    if (!loginForm.password.trim()) nextErrors.password = 'La contrasena es obligatoria';
    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  function validateRegister() {
    const nextErrors = {};
    if (!registerForm.nombre.trim()) nextErrors.nombre = 'El nombre es obligatorio';
    if (!registerForm.email.trim()) nextErrors.email = 'El correo es obligatorio';
    if (registerForm.password.length < 6) nextErrors.password = 'Minimo 6 caracteres';

    setErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setMessage('');

    try {
      if (mode === 'login') {
        if (!validateLogin()) return;
        await authService.login(loginForm);
      } else {
        if (!validateRegister()) return;
        await authService.register(registerForm);
      }
      const user = authService.getUser();
      navigate(getHomePath(getUserRole(user)));
    } catch (exception) {
      setMessage(exception.message);
    }
  }

  return (
    <main className="loginPage">
      <section className="loginPanel">
        <div className="loginVisual">
          <img
            src="https://images.unsplash.com/photo-1534773728080-33d31da27ae5?auto=format&fit=crop&w=1200&q=80"
            alt="Caballo en establo"
          />
        </div>

        <form className="loginForm" onSubmit={handleSubmit}>
          <div className="loginLogo">
            <div className="brandMark">C</div>
            <div>
              <strong>Caballeriza</strong>
              <span>{mode === 'login' ? 'Acceso administrativo' : 'Registro de usuario'}</span>
            </div>
          </div>

          <div className="segmented">
            <button type="button" className={mode === 'login' ? 'active' : ''} onClick={() => { setMode('login'); setErrors({}); }}>
              Ingresar
            </button>
            <button type="button" className={mode === 'register' ? 'active' : ''} onClick={() => { setMode('register'); setErrors({}); }}>
              Registrar
            </button>
          </div>

          {message && <div className="stateBox stateError">{message}</div>}

          {mode === 'register' && (
            <FormField label="Nombre" error={errors.nombre}>
              <input value={registerForm.nombre} onChange={(event) => setRegisterForm({ ...registerForm, nombre: event.target.value })} />
            </FormField>
          )}

          <FormField label="Correo" error={errors.email}>
            <input
              value={mode === 'login' ? loginForm.email : registerForm.email}
              onChange={(event) => {
                if (mode === 'login') setLoginForm({ ...loginForm, email: event.target.value });
                else setRegisterForm({ ...registerForm, email: event.target.value });
              }}
              placeholder="admin@caballeriza.com"
              type="email"
            />
          </FormField>

          <FormField label="Contrasena" error={errors.password}>
            <input
              value={mode === 'login' ? loginForm.password : registerForm.password}
              onChange={(event) => {
                if (mode === 'login') setLoginForm({ ...loginForm, password: event.target.value });
                else setRegisterForm({ ...registerForm, password: event.target.value });
              }}
              placeholder="********"
              type="password"
            />
          </FormField>



          <button className="primaryButton" type="submit">
            {mode === 'login' ? <LogIn size={18} /> : <UserPlus size={18} />}
            {mode === 'login' ? 'Ingresar' : 'Crear cuenta'}
          </button>

          <div className="loginHint">
            <Lock size={16} />
            <span>La sesion usa JWT del backend y protege las rutas internas.</span>
          </div>
        </form>
      </section>
    </main>
  );
}
