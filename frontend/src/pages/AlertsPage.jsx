import {
  AlertTriangle,
  CheckCircle2,
  Mail,
  MailOpen,
  RefreshCw
} from 'lucide-react';
import { useEffect, useState } from 'react';

import PageHeader from '../components/PageHeader';
import { alertService } from '../services/alertService';

export default function AlertsPage() {
  const [alerts, setAlerts] = useState([]);
  const [filter, setFilter] = useState('TODAS');
  const [unreadCount, setUnreadCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadAlerts();
  }, [filter]);

  async function loadAlerts() {
    setLoading(true);
    setError('');

    const params = {};

    if (filter === 'NO_LEIDAS') {
      params.leida = false;
    }

    if (filter === 'LEIDAS') {
      params.leida = true;
    }

    try {
      const [alertsData, unreadData] = await Promise.all([
        alertService.list(params),
        alertService.countUnread()
      ]);

      setAlerts(Array.isArray(alertsData) ? alertsData : []);
      setUnreadCount(unreadData || 0);
    } catch (requestError) {
      setError(
        getErrorMessage(
          requestError,
          'No se pudieron cargar las alertas'
        )
      );
    } finally {
      setLoading(false);
    }
  }

  async function markAsRead(alert) {
    setError('');

    try {
      await alertService.markRead(alert.id);
      await loadAlerts();
    } catch (requestError) {
      setError(
        getErrorMessage(
          requestError,
          'No se pudo marcar la alerta como leída'
        )
      );
    }
  }

  async function markAsUnread(alert) {
    setError('');

    try {
      await alertService.markUnread(alert.id);
      await loadAlerts();
    } catch (requestError) {
      setError(
        getErrorMessage(
          requestError,
          'No se pudo marcar la alerta como no leída'
        )
      );
    }
  }

  return (
    <div className="stack">
      <PageHeader
        title="Alertas"
        description="Vacunaciones próximas, tratamientos vencidos y stock bajo."
      />

      <section className="panel alertsToolbar">
        <div>
          <h3>Bandeja de notificaciones</h3>

          <p>
            Alertas sin leer: <strong>{unreadCount}</strong>
          </p>
        </div>

        <div className="alertsToolbarActions">
          <select
            value={filter}
            onChange={(event) => setFilter(event.target.value)}
          >
            <option value="TODAS">Todas</option>
            <option value="NO_LEIDAS">No leídas</option>
            <option value="LEIDAS">Leídas</option>
          </select>

          <button
            className="secondaryButton"
            type="button"
            onClick={loadAlerts}
          >
            <RefreshCw size={17} />
            Actualizar
          </button>
        </div>
      </section>

      {error && (
        <div className="alertPageError">
          {error}
        </div>
      )}

      {loading && (
        <section className="panel">
          Cargando alertas...
        </section>
      )}

      {!loading && alerts.length === 0 && (
        <article className="alertCard ok">
          <CheckCircle2 size={22} />

          <div>
            <strong>Sin alertas</strong>

            <p>
              No hay alertas que coincidan con el filtro.
            </p>
          </div>
        </article>
      )}

      {!loading && alerts.length > 0 && (
        <section className="alertGrid">
          {alerts.map((alert) => (
            <article
              className={[
                'alertCard',
                alert.severidad === 'ALTA'
                  ? 'high'
                  : 'medium',
                alert.leida
                  ? 'alertRead'
                  : 'alertUnread'
              ].join(' ')}
              key={alert.id}
            >
              <AlertTriangle size={22} />

              <div className="alertCardContent">
                <div className="alertCardHeader">
                  <strong>{alert.titulo}</strong>

                  <span
                    className={
                      alert.leida
                        ? 'readStatus readStatusDone'
                        : 'readStatus readStatusPending'
                    }
                  >
                    {alert.leida ? 'Leída' : 'No leída'}
                  </span>
                </div>

                <p>{alert.descripcion}</p>

                {alert.fecha && (
                  <span className="tag">
                    {alert.fecha}
                  </span>
                )}

                <div className="alertActions">
                  {!alert.leida ? (
                    <button
                      className="secondaryButton"
                      type="button"
                      onClick={() => markAsRead(alert)}
                    >
                      <MailOpen size={16} />
                      Marcar como leída
                    </button>
                  ) : (
                    <button
                      className="secondaryButton"
                      type="button"
                      onClick={() => markAsUnread(alert)}
                    >
                      <Mail size={16} />
                      Marcar como no leída
                    </button>
                  )}
                </div>
              </div>
            </article>
          ))}
        </section>
      )}
    </div>
  );
}

function getErrorMessage(error, fallbackMessage) {
  return (
    error?.response?.data?.detail ||
    error?.response?.data?.message ||
    error?.response?.data?.error ||
    fallbackMessage
  );
}