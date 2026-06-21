import api from './api';

export const inventoryService = {
  list: () => api.get('/inventario').then((response) => response.data),
  listLowStock: () => api.get('/inventario/stock-bajo').then((response) => response.data),
  create: (item) => api.post('/inventario', item).then((response) => response.data),
  update: (id, item) => api.put(`/inventario/${id}`, item).then((response) => response.data),
  remove: (id) => api.delete(`/inventario/${id}`)
};
