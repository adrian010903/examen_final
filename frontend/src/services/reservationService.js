import api from './api';

export const reservationService = {
  // Lista todas las reservas o aplica filtros
  async list(filters = {}) {
    const response = await api.get('/reservas', {
      params: filters
    });

    return response.data;
  },

  // Crea una reserva
  async create(reservation) {
    const response = await api.post('/reservas', reservation);
    return response.data;
  },

  // Edita una reserva
  async update(id, reservation) {
    const response = await api.put(`/reservas/${id}`, reservation);
    return response.data;
  },

  // Cancela una reserva
  async cancel(id) {
    const response = await api.patch(`/reservas/${id}/cancelar`);
    return response.data;
  },

  // Elimina una reserva
  async remove(id) {
    await api.delete(`/reservas/${id}`);
  }
};