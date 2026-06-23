import {
  AlertTriangle,
  CalendarDays,
  ClipboardList,
  Gauge,
  HeartPulse,
  LogOut,
  Menu,
  Package,
  Shield,
  Users,
  Wheat,
  X
} from 'lucide-react';
import { useMemo, useState } from 'react';
import { NavLink, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import { getUserRole } from '../services/roleAccess';

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: Gauge, roles: ['ADMINISTRADOR', 'VETERINARIO'] },
  { to: '/caballos', label: 'Caballos', icon: Shield, roles: ['ADMINISTRADOR', 'VETERINARIO', 'CUIDADOR'] },
  { to: '/caballos', label: 'Mis caballos', icon: Shield, roles: ['CLIENTE'] },
  { to: '/historial-medico', label: 'Historial medico', icon: HeartPulse, roles: ['ADMINISTRADOR', 'VETERINARIO'] },
  { to: '/empleados', label: 'Empleados', icon: Users, roles: ['ADMINISTRADOR'] },
  { to: '/turnos-tareas', label: 'Turnos y tareas', icon: ClipboardList, roles: ['ADMINISTRADOR', 'CUIDADOR'] },
  { to: '/calendario', label: 'Calendario', icon: CalendarDays, roles: ['ADMINISTRADOR', 'CLIENTE'] },
  { to: '/alimentacion', label: 'Alimentacion', icon: Wheat, roles: ['ADMINISTRADOR', 'CUIDADOR'] },
  { to: '/inventario', label: 'Inventario', icon: Package, roles: ['ADMINISTRADOR', 'CUIDADOR'] },
  { to: '/alertas', label: 'Alertas', icon: AlertTriangle, roles: ['ADMINISTRADOR', 'VETERINARIO', 'CUIDADOR'] }
];

const pageTitles = {
  '/dashboard': 'Dashboard',
  '/caballos': 'Caballos',
  '/historial-medico': 'Historial medico',
  '/empleados': 'Empleados',
  '/turnos-tareas': 'Turnos y tareas',
  '/calendario': 'Calendario y reservas',
  '/alimentacion': 'Alimentacion',
  '/inventario': 'Inventario',
  '/alertas': 'Alertas'
};

export default function AppLayout() {
  const [open, setOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const user = authService.getUser();
  const role = getUserRole(user);
  const visibleNavItems = navItems.filter((item) => item.roles.includes(role));

  const title = useMemo(() => {
    const match = Object.entries(pageTitles).find(([path]) => location.pathname.startsWith(path));
    return match ? match[1] : 'Caballeriza';
  }, [location.pathname]);

  function handleLogout() {
    authService.logout();
    navigate('/login');
  }

  return (
    <div className="shell">
      <aside className={`sidebar ${open ? 'sidebarOpen' : ''}`}>
        <div className="brand">
          <div className="brandMark">C</div>
          <div>
            <strong>Caballeriza</strong>
            <span>Gestion operativa</span>
          </div>
        </div>

        <nav className="navList" aria-label="Navegacion principal">
          {visibleNavItems.map(({ to, label, icon: Icon }) => (
            <NavLink key={to} to={to} onClick={() => setOpen(false)} className="navItem">
              <Icon size={19} />
              <span>{label}</span>
            </NavLink>
          ))}
        </nav>
      </aside>

      {open && <button className="scrim" aria-label="Cerrar menu" onClick={() => setOpen(false)} />}

      <main className="main">
        <header className="topbar">
          <button className="iconButton mobileOnly" type="button" aria-label="Abrir menu" onClick={() => setOpen(true)}>
            <Menu size={21} />
          </button>
          <div>
            <p className="eyebrow">Panel de control</p>
            <h1>{title}</h1>
          </div>
          <div className="topbarActions">
            <span className="userPill">{user?.nombre || user?.email || 'Usuario'}</span>
            <button className="iconButton" type="button" aria-label="Cerrar sesion" onClick={handleLogout}>
              <LogOut size={20} />
            </button>
            <button className="iconButton mobileOnly" type="button" aria-label="Cerrar menu" onClick={() => setOpen(false)}>
              <X size={20} />
            </button>
          </div>
        </header>

        <section className="content">
          <Outlet />
        </section>
      </main>
    </div>
  );
}
