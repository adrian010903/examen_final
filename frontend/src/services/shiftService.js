import api from './api';

export const shiftService = {
  list: () => api.get('/turnos').then((response) => response.data),
  listByEmployee: (employeeId) => api.get(`/turnos/empleado/${employeeId}`).then((response) => response.data),
  create: (shift) => api.post('/turnos', shift).then((response) => response.data),
  update: (id, shift) => api.put(`/turnos/${id}`, shift).then((response) => response.data),
  remove: (id) => api.delete(`/turnos/${id}`)
};
