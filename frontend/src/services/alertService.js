import api from './api';

export const alertService = {
  async list(filters = {}) {
    const response = await api.get('/alertas', {
      params: filters
    });

    return response.data;
  },

  async countUnread() {
    const response = await api.get('/alertas/no-leidas/count');
    return response.data.cantidad;
  },

  async markRead(id) {
    const response = await api.patch(
      `/alertas/${id}/marcar-leida`
    );

    return response.data;
  },

  async markUnread(id) {
    const response = await api.patch(
      `/alertas/${id}/marcar-no-leida`
    );

    return response.data;
  }
};