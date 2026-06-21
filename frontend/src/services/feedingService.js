import api from './api';

export const feedingService = {
  listPlans: () => api.get('/planes-alimentacion').then((response) => response.data),
  createPlan: (plan) => api.post('/planes-alimentacion', plan).then((response) => response.data),
  updatePlan: (id, plan) => api.put(`/planes-alimentacion/${id}`, plan).then((response) => response.data),
  removePlan: (id) => api.delete(`/planes-alimentacion/${id}`),
  listSupplies: () => api.get('/suministros').then((response) => response.data),
  createSupply: (supply) => api.post('/suministros', supply).then((response) => response.data)
};
