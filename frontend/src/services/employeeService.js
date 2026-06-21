import api from './api';

export const employeeService = {
  list: () => api.get('/empleados').then((response) => response.data),
  get: (id) => api.get(`/empleados/${id}`).then((response) => response.data),
  create: (employee) => api.post('/empleados', employee).then((response) => response.data),
  update: (id, employee) => api.put(`/empleados/${id}`, employee).then((response) => response.data),
  remove: (id) => api.delete(`/empleados/${id}`)
};
