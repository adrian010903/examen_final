export const DEFAULT_ROLE = 'CLIENTE';

export function getUserRole(user) {
  return user?.role || DEFAULT_ROLE;
}

export function getHomePath(role) {
  if (role === 'CLIENTE') {
    return '/calendario';
  }

  if (role === 'CUIDADOR') {
    return '/turnos-tareas';
  }

  return '/dashboard';
}

export function canAccessRole(user, allowedRoles) {
  if (!allowedRoles?.length) {
    return true;
  }

  return allowedRoles.includes(getUserRole(user));
}
