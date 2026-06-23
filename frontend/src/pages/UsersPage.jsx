import {
  RefreshCw,
  Save,
  Trash2,
  UserCog
} from 'lucide-react';
import { useEffect, useState } from 'react';

import PageHeader from '../components/PageHeader';
import { authService } from '../services/authService';
import { userService } from '../services/userService';

const roles = [
  'ADMINISTRADOR',
  'VETERINARIO',
  'CUIDADOR',
  'CLIENTE'
];

export default function UsersPage() {
  const [users, setUsers] = useState([]);
  const [selectedRoles, setSelectedRoles] = useState({});
  const [loading, setLoading] = useState(true);
  const [savingId, setSavingId] = useState(null);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const currentUser = authService.getUser();
  const currentEmail = currentUser?.email || '';

  useEffect(() => {
    loadUsers();
  }, []);

  async function loadUsers() {
    setLoading(true);
    setError('');
    setMessage('');

    try {
      const data = await userService.list();
      const userList = Array.isArray(data) ? data : [];

      setUsers(userList);

      const initialRoles = {};

      userList.forEach((user) => {
        initialRoles[user.id] = user.role;
      });

      setSelectedRoles(initialRoles);
    } catch (requestError) {
      setError(
        getErrorMessage(
          requestError,
          'No se pudieron cargar los usuarios'
        )
      );
    } finally {
      setLoading(false);
    }
  }

  async function saveRole(user) {
    const newRole = selectedRoles[user.id];

    if (!newRole) {
      return;
    }

    setSavingId(user.id);
    setError('');
    setMessage('');

    try {
      const updated = await userService.updateRole(
        user.id,
        newRole
      );

      setUsers((current) =>
        current.map((item) =>
          item.id === updated.id ? updated : item
        )
      );

      setMessage(
        `Rol de ${updated.nombre || updated.email} actualizado correctamente`
      );
    } catch (requestError) {
      setError(
        getErrorMessage(
          requestError,
          'No se pudo actualizar el rol'
        )
      );
    } finally {
      setSavingId(null);
    }
  }

  async function deleteUser(user) {
    const isCurrentUser =
      currentEmail &&
      user.email?.toLowerCase() === currentEmail.toLowerCase();

    if (isCurrentUser) {
      setError('No puede eliminar su propia cuenta');
      return;
    }

    const confirmed = window.confirm(
      `¿Desea eliminar al usuario ${user.nombre || user.email}?`
    );

    if (!confirmed) {
      return;
    }

    setError('');
    setMessage('');

    try {
      await userService.remove(user.id);

      setUsers((current) =>
        current.filter((item) => item.id !== user.id)
      );

      setMessage('Usuario eliminado correctamente');
    } catch (requestError) {
      setError(
        getErrorMessage(
          requestError,
          'No se pudo eliminar el usuario'
        )
      );
    }
  }

  return (
    <div className="stack">
      <PageHeader
        title="Usuarios"
        description="Administración de cuentas y roles del sistema."
      />

      <section className="panel usersToolbar">
        <div>
          <h3>Usuarios registrados</h3>
          <p>
            Total de usuarios: <strong>{users.length}</strong>
          </p>
        </div>

        <button
          className="secondaryButton"
          type="button"
          onClick={loadUsers}
        >
          <RefreshCw size={17} />
          Actualizar
        </button>
      </section>

      {error && (
        <div className="usersMessage usersError">
          {error}
        </div>
      )}

      {message && (
        <div className="usersMessage usersSuccess">
          {message}
        </div>
      )}

      {loading && (
        <section className="panel">
          Cargando usuarios...
        </section>
      )}

      {!loading && users.length === 0 && (
        <section className="panel">
          No hay usuarios registrados.
        </section>
      )}

      {!loading && users.length > 0 && (
        <section className="usersGrid">
          {users.map((user) => {
            const isCurrentUser =
              currentEmail &&
              user.email?.toLowerCase() ===
                currentEmail.toLowerCase();

            const roleChanged =
              selectedRoles[user.id] !== user.role;

            return (
              <article
                className="panel userCard"
                key={user.id}
              >
                <div className="userCardHeader">
                  <div className="userIcon">
                    <UserCog size={23} />
                  </div>

                  <div>
                    <strong>
                      {user.nombre || 'Usuario sin nombre'}
                    </strong>

                    <p>{user.email}</p>
                  </div>

                  {isCurrentUser && (
                    <span className="tag">
                      Mi cuenta
                    </span>
                  )}
                </div>

                <div className="userRoleControl">
                  <label htmlFor={`role-${user.id}`}>
                    Rol
                  </label>

                  <select
                    id={`role-${user.id}`}
                    value={selectedRoles[user.id] || ''}
                    onChange={(event) =>
                      setSelectedRoles((current) => ({
                        ...current,
                        [user.id]: event.target.value
                      }))
                    }
                  >
                    {roles.map((role) => (
                      <option key={role} value={role}>
                        {formatRole(role)}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="userCardActions">
                  <button
                    className="primaryButton"
                    type="button"
                    disabled={
                      !roleChanged || savingId === user.id
                    }
                    onClick={() => saveRole(user)}
                  >
                    <Save size={16} />

                    {savingId === user.id
                      ? 'Guardando...'
                      : 'Guardar rol'}
                  </button>

                  <button
                    className="secondaryButton dangerButton"
                    type="button"
                    disabled={isCurrentUser}
                    onClick={() => deleteUser(user)}
                  >
                    <Trash2 size={16} />
                    Eliminar
                  </button>
                </div>
              </article>
            );
          })}
        </section>
      )}
    </div>
  );
}

function formatRole(role) {
  const names = {
    ADMINISTRADOR: 'Administrador',
    VETERINARIO: 'Veterinario',
    CUIDADOR: 'Cuidador',
    CLIENTE: 'Cliente'
  };

  return names[role] || role;
}

function getErrorMessage(error, fallbackMessage) {
  return (
    error?.response?.data?.detail ||
    error?.response?.data?.message ||
    error?.response?.data?.error ||
    fallbackMessage
  );
}

