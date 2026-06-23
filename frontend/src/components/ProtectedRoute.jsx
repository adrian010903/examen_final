import { Navigate } from 'react-router-dom';
import { authService } from '../services/authService';
import { canAccessRole, getHomePath, getUserRole } from '../services/roleAccess';

export default function ProtectedRoute({ allowedRoles, children }) {
  if (!authService.isAuthenticated()) {
    return <Navigate to="/login" replace />;
  }

  const user = authService.getUser();
  const role = getUserRole(user);

  if (!canAccessRole(user, allowedRoles)) {
    return <Navigate to={getHomePath(role)} replace />;
  }

  return children;
}
