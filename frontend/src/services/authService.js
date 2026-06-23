import api, { TOKEN_KEY, USER_KEY } from './api';

function saveSession(response, user) {
  localStorage.setItem(TOKEN_KEY, response.token);
  localStorage.setItem(USER_KEY, JSON.stringify(user));
  return response;
}

export const authService = {
  login: async (credentials) => {
    const response = await api.post('/auth/login', credentials).then((result) => result.data);
    return saveSession(response, {
      email: response.email || credentials.email,
      nombre: response.nombre || credentials.email.split('@')[0],
      role: response.role
    });
  },
  register: async (payload) => {
    const response = await api.post('/auth/register', payload).then((result) => result.data);
    return saveSession(response, {
      email: response.email || payload.email,
      nombre: response.nombre || payload.nombre,
      role: response.role || payload.role
    });
  },
  logout: () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  },
  isAuthenticated: () => Boolean(localStorage.getItem(TOKEN_KEY)),
  getUser: () => {
    const raw = localStorage.getItem(USER_KEY);
    try {
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  }
};
