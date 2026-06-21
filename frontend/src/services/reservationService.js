import api from './api';

export const reservationService = {
  list: () => api.get('/reservas').then((response) => response.data),
  create: (reservation) => api.post('/reservas', reservation).then((response) => response.data),
  update: (id, reservation) => api.put(`/reservas/${id}`, reservation).then((response) => response.data),
  cancel: (id) => api.patch(`/reservas/${id}/cancelar`).then((response) => response.data),
  remove: (id) => api.delete(`/reservas/${id}`)
};
