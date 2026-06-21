import api from './api';

export const horseService = {
  list: () => api.get('/caballos').then((response) => response.data),
  get: (id) => api.get(`/caballos/${id}`).then((response) => response.data),
  create: (horse) => api.post('/caballos', horse).then((response) => response.data),
  update: (id, horse) => api.put(`/caballos/${id}`, horse).then((response) => response.data),
  remove: (id) => api.delete(`/caballos/${id}`)
};
