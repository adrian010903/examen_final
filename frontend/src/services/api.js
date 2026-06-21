import axios from 'axios';

export const TOKEN_KEY = 'caballeriza_token';
export const USER_KEY = 'caballeriza_user';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY);

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      localStorage.removeItem(TOKEN_KEY);
    }

    const message =
      error.response?.data?.message ||
      error.response?.data?.error ||
      'No se pudo completar la solicitud';

    return Promise.reject(new Error(message));
  }
);

export default api;
