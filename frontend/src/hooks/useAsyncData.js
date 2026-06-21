import { useCallback, useEffect, useState } from 'react';

export function useAsyncData(loader, fallback = []) {
  const [data, setData] = useState(fallback);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = useCallback(async () => {
    setLoading(true);
    setError('');

    try {
      const result = await loader();
      setData(Array.isArray(result) ? result : result ? [result] : []);
    } catch (exception) {
      setData(fallback);
      setError('Backend no disponible o sin autorizacion. Se muestran datos de ejemplo.');
    } finally {
      setLoading(false);
    }
  }, [fallback, loader]);

  useEffect(() => {
    load();
  }, [load]);

  return { data, setData, loading, error, reload: load };
}
