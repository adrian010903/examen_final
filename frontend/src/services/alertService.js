import api from './api';

export const alertService = {
  list: () => api.get('/alertas').then((response) => response.data)
};
