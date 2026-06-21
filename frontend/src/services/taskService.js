import api from './api';

export const taskService = {
  list: () => api.get('/tareas').then((response) => response.data),
  listByEmployee: (employeeId) => api.get(`/tareas/empleado/${employeeId}`).then((response) => response.data),
  listByHorse: (horseId) => api.get(`/tareas/caballo/${horseId}`).then((response) => response.data),
  create: (task) => api.post('/tareas', task).then((response) => response.data),
  update: (id, task) => api.put(`/tareas/${id}`, task).then((response) => response.data),
  remove: (id) => api.delete(`/tareas/${id}`)
};
