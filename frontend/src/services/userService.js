import api from './api';

export const userService = {
  async list() {
    const response = await api.get('/users');
    return response.data;
  },

  async updateRole(id, role) {
    const response = await api.patch(
      `/users/${id}/rol`,
      { role }
    );

    return response.data;
  },

  async remove(id) {
    await api.delete(`/users/${id}`);
  }
};

