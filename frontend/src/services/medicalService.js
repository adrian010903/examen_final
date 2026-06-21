import api from './api';

export const medicalService = {
  list: () => api.get('/historial-medico').then((response) => response.data),
  listByHorse: (horseId) => api.get(`/historial-medico/caballo/${horseId}`).then((response) => response.data),
  create: (record) => api.post('/historial-medico', record).then((response) => response.data),
  update: (id, record) => api.put(`/historial-medico/${id}`, record).then((response) => response.data),
  remove: (id) => api.delete(`/historial-medico/${id}`)
};
